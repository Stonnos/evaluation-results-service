package com.ers;

import com.ers.dto.ClassificationCostsReport;
import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierReport;
import com.ers.dto.ConfusionMatrixReport;
import com.ers.dto.EnsembleClassifierReport;
import com.ers.dto.EvaluationMethod;
import com.ers.dto.EvaluationMethodReport;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.InputOptionsMap;
import com.ers.dto.InstancesReport;
import com.ers.dto.RocCurveData;
import com.ers.dto.RocCurvePoint;
import com.ers.dto.RocCurveReport;
import com.ers.dto.StatisticsReport;
import com.ers.model.ClassificationCostsInfo;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.model.ConfusionMatrix;
import com.ers.model.EvaluationResultsInfo;
import com.ers.model.InstancesInfo;
import com.ers.model.RocCurveDataEntity;
import com.ers.model.RocCurveInfo;
import com.ers.model.RocCurvePointEntity;
import com.ers.model.StatisticsInfo;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Tests utility class.
 *
 * @author Roman Batygin
 */
public class TestHelperUtils {

    private static final int OPTIONS_SIZE = 5;
    private static final int RANDOM_STRING_SIZE = 5;
    private static final String CHARS = "ABCDEFG123456";
    private static final int NUM_FOLDS = 10;
    private static final int NUM_TESTS = 1;
    private static final int SEED = 1;
    private static final String CLASS_VALUE = "Class";
    private static final String XML_DATA = "xmlData";

    /**
     * Creates evaluation results report.
     *
     * @param requestId - request id
     * @return evaluation results report
     */
    public static EvaluationResultsRequest buildEvaluationResultsReport(String requestId) {
        EvaluationResultsRequest resultsRequest = new EvaluationResultsRequest();
        resultsRequest.setRequestId(requestId);
        resultsRequest.setInstances(buildInstancesReport());
        resultsRequest.setEvaluationMethodReport(buildEvaluationMethodReport(EvaluationMethod.CROSS_VALIDATION));
        resultsRequest.setClassifierReport(buildClassifierReport());
        resultsRequest.setStatistics(buildStatisticsReport());
        for (int i = 0; i < OPTIONS_SIZE; i++) {
            resultsRequest.getConfusionMatrix().add(buildConfusionMatrixReport());
            resultsRequest.getClassificationCosts().add(buildClassificationCostsReport());
            resultsRequest.getRocCurveData().add(buildRocCurveData());
        }
        return resultsRequest;
    }

