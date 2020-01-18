package com.ers.service;

import com.ers.AbstractJpaTest;
import com.ers.TestHelperUtils;
import com.ers.config.ServiceConfig;
import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.EvaluationMethod;
import com.ers.exception.DataNotFoundException;
import com.ers.mapping.EvaluationMethodMapper;
import com.ers.mapping.EvaluationMethodMapperImpl;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.model.EvaluationResultsInfo;
import com.ers.model.InstancesInfo;
import com.ers.repository.ClassifierOptionsInfoRepository;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.repository.InstancesInfoRepository;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.context.annotation.Import;
import org.springframework.util.DigestUtils;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for checking {@link ClassifierOptionsService} functionality.
 *
 * @author Roman Batygin
 */
@Import({ClassifierOptionsService.class, ServiceConfig.class, EvaluationMethodMapperImpl.class})
public class ClassifierOptionsServiceTest extends AbstractJpaTest {

    @Inject
    private ClassifierOptionsInfoRepository classifierOptionsInfoRepository;
    @Inject
    private InstancesInfoRepository instancesInfoRepository;
    @Inject
    private EvaluationResultsInfoRepository evaluationResultsInfoRepository;
    @Inject
    private ClassifierOptionsService classifierOptionsService;
    @Inject
    private ServiceConfig serviceConfig;
    @Inject
    private EvaluationMethodMapper evaluationMethodMapper;

    @Override
    public void deleteAll() {
        evaluationResultsInfoRepository.deleteAll();
        classifierOptionsInfoRepository.deleteAll();
        instancesInfoRepository.deleteAll();
    }

