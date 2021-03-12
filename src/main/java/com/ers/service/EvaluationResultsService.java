package com.ers.service;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.EvaluationResultsMapper;
import com.ers.model.EvaluationResultsInfo;
import com.ers.model.InstancesInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static com.ers.util.Utils.buildEvaluationResultsResponse;

/**
 * Implements service for saving evaluation results into database.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationResultsService {

    private final InstancesService instancesService;
    private final EvaluationResultsMapper evaluationResultsMapper;
    private final EvaluationResultsInfoRepository evaluationResultsInfoRepository;

    private ConcurrentHashMap<String, Object> cachedRequestIds = new ConcurrentHashMap<>();

    /**
     * Saves evaluation results report into database.
     *
     * @param evaluationResultsRequest - evaluation results report
     * @return evaluation results response
     */
    public EvaluationResultsResponse saveEvaluationResults(EvaluationResultsRequest evaluationResultsRequest) {
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        log.info("Starting to save evaluation results report with request id = {}.",
                evaluationResultsRequest.getRequestId());
        cachedRequestIds.putIfAbsent(evaluationResultsRequest.getRequestId(), new Object());
        synchronized (cachedRequestIds.get(evaluationResultsRequest.getRequestId())) {
            if (evaluationResultsInfoRepository.existsByRequestId(evaluationResultsRequest.getRequestId())) {
                log.warn("Evaluation results with request id = {} is already exists!",
                        evaluationResultsRequest.getRequestId());
                responseStatus = ResponseStatus.DUPLICATE_REQUEST_ID;
            } else {
                InstancesInfo instancesInfo = instancesService.getOrSaveInstancesInfo(evaluationResultsRequest);
                EvaluationResultsInfo evaluationResultsInfo =
                        evaluationResultsMapper.map(evaluationResultsRequest);
                evaluationResultsInfo.setInstances(instancesInfo);
                evaluationResultsInfo.setSaveDate(LocalDateTime.now());
                evaluationResultsInfoRepository.save(evaluationResultsInfo);
                log.info("Evaluation results report with request id = {} has been successfully saved.",
                        evaluationResultsRequest.getRequestId());
            }
        }
        cachedRequestIds.remove(evaluationResultsRequest.getRequestId());
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
        return buildEvaluationResultsResponse(request.getRequestId(), responseStatus);
    }
}
