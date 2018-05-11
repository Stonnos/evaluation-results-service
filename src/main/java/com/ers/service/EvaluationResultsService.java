package com.ers.service;

import com.ers.dto.EvaluationResultsReport;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.EvaluationResultsReportMapper;
import com.ers.model.EvaluationResultsInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements service for saving evaluation results into database.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
public class EvaluationResultsService {

    private final EvaluationResultsReportMapper evaluationResultsReportMapper;
    private final EvaluationResultsInfoRepository evaluationResultsInfoRepository;

    private ConcurrentHashMap<String, Object> cachedIds = new ConcurrentHashMap<>();

    /**
     * Constructor with spring dependency injection.
     *
     * @param evaluationResultsReportMapper   - evaluation results report mapper bean
     * @param evaluationResultsInfoRepository - evaluation results info repository bean
     */
    @Inject
    public EvaluationResultsService(EvaluationResultsReportMapper evaluationResultsReportMapper,
                                    EvaluationResultsInfoRepository evaluationResultsInfoRepository) {
        this.evaluationResultsReportMapper = evaluationResultsReportMapper;
        this.evaluationResultsInfoRepository = evaluationResultsInfoRepository;
    }

    /**
     * Saves evaluation results report into database.
     *
     * @param evaluationResultsReport - evaluation results report
     * @return evaluation results response
     */
    public EvaluationResultsResponse saveEvaluationResults(EvaluationResultsReport evaluationResultsReport) {
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        if (!Utils.hasRequestId(evaluationResultsReport)) {
            log.error("Request id isn't specified!");
            responseStatus = ResponseStatus.INVALID_REQUEST_ID;
        } else {
            log.info("Starting to save evaluation results report with request id = {}.",
                    evaluationResultsReport.getRequestId());
            cachedIds.putIfAbsent(evaluationResultsReport.getRequestId(), new Object());
            synchronized (cachedIds.get(evaluationResultsReport.getRequestId())) {
                if (evaluationResultsInfoRepository.existsByRequestId(evaluationResultsReport.getRequestId())) {
                    log.warn("Evaluation results with request id = {} is already exists!",
                            evaluationResultsReport.getRequestId());
                    responseStatus = ResponseStatus.DUPLICATE_REQUEST_ID;
                } else {
                    try {
                        EvaluationResultsInfo evaluationResultsInfo =
                                evaluationResultsReportMapper.map(evaluationResultsReport);
                        evaluationResultsInfo.setSaveDate(LocalDateTime.now());
                        evaluationResultsInfoRepository.save(evaluationResultsInfo);
                        log.info("Evaluation results report with request id = {} has been successfully saved.",
                                evaluationResultsReport.getRequestId());
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        responseStatus = ResponseStatus.ERROR;
                    }
                }
            }
            cachedIds.remove(evaluationResultsReport.getRequestId());
        }
        return Utils.buildResponse(evaluationResultsReport.getRequestId(), responseStatus);
    }
}
