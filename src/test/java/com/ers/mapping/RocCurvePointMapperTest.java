package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.RocCurvePoint;
import com.ers.model.RocCurvePointEntity;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link RocCurvePointMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import(RocCurvePointMapperImpl.class)
public class RocCurvePointMapperTest {

    @Inject
    private RocCurvePointMapper rocCurvePointMapper;

    @Test
    public void testRocCurvePointMap() {
        RocCurvePoint rocCurvePoint = TestHelperUtils.buildRocCurvePoint();
        RocCurvePointEntity rocCurvePointEntity = rocCurvePointMapper.map(rocCurvePoint);
        Assertions.assertThat(rocCurvePointEntity).isNotNull();
        Assertions.assertThat(rocCurvePointEntity.getXValue()).isEqualTo(rocCurvePoint.getXValue());
        Assertions.assertThat(rocCurvePointEntity.getYValue()).isEqualTo(rocCurvePoint.getYValue());
        Assertions.assertThat(rocCurvePointEntity.getThresholdValue()).isEqualTo(rocCurvePoint.getThresholdValue());
    }

    @Test
    public void testRocCurvePointEntityMap() {
        RocCurvePointEntity rocCurvePointEntity = TestHelperUtils.buildRocCurvePointEntity();
        RocCurvePoint rocCurvePoint = rocCurvePointMapper.map(rocCurvePointEntity);
        Assertions.assertThat(rocCurvePoint).isNotNull();
        Assertions.assertThat(rocCurvePoint.getXValue()).isEqualTo(rocCurvePointEntity.getXValue());
        Assertions.assertThat(rocCurvePoint.getYValue()).isEqualTo(rocCurvePointEntity.getYValue());
        Assertions.assertThat(rocCurvePoint.getThresholdValue()).isEqualTo(rocCurvePointEntity.getThresholdValue());
    }
}
