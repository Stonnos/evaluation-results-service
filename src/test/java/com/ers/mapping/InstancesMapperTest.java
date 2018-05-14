package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.InstancesReport;
import com.ers.model.InstancesInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link InstancesMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InstancesMapperTest {

    @Inject
    private InstancesMapper instancesMapper;

    @Test
    public void testMapInstancesReport() throws Exception {
        InstancesReport report = TestHelperUtils.buildInstancesReport();
        InstancesInfo instancesInfo = instancesMapper.map(report);
        Assertions.assertThat(instancesInfo.getRelationName()).isEqualTo(report.getRelationName());
        Assertions.assertThat(instancesInfo.getNumClasses().intValue()).isEqualTo(report.getNumClasses().intValue());
        Assertions.assertThat(instancesInfo.getNumInstances().intValue()).isEqualTo(
                report.getNumInstances().intValue());
        Assertions.assertThat(instancesInfo.getNumAttributes().intValue()).isEqualTo(
                report.getNumAttributes().intValue());
    }
}
