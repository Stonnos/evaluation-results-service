package com.ers.controller;

import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.service.ClassifierOptionsRequestService;
import com.ers.service.EvaluationResultsService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static com.ers.config.MetricConstants.GET_EVALUATION_RESULTS_TIMED_METRIC_NAME;
import static com.ers.config.MetricConstants.GET_OPTIMAL_CLASSIFIER_OPTIONS_TIMED_METRIC_NAME;
import static com.ers.config.MetricConstants.SAVE_EVALUATION_RESULTS_TIMED_METRIC_NAME;
import static com.ers.config.WebServiceConfiguration.TARGET_NAMESPACE;

/**
 * Evaluation results service endpoint.
 *
 * @author Roman Batygin
 */
@Slf4j
@Endpoint
@RequiredArgsConstructor
public class EvaluationResultsEndpoint {

    private static final String EVALUATION_RESULTS_REQUEST_LOCAL_PART = "evaluationResultsRequest";
    private static final String CLASSIFIER_OPTIONS_REQUEST_LOCAL_PART = "classifierOptionsRequest";
    private static final String GET_EVALUATION_RESULTS_REQUEST_LOCAL_PART = "getEvaluationResultsRequest";

    private final EvaluationResultsService evaluationResultsService;
    private final ClassifierOptionsRequestService classifierOptionsRequestService;

    /**
     * Saves evaluation results report to database.
     *
     * @param evaluationResultsRequest - evaluation result request
     * @return evaluation results response
     */
    @Timed(value = SAVE_EVALUATION_RESULTS_TIMED_METRIC_NAME)
    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = EVALUATION_RESULTS_REQUEST_LOCAL_PART)
    @ResponsePayload
    public EvaluationResultsResponse save(@RequestPayload EvaluationResultsRequest evaluationResultsRequest) {
        return evaluationResultsService.saveEvaluationResults(evaluationResultsRequest);
    }

    /**
     * Gets evaluation results simple report.
     *
     * @param request - get evaluation result request
     * @return evaluation results response
     */
    @Timed(value = GET_EVALUATION_RESULTS_TIMED_METRIC_NAME)
    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = GET_EVALUATION_RESULTS_REQUEST_LOCAL_PART)
    @ResponsePayload
    public GetEvaluationResultsResponse getEvaluationResultsResponse(
            @RequestPayload GetEvaluationResultsRequest request) {
        return evaluationResultsService.getEvaluationResultsResponse(request);
    }

    /**
     * End point for searching the best classifiers options for specified request.
     *
     * @param classifierOptionsRequest - classifier options request
     * @return classifier options response
     */
    @Timed(value = GET_OPTIMAL_CLASSIFIER_OPTIONS_TIMED_METRIC_NAME)
    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = CLASSIFIER_OPTIONS_REQUEST_LOCAL_PART)
    @ResponsePayload
    public ClassifierOptionsResponse findClassifierOptions(
            @RequestPayload ClassifierOptionsRequest classifierOptionsRequest) {
        return classifierOptionsRequestService.findClassifierOptions(classifierOptionsRequest);
    }
}
