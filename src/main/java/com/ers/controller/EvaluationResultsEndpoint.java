package com.ers.controller;

import com.ers.dto.EvaluationResultsReport;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.service.EvaluationResultsService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.inject.Inject;

/**
 * Evaluation results storage endpoint.
 *
 * @author Roman Batygin
 */
@Endpoint
public class EvaluationResultsEndpoint {

    private static final String NAMESPACE_URI = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String LOCAL_PART = "evaluationResultsReport";

    private final EvaluationResultsService evaluationResultsService;

    /**
     * Constructor with spring dependency injection.
     *
     * @param evaluationResultsService - evaluation results service bean
     */
    @Inject
    public EvaluationResultsEndpoint(EvaluationResultsService evaluationResultsService) {
        this.evaluationResultsService = evaluationResultsService;
    }

    /**
     * Saves evaluation results report to database.
     *
     * @param evaluationResultsReport - evaluation result report
     * @return evaluation results response
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART)
    @ResponsePayload
    public EvaluationResultsResponse save(@RequestPayload EvaluationResultsReport evaluationResultsReport) {
        return evaluationResultsService.saveEvaluationResults(evaluationResultsReport);
    }

    /**
     * Saves evaluation results report to database.
     *
     * @param evaluationRequest - evaluation result report
     * @return evaluation results response
     */
    /*@PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART)
    @ResponsePayload
    public EvaluationResultsResponse saveReport(@RequestPayload EvaluationRequest evaluationRequest) {
        log.info("Received evaluation results report with uuid = {}.", evaluationRequest.getUuid());
        EvaluationResultsResponse evaluationResultsResponse = new EvaluationResultsResponse();
        evaluationResultsResponse.setUuid(evaluationRequest.getUuid());
        evaluationResultsResponse.setStatus(ResponseStatus.SUCCESS);
        return evaluationResultsResponse;
    }*/
}