    /**
     * Creates instances report.
     *
     * @return instances report
     */
    public static InstancesReport buildInstancesReport() {
        InstancesReport instancesReport = new InstancesReport();
        instancesReport.setXmlInstances(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        instancesReport.setRelationName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        instancesReport.setNumInstances(BigInteger.TEN);
        instancesReport.setNumAttributes(BigInteger.TEN);
        instancesReport.setNumClasses(BigInteger.TEN);
        instancesReport.setClassName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        return instancesReport;
    }

    /**
     * Creates instances info.
     *
     * @return instances info
     */
    public static InstancesInfo buildInstancesInfo() {
        InstancesInfo instancesInfo = new InstancesInfo();
        instancesInfo.setXmlData(XML_DATA.getBytes(Charsets.UTF_8));
        instancesInfo.setRelationName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        instancesInfo.setNumInstances(BigInteger.TEN.intValue());
        instancesInfo.setNumAttributes(BigInteger.TEN.intValue());
        instancesInfo.setNumClasses(BigInteger.TEN.intValue());
        instancesInfo.setClassName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        return instancesInfo;
    }

    /**
     * Creates confusion matrix report.
     *
     * @return confusion matrix report
     */
    public static ConfusionMatrixReport buildConfusionMatrixReport() {
        ConfusionMatrixReport confusionMatrix = new ConfusionMatrixReport();
        confusionMatrix.setActualClass(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        confusionMatrix.setPredictedClass(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        confusionMatrix.setNumInstances(BigInteger.TEN);
        return confusionMatrix;
    }

    /**
     * Creates confusion matrix.
     *
     * @return confusion matrix
     */
    public static ConfusionMatrix buildConfusionMatrix() {
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        confusionMatrix.setActualClass(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        confusionMatrix.setPredictedClass(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        confusionMatrix.setNumInstances(BigInteger.TEN.intValue());
        return confusionMatrix;
    }

    /**
     * Creates classification costs report.
     *
     * @return classification costs report
     */
    public static ClassificationCostsReport buildClassificationCostsReport() {
        ClassificationCostsReport classificationCostsReport = new ClassificationCostsReport();
        classificationCostsReport.setClassValue(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classificationCostsReport.setFalseNegativeRate(BigDecimal.valueOf(Math.random()));
        classificationCostsReport.setTrueNegativeRate(BigDecimal.valueOf(Math.random()));
        classificationCostsReport.setTruePositiveRate(BigDecimal.valueOf(Math.random()));
        classificationCostsReport.setFalsePositiveRate(BigDecimal.valueOf(Math.random()));
        classificationCostsReport.setRocCurve(buildRocCurveReport());
        return classificationCostsReport;
    }

    /**
     * Creates classification costs info.
     *
     * @return classification costs info
     */
    public static ClassificationCostsInfo buildClassificationCostsInfo() {
        ClassificationCostsInfo classificationCostsInfo = new ClassificationCostsInfo();
        classificationCostsInfo.setClassValue(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classificationCostsInfo.setFalseNegativeRate(BigDecimal.valueOf(Math.random()));
        classificationCostsInfo.setTrueNegativeRate(BigDecimal.valueOf(Math.random()));
        classificationCostsInfo.setTruePositiveRate(BigDecimal.valueOf(Math.random()));
        classificationCostsInfo.setFalsePositiveRate(BigDecimal.valueOf(Math.random()));
        classificationCostsInfo.setRocCurveInfo(buildRocCurveInfo());
        return classificationCostsInfo;
    }

    /**
     * Creates roc curve data.
     *
     * @return roc curve data
     */
    public static RocCurveData buildRocCurveData() {
        RocCurveData rocCurveData = new RocCurveData();
        rocCurveData.setClassValue(CLASS_VALUE);
        for (int i = 0; i < OPTIONS_SIZE; i++) {
            rocCurveData.getPoints().add(buildRocCurvePoint());
        }
        return rocCurveData;
    }

    /**
     * Creates roc curve data entity.
     *
     * @return roc curve data entity
     */
    public static RocCurveDataEntity buildRocCurveDataEntity() {
        RocCurveDataEntity rocCurveDataEntity = new RocCurveDataEntity();
        rocCurveDataEntity.setClassValue(CLASS_VALUE);
        rocCurveDataEntity.setPoints(newHashSet());
        for (int i = 0; i < OPTIONS_SIZE; i++) {
            rocCurveDataEntity.getPoints().add(buildRocCurvePointEntity());
        }
        return rocCurveDataEntity;
    }

    /**
     * Creates roc curve point.
     *
     * @return roc curve point
     */
    public static RocCurvePoint buildRocCurvePoint() {
        RocCurvePoint rocCurvePoint = new RocCurvePoint();
        rocCurvePoint.setXValue(BigDecimal.ONE);
        rocCurvePoint.setYValue(BigDecimal.ONE);
        rocCurvePoint.setThresholdValue(BigDecimal.ONE);
        return rocCurvePoint;
    }

    /**
     * Creates roc curve point entity.
     *
     * @return roc curve point entity
     */
    public static RocCurvePointEntity buildRocCurvePointEntity() {
        RocCurvePointEntity rocCurvePointEntity = new RocCurvePointEntity();
        rocCurvePointEntity.setXValue(BigDecimal.ONE);
        rocCurvePointEntity.setYValue(BigDecimal.ONE);
        rocCurvePointEntity.setThresholdValue(BigDecimal.ONE);
        return rocCurvePointEntity;
    }

    /**
     * Creates roc - curve report.
     *
     * @return roc - curve report
     */
    public static RocCurveReport buildRocCurveReport() {
        RocCurveReport rocCurveReport = new RocCurveReport();
        rocCurveReport.setAucValue(BigDecimal.valueOf(Math.random()));
        rocCurveReport.setSpecificity(BigDecimal.valueOf(Math.random()));
        rocCurveReport.setSensitivity(BigDecimal.valueOf(Math.random()));
        rocCurveReport.setThresholdValue(BigDecimal.valueOf(Math.random()));
        return rocCurveReport;
    }

    /**
     * Creates roc - curve info.
     *
     * @return roc - curve info
     */
    public static RocCurveInfo buildRocCurveInfo() {
        RocCurveInfo rocCurveInfo = new RocCurveInfo();
        rocCurveInfo.setAucValue(BigDecimal.valueOf(Math.random()));
        rocCurveInfo.setSpecificity(BigDecimal.valueOf(Math.random()));
        rocCurveInfo.setSensitivity(BigDecimal.valueOf(Math.random()));
        rocCurveInfo.setThresholdValue(BigDecimal.valueOf(Math.random()));
        return rocCurveInfo;
    }

    /**
     * Creates statistics report.
     *
     * @return statistics report
     */
    public static StatisticsReport buildStatisticsReport() {
        StatisticsReport statisticsReport = new StatisticsReport();
        statisticsReport.setPctCorrect(BigDecimal.valueOf(Math.random()));
        statisticsReport.setPctIncorrect(BigDecimal.valueOf(Math.random()));
        statisticsReport.setNumTestInstances(BigInteger.TEN);
        statisticsReport.setNumCorrect(BigInteger.ZERO);
        statisticsReport.setNumIncorrect(BigInteger.TEN);
        statisticsReport.setMeanAbsoluteError(BigDecimal.valueOf(Math.random()));
        statisticsReport.setRootMeanSquaredError(BigDecimal.valueOf(Math.random()));
        statisticsReport.setMaxAucValue(BigDecimal.valueOf(Math.random()));
        statisticsReport.setVarianceError(BigDecimal.valueOf(Math.random()));
        statisticsReport.setConfidenceIntervalLowerBound(BigDecimal.valueOf(Math.random()));
        statisticsReport.setConfidenceIntervalUpperBound(BigDecimal.valueOf(Math.random()));
        return statisticsReport;
    }

    /**
     * Creates statistics info.
     *
     * @return statistics info
     */
    public static StatisticsInfo buildStatisticsInfo() {
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setPctCorrect(BigDecimal.valueOf(Math.random()));
        statisticsInfo.setPctIncorrect(BigDecimal.valueOf(Math.random()));
        statisticsInfo.setNumTestInstances(BigInteger.TEN.intValue());
        statisticsInfo.setNumCorrect(BigInteger.ZERO.intValue());
        statisticsInfo.setNumIncorrect(BigInteger.TEN.intValue());
        statisticsInfo.setMeanAbsoluteError(BigDecimal.valueOf(Math.random()));
        statisticsInfo.setRootMeanSquaredError(BigDecimal.valueOf(Math.random()));
        statisticsInfo.setMaxAucValue(BigDecimal.valueOf(Math.random()));
        statisticsInfo.setVarianceError(BigDecimal.valueOf(Math.random()));
        statisticsInfo.setConfidenceIntervalLowerBound(BigDecimal.valueOf(Math.random()));
        statisticsInfo.setConfidenceIntervalUpperBound(BigDecimal.valueOf(Math.random()));
        return statisticsInfo;
    }

    /**
     * Creates evaluation method report.
     *
     * @param evaluationMethod - evaluation method
     * @return evaluation method report
     */
    public static EvaluationMethodReport buildEvaluationMethodReport(EvaluationMethod evaluationMethod) {
        EvaluationMethodReport evaluationMethodReport = new EvaluationMethodReport();
        evaluationMethodReport.setEvaluationMethod(evaluationMethod);
        evaluationMethodReport.setNumFolds(BigInteger.valueOf(NUM_FOLDS));
        evaluationMethodReport.setNumTests(BigInteger.valueOf(NUM_TESTS));
        evaluationMethodReport.setSeed(BigInteger.valueOf(SEED));
        return evaluationMethodReport;
    }

    /**
     * Creates classifier report.
     *
     * @return classifier report
     */
    public static ClassifierReport buildClassifierReport() {
        ClassifierReport classifierReport = new ClassifierReport();
        classifierReport.setClassifierName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classifierReport.setClassifierDescription(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classifierReport.setOptions(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classifierReport.setInputOptionsMap(new InputOptionsMap());
        populateInputOptionsMap(classifierReport.getInputOptionsMap());
        return classifierReport;
    }

    /**
     * Creates ensemble classifier report.
     *
     * @return ensemble classifier report
     */
    public static EnsembleClassifierReport buildEnsembleClassifierReport() {
        EnsembleClassifierReport classifierReport = new EnsembleClassifierReport();
        classifierReport.setClassifierName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classifierReport.setInputOptionsMap(new InputOptionsMap());
        populateInputOptionsMap(classifierReport.getInputOptionsMap());
        for (int i = 0; i < OPTIONS_SIZE; i++) {
            classifierReport.getIndividualClassifiers().add(buildClassifierReport());
        }
        return classifierReport;
    }

    /**
     * Creates classifier options info list.
     *
     * @param classifierOptionsInfoList - classifiers options info list
     * @return classifier options info list
     */
    public static ClassifierOptionsInfo buildClassifierOptionsInfo(Map<String, String> inputOptionsMap,
                                                                   List<ClassifierOptionsInfo> classifierOptionsInfoList) {
        ClassifierOptionsInfo classifierOptionsInfo = new ClassifierOptionsInfo();
        classifierOptionsInfo.setClassifierName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classifierOptionsInfo.setClassifierDescription(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        classifierOptionsInfo.setInputOptionsMap(inputOptionsMap);
        classifierOptionsInfo.setIndividualClassifiers(classifierOptionsInfoList);
        return classifierOptionsInfo;
    }

    /**
     * Creates classifier options info list.
     *
     * @return classifier options info list
     */
    public static ClassifierOptionsInfo buildClassifierOptionsInfo() {
        return buildClassifierOptionsInfo(Collections.emptyMap(), Collections.emptyList());
    }

    /**
     * Creates classifier options request.
     *
     * @param evaluationMethod - evaluation method
     * @return classifier options request
     */
    public static ClassifierOptionsRequest createClassifierOptionsRequest(EvaluationMethod evaluationMethod) {
        ClassifierOptionsRequest request = new ClassifierOptionsRequest();
        request.setInstances(buildInstancesReport());
        request.setEvaluationMethodReport(buildEvaluationMethodReport(evaluationMethod));
        return request;
    }

    /**
     * Creates evaluation results info.
     *
     * @param instancesInfo         - instances info
     * @param classifierOptionsInfo - classifier options info
     * @param evaluationMethod      - evaluation method
     * @param pctCorrect            - pct correct
     * @param maxAucValue           - max AUC value
     * @param varianceError         - variance error
     * @return evaluation results info
     */
    public static EvaluationResultsInfo createEvaluationResultsInfo(InstancesInfo instancesInfo,
                                                                    ClassifierOptionsInfo classifierOptionsInfo,
                                                                    com.ers.model.EvaluationMethod evaluationMethod,
                                                                    BigDecimal pctCorrect,
                                                                    BigDecimal maxAucValue,
                                                                    BigDecimal varianceError) {
        EvaluationResultsInfo evaluationResultsInfo = new EvaluationResultsInfo();
        evaluationResultsInfo.setInstances(instancesInfo);
        evaluationResultsInfo.setClassifierOptionsInfo(classifierOptionsInfo);
        evaluationResultsInfo.setNumFolds(NUM_FOLDS);
        evaluationResultsInfo.setNumTests(NUM_TESTS);
        evaluationResultsInfo.setSeed(SEED);
        evaluationResultsInfo.setEvaluationMethod(evaluationMethod);
        evaluationResultsInfo.setStatistics(new StatisticsInfo());
        evaluationResultsInfo.getStatistics().setPctCorrect(pctCorrect);
        evaluationResultsInfo.getStatistics().setMaxAucValue(maxAucValue);
        evaluationResultsInfo.getStatistics().setVarianceError(varianceError);
        return evaluationResultsInfo;
    }

    /**
     * Creates evaluation results get request.
     *
     * @param requestId - request id
     * @return evaluation results get request
     */
    public static GetEvaluationResultsRequest buildGetEvaluationResultsRequest(String requestId) {
        GetEvaluationResultsRequest request = new GetEvaluationResultsRequest();
        request.setRequestId(requestId);
        return request;
    }

    private static void populateInputOptionsMap(InputOptionsMap inputOptionsMap) {
        for (int i = 0; i < OPTIONS_SIZE; i++) {
            InputOptionsMap.Entry entry = new InputOptionsMap.Entry();
            entry.setKey(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
            entry.setValue(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
            inputOptionsMap.getEntry().add(entry);
        }
    }
}
