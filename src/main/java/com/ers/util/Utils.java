package com.ers.util;

import com.ers.dto.EvaluationResultsReport;
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
     * @param evaluationResultsReport - evaluation results report
     * @return {@code true} if request id is not empty
     */
    public static boolean hasRequestId(EvaluationResultsReport evaluationResultsReport) {
        return evaluationResultsReport != null && !StringUtils.isEmpty(evaluationResultsReport.getRequestId());
    }
}
