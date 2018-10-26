package com.ers.mapping;

import com.ers.dto.EvaluationMethod;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link EvaluationMethodMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import(EvaluationMethodMapperImpl.class)
public class EvaluationMethodMapperTest {

    @Inject
    private EvaluationMethodMapper evaluationMethodMapper;

    @Test
    public void testMapCrossValidationMethod() {
        Assertions.assertThat(evaluationMethodMapper.map(EvaluationMethod.CROSS_VALIDATION)).isEqualTo(
                com.ers.model.EvaluationMethod.CROSS_VALIDATION);
    }

    @Test
    public void testMapTrainingDataMethod() {
        Assertions.assertThat(evaluationMethodMapper.map(EvaluationMethod.TRAINING_DATA)).isEqualTo(
                com.ers.model.EvaluationMethod.TRAINING_DATA);
    }
}
