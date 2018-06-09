package com.ers.controller;

import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.exception.DataNotFoundException;
import com.ers.mapping.ClassifierOptionsInfoMapper;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.service.ClassifierOptionsService;
import com.ers.service.EvaluationResultsService;
import com.ers.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * Evaluation results service endpoint.
 *
 * @author Roman Batygin
 */
@Slf4j
@Endpoint
public class EvaluationResultsEndpoint {

    private static final String NAMESPACE_URI = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String EVALUATION_RESULTS_REQUEST_LOCAL_PART = "evaluationResultsRequest";
    private static final String CLASSIFIER_OPTIONS_REQUEST_LOCAL_PART = "classifierOptionsRequest";

    private final EvaluationResultsService evaluationResultsService;
    private final ClassifierOptionsService classifierOptionsService;
    private final ClassifierOptionsInfoMapper classifierOptionsInfoMapper;

    /**
     * Constructor with spring dependency injection.
     *
     * @param evaluationResultsService    - evaluation results service bean
     * @param classifierOptionsService    - classification options service bean
     * @param classifierOptionsInfoMapper - classifier options info mapper bean
     */
    @Inject
    public EvaluationResultsEndpoint(EvaluationResultsService evaluationResultsService,
                                     ClassifierOptionsService classifierOptionsService,
                                     ClassifierOptionsInfoMapper classifierOptionsInfoMapper) {
        this.evaluationResultsService = evaluationResultsService;
        this.classifierOptionsService = classifierOptionsService;
        this.classifierOptionsInfoMapper = classifierOptionsInfoMapper;
    }

    /**
     * Saves evaluation results report to database.
     *
     * @param evaluationResultsRequest - evaluation result request
     * @return evaluation results response
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = EVALUATION_RESULTS_REQUEST_LOCAL_PART)
    @ResponsePayload
    public EvaluationResultsResponse save(@RequestPayload EvaluationResultsRequest evaluationResultsRequest) {
        return evaluationResultsService.saveEvaluationResults(evaluationResultsRequest);
    }

    /**
     * End point for searching the best classifiers options for specified request.
     *
     * @param classifierOptionsRequest - classifier options request
     * @return classifier options response
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = CLASSIFIER_OPTIONS_REQUEST_LOCAL_PART)
    @ResponsePayload
    public ClassifierOptionsResponse findClassifierOptions(
            @RequestPayload ClassifierOptionsRequest classifierOptionsRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Received request [{}] for searching the best classifiers options.", requestId);
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        if (!Utils.validateClassifierOptionsRequest(classifierOptionsRequest)) {
            responseStatus = ResponseStatus.INVALID_REQUEST_PARAMS;
        } else {
            try {
                log.info(
                        "Starting to find the best classifiers options with request id [{}] for data '{}' classification.",
                        requestId, classifierOptionsRequest.getInstances().getRelationName());
                List<ClassifierOptionsInfo> classifierOptionsInfoList =
                        classifierOptionsService.findBestClassifierOptions(classifierOptionsRequest);
                if (CollectionUtils.isEmpty(classifierOptionsInfoList)) {
                    responseStatus = ResponseStatus.RESULTS_NOT_FOUND;
                } else {
                    log.info("{} best classifiers options has been found for data '{}', request id [{}]",
                            classifierOptionsRequest.getInstances().getRelationName(), requestId);
                    return Utils.buildClassifierOptionsResponse(requestId,
                            classifierOptionsInfoMapper.map(classifierOptionsInfoList), responseStatus);
                }
            } catch (DataNotFoundException ex) {
                log.warn(ex.getMessage());
                responseStatus = ResponseStatus.DATA_NOT_FOUND;
            } catch (Exception ex) {
                log.error(ex.getMessage());
                responseStatus = ResponseStatus.ERROR;
            }
        }
        return Utils.buildClassifierOptionsResponse(requestId, responseStatus);
    }
}
