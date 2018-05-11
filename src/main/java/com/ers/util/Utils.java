package com.ers.util;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import org.apache.commons.lang3.StringUtils;

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
}
