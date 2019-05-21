package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.RocCurveData;
import com.ers.model.RocCurveDataEntity;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link RocCurveDataMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import({RocCurveDataMapperImpl.class, RocCurvePointMapperImpl.class})
public class RocCurveDataMapperTest {

    @Inject
    private RocCurveDataMapper rocCurveDataMapper;

    @Test
    public void testRocCurveDataMap() {
        RocCurveData rocCurveData = TestHelperUtils.buildRocCurveData();
        RocCurveDataEntity rocCurveDataEntity = rocCurveDataMapper.map(rocCurveData);
        Assertions.assertThat(rocCurveDataEntity).isNotNull();
        Assertions.assertThat(rocCurveDataEntity.getClassValue()).isEqualTo(rocCurveData.getClassValue());
        Assertions.assertThat(rocCurveDataEntity.getPoints()).hasSameSizeAs(rocCurveData.getPoints());
    }
}
