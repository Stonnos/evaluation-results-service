package com.ers.exception;

/**
 * Transform data exception.
 *
 * @author Roman Batygin
 */
public class TransformDataException extends EvaluationResultsException {

    /**
     * Creates transform data exception.
     *
     * @param message - error message
     */
    public TransformDataException(String message) {
        super(message);
    }
}
