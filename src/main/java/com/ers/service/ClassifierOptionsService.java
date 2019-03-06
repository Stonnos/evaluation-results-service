package com.ers.service;

import com.ers.config.ServiceConfig;
import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.EvaluationMethodReport;
import com.ers.exception.DataNotFoundException;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.repository.ClassifierOptionsInfoRepository;
import com.ers.repository.InstancesInfoRepository;
import com.ers.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * Implements service for searching the best classifier options.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
public class ClassifierOptionsService {

    private final InstancesInfoRepository instancesInfoRepository;
    private final ClassifierOptionsInfoRepository classifierOptionsInfoRepository;
    private final ServiceConfig serviceConfig;

    /**
     * Constructor with spring dependency injection.
     *
     * @param instancesInfoRepository         - instances info repository bean
     * @param classifierOptionsInfoRepository - classifier options info repository bean
     * @param serviceConfig                   - service config bean
     */
    @Inject
    public ClassifierOptionsService(InstancesInfoRepository instancesInfoRepository,
                                    ClassifierOptionsInfoRepository classifierOptionsInfoRepository,
                                    ServiceConfig serviceConfig) {
        this.instancesInfoRepository = instancesInfoRepository;
        this.classifierOptionsInfoRepository = classifierOptionsInfoRepository;
        this.serviceConfig = serviceConfig;
    }

    /**
     * Finds the best classifiers options.
     *
     * @param classifierOptionsRequest - classifier options request
     * @return the best classifiers options list
     */
    public List<ClassifierOptionsInfo> findBestClassifierOptions(ClassifierOptionsRequest classifierOptionsRequest) {
        String xmlData = classifierOptionsRequest.getInstances().getXmlInstances();
        String md5Hash = DigestUtils.md5DigestAsHex(xmlData.getBytes());
        Long instancesInfoId = instancesInfoRepository.findIdByDataMd5Hash(md5Hash);
        if (instancesInfoId == null) {
            throw new DataNotFoundException(String.format("Instances '%s' doesn't exists!",
                    classifierOptionsRequest.getInstances().getRelationName()));
        } else {
            EvaluationMethodReport evaluationMethodReport = classifierOptionsRequest.getEvaluationMethodReport();
            List<ClassifierOptionsInfo> classifierOptionsInfoList;
            PageRequest pageRequest = PageRequest.of(0, serviceConfig.getResultSize());
            switch (evaluationMethodReport.getEvaluationMethod()) {

                case TRAINING_DATA:
                    classifierOptionsInfoList =
                            classifierOptionsInfoRepository.findTopClassifierOptions(instancesInfoId, pageRequest);
                    break;

                case CROSS_VALIDATION:
                    classifierOptionsInfoList =
                            classifierOptionsInfoRepository.findTopClassifierOptionsByCrossValidation(instancesInfoId,
                                    Utils.toInteger(evaluationMethodReport.getNumFolds()),
                                    Utils.toInteger(evaluationMethodReport.getNumTests()),
                                    Utils.toInteger(evaluationMethodReport.getSeed()), pageRequest);
                    break;

                default:
                    throw new IllegalArgumentException(String.format("Unexpected evaluation method: %s!",
                            evaluationMethodReport.getEvaluationMethod()));
            }
            return classifierOptionsInfoList;
        }
    }
}
