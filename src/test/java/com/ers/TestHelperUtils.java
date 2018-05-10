package com.ers;

import com.ers.dto.ClassificationCostsReport;
import com.ers.dto.ClassifierReport;
import com.ers.dto.ConfusionMatrixReport;
import com.ers.dto.EvaluationMethod;
import com.ers.dto.EvaluationMethodReport;
import com.ers.dto.EvaluationResultsReport;
import com.ers.dto.InstancesReport;
import com.ers.dto.StatisticsReport;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Tests utility class.
 *
 * @author Roman Batygin
 */
public class TestHelperUtils {

    private static final int OPTIONS_SIZE = 5;
    private static final int RANDOM_STRING_SIZE = 5;
    private static final String CHARS = "ABCDEFG123456";

    /**
     * Creates evaluation results report.
     *
     * @param requestId - request id
     * @return evaluation results report
     */
    public static EvaluationResultsReport buildEvaluationResultsReport(String requestId) {
        EvaluationResultsReport report = new EvaluationResultsReport();
        report.setRequestId(requestId);
        report.setInstances(buildInstancesReport());
        report.setEvaluationMethodReport(buildEvaluationMethodReport());
        report.setClassifierReport(buildClassifierReport());
        report.setStatistics(buildStatisticsReport());
        for (int i = 0; i < OPTIONS_SIZE; i++) {
            report.getConfusionMatrix().add(buildConfusionMatrix());
            report.getClassificationCosts().add(buildClassificationCostsReport());
        }
        return report;
    }

    /**
     * Creates instances report.
     *
     * @return instances report
     */
    public static InstancesReport buildInstancesReport() {
        InstancesReport instancesReport = new InstancesReport();
        instancesReport.setXmlData(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        instancesReport.setRelationName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        instancesReport.setNumInstances(BigInteger.TEN);
        instancesReport.setNumAttributes(BigInteger.TEN);
        instancesReport.setNumClasses(BigInteger.TEN);
        instancesReport.setClassName(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        return instancesReport;
    }

    /**
     * Creates confusion matrix report.
     *
     * @return confusion matrix report
     */
    public static ConfusionMatrixReport buildConfusionMatrix() {
        ConfusionMatrixReport confusionMatrix = new ConfusionMatrixReport();
        confusionMatrix.setActualClass(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        confusionMatrix.setPredictedClass(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
        confusionMatrix.setNumInstances(BigInteger.TEN);
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
        classificationCostsReport.setAucValue(BigDecimal.valueOf(Math.random()));
        return classificationCostsReport;
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
        statisticsReport.setNumCorrect(BigInteger.ZERO);
        statisticsReport.setNumIncorrect(BigInteger.TEN);
        statisticsReport.setMeanAbsoluteError(BigDecimal.valueOf(Math.random()));
        statisticsReport.setRootMeanSquaredError(BigDecimal.valueOf(Math.random()));
        statisticsReport.setVarianceError(BigDecimal.valueOf(Math.random()));
        statisticsReport.setConfidenceIntervalLowerBound(BigDecimal.valueOf(Math.random()));
        statisticsReport.setConfidenceIntervalUpperBound(BigDecimal.valueOf(Math.random()));
        return statisticsReport;
    }

    /**
     * Creates evaluation method report.
     *
     * @return evaluation method report
     */
    public static EvaluationMethodReport buildEvaluationMethodReport() {
        EvaluationMethodReport evaluationMethodReport = new EvaluationMethodReport();
        evaluationMethodReport.setEvaluationMethod(EvaluationMethod.CROSS_VALIDATION);
        evaluationMethodReport.setNumFolds(BigInteger.TEN);
        evaluationMethodReport.setNumTests(BigInteger.TEN);
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
        classifierReport.setInputOptionsMap(new ClassifierReport.InputOptionsMap());
        for (int i = 0; i < OPTIONS_SIZE; i++) {
            ClassifierReport.InputOptionsMap.Entry entry = new ClassifierReport.InputOptionsMap.Entry();
            entry.setKey(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
            entry.setValue(RandomStringUtils.random(RANDOM_STRING_SIZE, CHARS));
            classifierReport.getInputOptionsMap().getEntry().add(entry);
        }
        return classifierReport;
    }
}
