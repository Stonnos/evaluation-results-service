package com.ers.model;

/**
 * Evaluation options enum.
 *
 * @author Roman Batygin
 */
public enum EvaluationOption {

    /**
     * Folds number for k * V - folds cross validation method
     */
    NUM_FOLDS,

    /**
     * Tests number for k * V - folds cross validation method
     */
    NUM_TESTS,

    /**
     * Seed value for random generator
     */
    SEED
}
