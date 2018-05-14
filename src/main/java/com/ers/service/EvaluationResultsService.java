package com.ers.service;

import com.ers.config.ServiceConfig;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.InstancesReport;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.EvaluationResultsRequestMapper;
import com.ers.model.EvaluationResultsInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.util.FileUtils;
import com.ers.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements service for saving evaluation results into database.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
public class EvaluationResultsService {

    private final ServiceConfig serviceConfig;
    private final EvaluationResultsRequestMapper evaluationResultsRequestMapper;
    private final EvaluationResultsInfoRepository evaluationResultsInfoRepository;

    private ConcurrentHashMap<String, Object> cachedIds = new ConcurrentHashMap<>();

    /**
     * Constructor with spring dependency injection.
     *
     * @param serviceConfig                   - service config bean
     * @param evaluationResultsRequestMapper  - evaluation results request mapper bean
     * @param evaluationResultsInfoRepository - evaluation results info repository bean
     */
    @Inject
    public EvaluationResultsService(ServiceConfig serviceConfig,
                                    EvaluationResultsRequestMapper evaluationResultsRequestMapper,
                                    EvaluationResultsInfoRepository evaluationResultsInfoRepository) {
        this.serviceConfig = serviceConfig;
        this.evaluationResultsRequestMapper = evaluationResultsRequestMapper;
        this.evaluationResultsInfoRepository = evaluationResultsInfoRepository;
    }

    /**
     * Saves evaluation results report into database.
     *
     * @param evaluationResultsRequest - evaluation results report
     * @return evaluation results response
     */
    public EvaluationResultsResponse saveEvaluationResults(EvaluationResultsRequest evaluationResultsRequest) {
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        if (!Utils.hasRequestId(evaluationResultsRequest)) {
            log.error("Request id isn't specified!");
            responseStatus = ResponseStatus.INVALID_REQUEST_ID;
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
                                evaluationResultsRequestMapper.map(evaluationResultsRequest);
                        saveXmlData(evaluationResultsRequest, evaluationResultsInfo);
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

    private void saveXmlData(EvaluationResultsRequest evaluationResultsRequest,
                             EvaluationResultsInfo evaluationResultsInfo) {
        if (Optional.ofNullable(evaluationResultsRequest.getInstances()).map(InstancesReport::getXmlData).isPresent()) {
            File xmlDataFile = new File(serviceConfig.getDataStoragePath(),
                    String.format(serviceConfig.getFileFormat(), System.currentTimeMillis()));
            FileUtils.saveXmlToFile(evaluationResultsRequest.getInstances().getXmlData(), xmlDataFile);
            evaluationResultsInfo.getInstances().setDataPath(xmlDataFile.getAbsolutePath());
        }
    }
}
