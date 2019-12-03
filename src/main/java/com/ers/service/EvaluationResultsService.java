package com.ers.service;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.EvaluationResultsMapper;
import com.ers.mapping.InstancesMapper;
import com.ers.model.EvaluationResultsInfo;
import com.ers.model.InstancesInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.repository.InstancesInfoRepository;
import com.ers.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static com.ers.util.Utils.buildEvaluationResultsResponse;
import static com.ers.util.Utils.hasValidRequestId;
import static com.ers.util.Utils.validateEvaluationResultsRequest;

/**
 * Implements service for saving evaluation results into database.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationResultsService {

    private final EvaluationResultsMapper evaluationResultsMapper;
    private final InstancesMapper instancesMapper;
    private final EvaluationResultsInfoRepository evaluationResultsInfoRepository;
    private final InstancesInfoRepository instancesInfoRepository;

    private ConcurrentHashMap<String, Object> cachedRequestIds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> cachedDataMd5Hashes = new ConcurrentHashMap<>();

    /**
     * Saves evaluation results report into database.
     *
     * @param evaluationResultsRequest - evaluation results report
     * @return evaluation results response
     */
    public EvaluationResultsResponse saveEvaluationResults(EvaluationResultsRequest evaluationResultsRequest) {
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        if (!hasValidRequestId(evaluationResultsRequest)) {
            log.error("Request id doesn't match UUID format!");
            responseStatus = ResponseStatus.INVALID_REQUEST_ID;
        } else if (!validateEvaluationResultsRequest(evaluationResultsRequest)) {
            log.error("Required request params isn't specified!");
            responseStatus = ResponseStatus.INVALID_REQUEST_PARAMS;
        } else {
            log.info("Starting to save evaluation results report with request id = {}.",
                    evaluationResultsRequest.getRequestId());
            cachedRequestIds.putIfAbsent(evaluationResultsRequest.getRequestId(), new Object());
            synchronized (cachedRequestIds.get(evaluationResultsRequest.getRequestId())) {
                if (evaluationResultsInfoRepository.existsByRequestId(evaluationResultsRequest.getRequestId())) {
                    log.warn("Evaluation results with request id = {} is already exists!",
                            evaluationResultsRequest.getRequestId());
                    responseStatus = ResponseStatus.DUPLICATE_REQUEST_ID;
                } else {
                    try {
                        EvaluationResultsInfo evaluationResultsInfo =
                                evaluationResultsMapper.map(evaluationResultsRequest);
                        populateAndSaveInstancesInfo(evaluationResultsRequest, evaluationResultsInfo);
                        evaluationResultsInfo.setSaveDate(LocalDateTime.now());
                        evaluationResultsInfoRepository.save(evaluationResultsInfo);
                        log.info("Evaluation results report with request id = {} has been successfully saved.",
                                evaluationResultsRequest.getRequestId());
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        responseStatus = ResponseStatus.ERROR;
                    }
                }
            }
            cachedRequestIds.remove(evaluationResultsRequest.getRequestId());
        }
        return Utils.buildResponse(evaluationResultsRequest.getRequestId(), responseStatus);
    }

    /**
     * Gets evaluation results response.
     *
     * @param request - evaluation results request
     * @return evaluation results simple response
     */
    @Transactional
    public GetEvaluationResultsResponse getEvaluationResultsResponse(GetEvaluationResultsRequest request) {
        log.info("Starting to get evaluation results for request id [{}]", request.getRequestId());
        ResponseStatus responseStatus;
        if (!hasValidRequestId(request)) {
            log.error("Request id doesn't match UUID format!");
            responseStatus = ResponseStatus.INVALID_REQUEST_ID;
        } else {
            try {
                EvaluationResultsInfo evaluationResultsInfo =
                        evaluationResultsInfoRepository.findByRequestId(request.getRequestId());
                if (evaluationResultsInfo == null) {
                    log.info("Evaluation results not found for request id [{}]", request.getRequestId());
                    responseStatus = ResponseStatus.RESULTS_NOT_FOUND;
                } else {
                    GetEvaluationResultsResponse response = evaluationResultsMapper.map(evaluationResultsInfo);
                    response.setStatus(ResponseStatus.SUCCESS);
                    log.info("Received evaluation results for request id [{}]", request.getRequestId());
                    return response;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
                responseStatus = ResponseStatus.ERROR;
            }
        }
        return buildEvaluationResultsResponse(request.getRequestId(), responseStatus);
    }

    private void populateAndSaveInstancesInfo(EvaluationResultsRequest evaluationResultsRequest,
                                              EvaluationResultsInfo evaluationResultsInfo) {
        String xmlData = evaluationResultsRequest.getInstances().getXmlInstances();
        byte[] xmlDataBytes = xmlData.getBytes(StandardCharsets.UTF_8);
        String md5Hash = DigestUtils.md5DigestAsHex(xmlDataBytes);
        cachedDataMd5Hashes.putIfAbsent(md5Hash, new Object());
        synchronized (cachedDataMd5Hashes.get(md5Hash)) {
            InstancesInfo instancesInfo;
            Long instancesInfoId = instancesInfoRepository.findIdByDataMd5Hash(md5Hash);
            if (instancesInfoId != null) {
                instancesInfo = new InstancesInfo();
                instancesInfo.setId(instancesInfoId);
            } else {
                instancesInfo = instancesMapper.map(evaluationResultsRequest.getInstances());
                instancesInfo.setXmlData(xmlDataBytes);
                instancesInfo.setDataMd5Hash(md5Hash);
                instancesInfoRepository.save(instancesInfo);
            }
            evaluationResultsInfo.setInstances(instancesInfo);
        }
        cachedDataMd5Hashes.remove(md5Hash);
    }
}
