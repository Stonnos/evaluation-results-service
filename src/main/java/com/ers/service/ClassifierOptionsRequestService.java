package com.ers.service;

import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.exception.DataNotFoundException;
import com.ers.mapping.ClassifierOptionsInfoMapper;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * Service for handling classifier options requests.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassifierOptionsRequestService {

    private final ClassifierOptionsService classifierOptionsService;
    private final ClassifierOptionsInfoMapper classifierOptionsInfoMapper;

    /**
     * Finds the best classifiers options.
     *
     * @param classifierOptionsRequest - classifier options request
     * @return classifier options response
     */
    public ClassifierOptionsResponse findClassifierOptions(ClassifierOptionsRequest classifierOptionsRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Received request [{}] for searching the best classifiers options.", requestId);
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        try {
            log.info(
                    "Starting to find the best classifiers options with request id [{}] for data '{}' classification.",
                    requestId, classifierOptionsRequest.getInstances().getRelationName());
            List<ClassifierOptionsInfo> classifierOptionsInfoList =
                    classifierOptionsService.findBestClassifierOptions(classifierOptionsRequest);
            if (CollectionUtils.isEmpty(classifierOptionsInfoList)) {
                log.info("Best classifiers options not found for data '{}', request id [{}]",
                        classifierOptionsRequest.getInstances().getRelationName(), requestId);
                responseStatus = ResponseStatus.RESULTS_NOT_FOUND;
            } else {
                log.info("{} best classifiers options has been found for data '{}', request id [{}]",
                        classifierOptionsInfoList.size(), classifierOptionsRequest.getInstances().getRelationName(),
                        requestId);
                return Utils.buildClassifierOptionsResponse(requestId,
                        classifierOptionsInfoMapper.map(classifierOptionsInfoList), responseStatus);
            }
        } catch (DataNotFoundException ex) {
            log.warn(ex.getMessage());
            responseStatus = ResponseStatus.DATA_NOT_FOUND;
        }
        return Utils.buildClassifierOptionsResponse(requestId, responseStatus);
    }
}
