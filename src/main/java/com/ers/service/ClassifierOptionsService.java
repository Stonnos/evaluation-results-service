package com.ers.service;

import com.ers.config.ErsConfig;
import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.EvaluationMethodReport;
import com.ers.exception.DataNotFoundException;
import com.ers.filter.EvaluationResultsFilter;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.model.EvaluationResultsInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.repository.InstancesInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements service for searching the best classifier options.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassifierOptionsService {

    private final InstancesInfoRepository instancesInfoRepository;
    private final EvaluationResultsInfoRepository evaluationResultsInfoRepository;
    private final ErsConfig ersConfig;
    private final SortFieldService sortFieldService;

    /**
     * Finds the best classifiers options.
     *
     * @param classifierOptionsRequest - classifier options request
     * @return the best classifiers options list
     */
    public List<ClassifierOptionsInfo> findBestClassifierOptions(ClassifierOptionsRequest classifierOptionsRequest) {
        String xmlData = classifierOptionsRequest.getInstances().getXmlInstances();
        String md5Hash = DigestUtils.md5DigestAsHex(xmlData.getBytes(StandardCharsets.UTF_8));
        Long instancesInfoId = instancesInfoRepository.findIdByDataMd5Hash(md5Hash);
        if (instancesInfoId == null) {
            throw new DataNotFoundException(String.format("Instances '%s' doesn't exists!",
                    classifierOptionsRequest.getInstances().getRelationName()));
        } else {
            EvaluationMethodReport evaluationMethodReport = classifierOptionsRequest.getEvaluationMethodReport();
            EvaluationResultsFilter filter = new EvaluationResultsFilter(instancesInfoId, evaluationMethodReport);
            Sort sort = sortFieldService.getEvaluationResultsSort();
            PageRequest pageRequest = PageRequest.of(0, ersConfig.getResultSize(), sort);
            Page<EvaluationResultsInfo> evaluationResultsInfoPage =
                    evaluationResultsInfoRepository.findAll(filter, pageRequest);
            return evaluationResultsInfoPage.getContent().stream().map(
                    EvaluationResultsInfo::getClassifierOptionsInfo).collect(Collectors.toList());
        }
    }
}