    @Test(expected = DataNotFoundException.class)
    public void testDataNotFoundException() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.TRAINING_DATA);
        classifierOptionsService.findBestClassifierOptions(request);
    }

    @Test
    public void testClassifierOptionsSearchingWithTrainingDataEvaluationMethod() {
        testClassifierOptionsSearching(EvaluationMethod.TRAINING_DATA);
    }

    @Test
    public void testClassifierOptionsSearchingWithCrossValidation() {
        testClassifierOptionsSearching(EvaluationMethod.CROSS_VALIDATION);
    }

    private void testClassifierOptionsSearching(EvaluationMethod evaluationMethod) {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(evaluationMethod);
        InstancesInfo instancesInfo = new InstancesInfo();
        instancesInfo.setDataMd5Hash(
                DigestUtils.md5DigestAsHex(request.getInstances().getXmlInstances().getBytes(StandardCharsets.UTF_8)));
        InstancesInfo anotherInstancesInfo = new InstancesInfo();
        anotherInstancesInfo.setDataMd5Hash(
                DigestUtils.md5DigestAsHex(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8)));
        instancesInfoRepository.saveAll(Arrays.asList(instancesInfo, anotherInstancesInfo));

        ClassifierOptionsInfo classifierOptionsInfo1 = TestHelperUtils.buildClassifierOptionsInfo();
        classifierOptionsInfo1.setClassifierName("Classifier1");
        ClassifierOptionsInfo classifierOptionsInfo2 = TestHelperUtils.buildClassifierOptionsInfo();
        classifierOptionsInfo2.setClassifierName("Classifier2");
        ClassifierOptionsInfo classifierOptionsInfo3 = TestHelperUtils.buildClassifierOptionsInfo();
        classifierOptionsInfo3.setClassifierName("Classifier3");
        ClassifierOptionsInfo classifierOptionsInfo4 = TestHelperUtils.buildClassifierOptionsInfo();
        classifierOptionsInfo4.setClassifierName("Classifier4");
        ClassifierOptionsInfo classifierOptionsInfo5 = TestHelperUtils.buildClassifierOptionsInfo();
        classifierOptionsInfo5.setClassifierName("Classifier5");
        ClassifierOptionsInfo classifierOptionsInfo6 = TestHelperUtils.buildClassifierOptionsInfo();
        classifierOptionsInfo6.setClassifierName("Classifier6");
        ClassifierOptionsInfo classifierOptionsInfo7 = TestHelperUtils.buildClassifierOptionsInfo();
        classifierOptionsInfo7.setClassifierName("Classifier7");

        com.ers.model.EvaluationMethod modelEvaluationMethod = evaluationMethodMapper.map(evaluationMethod);
        EvaluationResultsInfo evaluationResultsInfo1 = TestHelperUtils.createEvaluationResultsInfo(instancesInfo,
                classifierOptionsInfo1, modelEvaluationMethod, BigDecimal.valueOf(67.73d),
                BigDecimal.valueOf(0.76d), BigDecimal.valueOf(0.07d));
        EvaluationResultsInfo evaluationResultsInfo2 = TestHelperUtils.createEvaluationResultsInfo(instancesInfo,
                classifierOptionsInfo2, modelEvaluationMethod, BigDecimal.valueOf(65.96d),
                BigDecimal.valueOf(0.72d), BigDecimal.valueOf(0.046d));
        EvaluationResultsInfo evaluationResultsInfo3 = TestHelperUtils.createEvaluationResultsInfo(instancesInfo,
                classifierOptionsInfo3, modelEvaluationMethod, BigDecimal.valueOf(61.08d),
                BigDecimal.valueOf(0.73d), BigDecimal.valueOf(0.006d));
        EvaluationResultsInfo evaluationResultsInfo4 = TestHelperUtils.createEvaluationResultsInfo(instancesInfo,
                classifierOptionsInfo4, modelEvaluationMethod, BigDecimal.valueOf(87.79d),
                BigDecimal.valueOf(0.71d), BigDecimal.valueOf(0.01d));
        EvaluationResultsInfo evaluationResultsInfo5 = TestHelperUtils.createEvaluationResultsInfo(instancesInfo,
                classifierOptionsInfo5, modelEvaluationMethod, BigDecimal.valueOf(87.79d),
                BigDecimal.valueOf(0.79d), BigDecimal.valueOf(0.04d));
        EvaluationResultsInfo evaluationResultsInfo6 = TestHelperUtils.createEvaluationResultsInfo(anotherInstancesInfo,
                classifierOptionsInfo6, modelEvaluationMethod, BigDecimal.valueOf(56.80d),
                BigDecimal.valueOf(0.88d), BigDecimal.valueOf(0.09d));
        EvaluationResultsInfo evaluationResultsInfo7 = TestHelperUtils.createEvaluationResultsInfo(instancesInfo,
                classifierOptionsInfo7, modelEvaluationMethod, BigDecimal.valueOf(87.79d),
                BigDecimal.valueOf(0.81d), BigDecimal.valueOf(0.03d));
        evaluationResultsInfoRepository.saveAll(
                Arrays.asList(evaluationResultsInfo1, evaluationResultsInfo2, evaluationResultsInfo3,
                        evaluationResultsInfo4, evaluationResultsInfo5, evaluationResultsInfo6,
                        evaluationResultsInfo7));

        List<ClassifierOptionsInfo> classifierOptionsInfoList =
                classifierOptionsService.findBestClassifierOptions(request);
        Assertions.assertThat(classifierOptionsInfoList).isNotEmpty();
        Assertions.assertThat(classifierOptionsInfoList.size()).isEqualTo(serviceConfig.getResultSize());
        Assertions.assertThat(classifierOptionsInfoList.get(0).getClassifierName()).isEqualTo
                (classifierOptionsInfo7.getClassifierName());
        Assertions.assertThat(classifierOptionsInfoList.get(1).getClassifierName()).isEqualTo
                (classifierOptionsInfo5.getClassifierName());
        Assertions.assertThat(classifierOptionsInfoList.get(2).getClassifierName()).isEqualTo
                (classifierOptionsInfo4.getClassifierName());
    }
}
