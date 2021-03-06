package com.ers.mapping;

import com.ers.dto.InstancesReport;
import com.ers.model.InstancesInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Instances report mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public abstract class InstancesMapper {

    /**
     * Maps instances report to instances info entity.
     *
     * @param instancesReport - instances report entity
     * @return instances info entity
     */
     public abstract InstancesInfo map(InstancesReport instancesReport);

    /**
     * Maps instances info entity to dto model.
     *
     * @param instancesInfo - instances info entity
     * @return instances report
     */
    public abstract InstancesReport map(InstancesInfo instancesInfo);

    @AfterMapping
    protected void mapXmlData(InstancesInfo instancesInfo, @MappingTarget InstancesReport instancesReport) {
        instancesReport.setXmlInstances(Optional.ofNullable(instancesInfo.getXmlData()).map(
                data -> new String(data, StandardCharsets.UTF_8)).orElse(null));
    }
}
