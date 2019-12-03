package com.ers.util;

import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.ClassifierReport;
import com.ers.dto.EvaluationMethodReport;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Utility class.
 *
 * @author Roman Batygin
 */
@UtilityClass
public class Utils {

    private static final String UUID_REGEX =
            "^[0-9a-f]{8}-[0-9a-f]{4}-[34][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    /**
     * Creates evaluation results response.
     *
     * @param requestId      - request id
     * @param responseStatus - response status
     * @return evaluation results response
     */
    public static EvaluationResultsResponse buildResponse(String requestId, ResponseStatus responseStatus) {
        EvaluationResultsResponse evaluationResultsResponse = new EvaluationResultsResponse();
        evaluationResultsResponse.setRequestId(requestId);
        evaluationResultsResponse.setStatus(responseStatus);
        return evaluationResultsResponse;
    }

    /**
     * Checks existing of request id.
     *
     * @param evaluationResultsRequest - evaluation results request
     * @return {@code true} if request id is not empty
     */
    public static boolean hasValidRequestId(EvaluationResultsRequest evaluationResultsRequest) {
        return evaluationResultsRequest != null && isValidUuid(evaluationResultsRequest.getRequestId());
    }

    /**
     * Checks existing of request id.
     *
     * @param evaluationResultsRequest - evaluation results request
     * @return {@code true} if request id is not empty
     */
    public static boolean hasValidRequestId(GetEvaluationResultsRequest evaluationResultsRequest) {
        return evaluationResultsRequest != null && isValidUuid(evaluationResultsRequest.getRequestId());
    }

    /**
     * Check uuid format.
     *
     * @param uuid - uuid as string
     * @return {@code true} if uuid format is valid
     */
    public static boolean isValidUuid(String uuid) {
        return !StringUtils.isEmpty(uuid) && uuid.matches(UUID_REGEX);
    }

    public static void main(String[] arhs) {
        System.out.println(isValidUuid(UUID.randomUUID().toString()));
    }

    /**
     * Creates classifier options response.
     *
     * @param requestId      - request id
     * @param responseStatus - response status
     * @return classifier options response
     */
    public static ClassifierOptionsResponse buildClassifierOptionsResponse(String requestId,
                                                                           ResponseStatus responseStatus) {
        ClassifierOptionsResponse classifierOptionsResponse = new ClassifierOptionsResponse();
        classifierOptionsResponse.setRequestId(requestId);
        classifierOptionsResponse.setStatus(responseStatus);
        return classifierOptionsResponse;
    }

    /**
     * Creates evaluation results response.
     *
     * @param requestId      - request id
     * @param responseStatus - response status
     * @return evaluation results simple response
     */
    public static GetEvaluationResultsResponse buildEvaluationResultsResponse(String requestId,
                                                                              ResponseStatus responseStatus) {
        GetEvaluationResultsResponse evaluationResultsResponse = new GetEvaluationResultsResponse();
        evaluationResultsResponse.setRequestId(requestId);
        evaluationResultsResponse.setStatus(responseStatus);
        return evaluationResultsResponse;
    }

    /**
     * Creates classifier options response.
     *
     * @param requestId         - request id
     * @param classifierReports - classifier reports
     * @param responseStatus    - response status
     * @return classifier options response
     */
    public static ClassifierOptionsResponse buildClassifierOptionsResponse(String requestId,
                                                                           List<ClassifierReport> classifierReports,
                                                                           ResponseStatus responseStatus) {
        ClassifierOptionsResponse classifierOptionsResponse = buildClassifierOptionsResponse(requestId, responseStatus);
        classifierReports.forEach(
                classifierReport -> classifierOptionsResponse.getClassifierReports().add(classifierReport));
        return classifierOptionsResponse;
    }

    /**
     * Validates classifier options request. Checks for not empty xml instances and not null evaluation method.
     *
     * @param request - classifier options request
     * @return {@code true} if classifier options request is valid
     */
    public static boolean validateClassifierOptionsRequest(ClassifierOptionsRequest request) {
        return Optional.ofNullable(request).map(ClassifierOptionsRequest::getInstances).isPresent() &&
                !StringUtils.isEmpty(request.getInstances().getXmlInstances()) &&
                Optional.ofNullable(request.getEvaluationMethodReport()).map(
                        EvaluationMethodReport::getEvaluationMethod).isPresent();
    }

    /**
     * Validates evaluation results request. Checks for not empty xml instances and not null evaluation method.
     *
     * @param request - classifier options request
     * @return {@code true} if evaluation results request is valid
     */
    public static boolean validateEvaluationResultsRequest(EvaluationResultsRequest request) {
        return Optional.ofNullable(request).map(EvaluationResultsRequest::getInstances).isPresent() &&
                !StringUtils.isEmpty(request.getInstances().getXmlInstances()) &&
                Optional.ofNullable(request.getEvaluationMethodReport()).map(
                        EvaluationMethodReport::getEvaluationMethod).isPresent();
    }

    /**
     * Returns integer value if specified big integer value is not null, null otherwise.
     *
     * @param value - big integer value
     * @return integer value
     */
    public static Integer toInteger(BigInteger value) {
        return value != null ? value.intValue() : null;
    }
}
