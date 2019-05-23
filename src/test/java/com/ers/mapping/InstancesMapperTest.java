package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.InstancesReport;
import com.ers.model.InstancesInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link InstancesMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import(InstancesMapperImpl.class)
public class InstancesMapperTest {

    @Inject
    private InstancesMapper instancesMapper;

    @Test
    public void testMapInstancesReport() {
        InstancesReport report = TestHelperUtils.buildInstancesReport();
        InstancesInfo instancesInfo = instancesMapper.map(report);
        Assertions.assertThat(instancesInfo.getRelationName()).isEqualTo(report.getRelationName());
        Assertions.assertThat(instancesInfo.getNumClasses().intValue()).isEqualTo(report.getNumClasses().intValue());
        Assertions.assertThat(instancesInfo.getNumInstances().intValue()).isEqualTo(
                report.getNumInstances().intValue());
        Assertions.assertThat(instancesInfo.getNumAttributes().intValue()).isEqualTo(
                report.getNumAttributes().intValue());
    }

    @Test
    public void testMapInstancesInfo() {
        InstancesInfo instancesInfo = TestHelperUtils.buildInstancesInfo();
        InstancesReport instancesReport = instancesMapper.map(instancesInfo);
        Assertions.assertThat(instancesReport.getRelationName()).isEqualTo(instancesInfo.getRelationName());
        Assertions.assertThat(instancesReport.getNumClasses().intValue()).isEqualTo(
                instancesInfo.getNumClasses().intValue());
        Assertions.assertThat(instancesReport.getNumInstances().intValue()).isEqualTo(
                instancesInfo.getNumInstances().intValue());
        Assertions.assertThat(instancesReport.getNumAttributes().intValue()).isEqualTo(
                instancesInfo.getNumAttributes().intValue());
        Assertions.assertThat(instancesReport.getXmlInstances()).isNotNull();
    }
}
