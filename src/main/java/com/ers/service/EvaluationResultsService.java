package com.ers.service;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsSimpleRequest;
import com.ers.dto.GetEvaluationResultsSimpleResponse;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.EvaluationResultsMapper;
import com.ers.mapping.InstancesMapper;
import com.ers.model.EvaluationResultsInfo;
import com.ers.model.InstancesInfo;
import com.ers.projection.EvaluationResultsSimpleInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.repository.InstancesInfoRepository;
import com.ers.util.Utils;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static com.ers.util.Utils.buildEvaluationResultsResponse;
import static com.ers.util.Utils.hasRequestId;
import static com.ers.util.Utils.validateEvaluationResultsRequest;

/**
 * Implements service for saving evaluation results into database.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
public class EvaluationResultsService {

    private final EvaluationResultsMapper evaluationResultsMapper;
    private final InstancesMapper instancesMapper;
    private final EvaluationResultsInfoRepository evaluationResultsInfoRepository;
    private final InstancesInfoRepository instancesInfoRepository;

    private ConcurrentHashMap<String, Object> cachedIds = new ConcurrentHashMap<>();

    /**
     * Constructor with spring dependency injection.
     *
     * @param evaluationResultsMapper         - evaluation results mapper bean
     * @param instancesMapper                 - instances mapper bean
     * @param evaluationResultsInfoRepository - evaluation results info repository bean
     * @param instancesInfoRepository         - instances info repository bean
     */
    @Inject
    public EvaluationResultsService(EvaluationResultsMapper evaluationResultsMapper,
                                    InstancesMapper instancesMapper,
                                    EvaluationResultsInfoRepository evaluationResultsInfoRepository,
                                    InstancesInfoRepository instancesInfoRepository) {
        this.evaluationResultsMapper = evaluationResultsMapper;
        this.instancesMapper = instancesMapper;
        this.evaluationResultsInfoRepository = evaluationResultsInfoRepository;
        this.instancesInfoRepository = instancesInfoRepository;
    }

    /**
     * Saves evaluation results report into database.
     *
     * @param evaluationResultsRequest - evaluation results report
     * @return evaluation results response
     */
    @Transactional
    public EvaluationResultsResponse saveEvaluationResults(EvaluationResultsRequest evaluationResultsRequest) {
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        if (!hasRequestId(evaluationResultsRequest)) {
            log.error("Request id isn't specified!");
            responseStatus = ResponseStatus.INVALID_REQUEST_ID;
        } else if (!validateEvaluationResultsRequest(evaluationResultsRequest)) {
            log.error("Required request params isn't specified!");
            responseStatus = ResponseStatus.INVALID_REQUEST_PARAMS;
        } else {
            log.info("Starting to save evaluation results report with request id = {}.",
                    evaluationResultsRequest.getRequestId());
            cachedIds.putIfAbsent(evaluationResultsRequest.getRequestId(), new Object());
            synchronized (cachedIds.get(evaluationResultsRequest.getRequestId())) {
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
            cachedIds.remove(evaluationResultsRequest.getRequestId());
        }
        return Utils.buildResponse(evaluationResultsRequest.getRequestId(), responseStatus);
    }

    /**
     * Gets evaluation results simple response.
     *
     * @param request - evaluation results request
     * @return evaluation results simple response
     */
    public GetEvaluationResultsSimpleResponse getEvaluationResultsSimpleResponse(
            GetEvaluationResultsSimpleRequest request) {
        log.info("Starting to get simple evaluation results for request id [{}]", request.getRequestId());
        ResponseStatus responseStatus;
        if (!hasRequestId(request)) {
            log.error("Request id isn't specified!");
            responseStatus = ResponseStatus.INVALID_REQUEST_ID;
        } else {
            try {
                EvaluationResultsSimpleInfo evaluationResultsSimpleInfo =
                        evaluationResultsInfoRepository.findEvaluationResultsSimpleInfo(request.getRequestId());
                if (evaluationResultsSimpleInfo == null) {
                    log.info("Evaluation results not found for request id [{}]", request.getRequestId());
                    responseStatus = ResponseStatus.RESULTS_NOT_FOUND;
                } else {
                    GetEvaluationResultsSimpleResponse response =
                            evaluationResultsMapper.map(evaluationResultsSimpleInfo);
                    response.setStatus(ResponseStatus.SUCCESS);
                    log.info("Received simple evaluation results for request id [{}]", request.getRequestId());
                    return response;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
                responseStatus = ResponseStatus.ERROR;
            }
        }
        return buildEvaluationResultsResponse(request.getRequestId(), responseStatus);
    }

    private synchronized void populateAndSaveInstancesInfo(EvaluationResultsRequest evaluationResultsRequest,
                                                           EvaluationResultsInfo evaluationResultsInfo) {
        if (evaluationResultsRequest.getInstances() != null) {
            String xmlData = evaluationResultsRequest.getInstances().getXmlInstances();
            byte[] xmlDataBytes = xmlData.getBytes(Charsets.UTF_8);
            String md5Hash = DigestUtils.md5DigestAsHex(xmlDataBytes);
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
    }
}
