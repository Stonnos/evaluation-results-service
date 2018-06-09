package com.ers.util;

import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.ClassifierReport;
import com.ers.dto.EvaluationMethodReport;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
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
public class Utils {

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
    public static boolean hasRequestId(EvaluationResultsRequest evaluationResultsRequest) {
        return evaluationResultsRequest != null && !StringUtils.isEmpty(evaluationResultsRequest.getRequestId());
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
     * Validates classifier options request.
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
     * Returns integer value if specified big integer value is not null, null otherwise.
     *
     * @param value - big integer value
     * @return integer value
     */
    public static Integer toInteger(BigInteger value) {
        return value != null ? value.intValue() : null;
    }
}
