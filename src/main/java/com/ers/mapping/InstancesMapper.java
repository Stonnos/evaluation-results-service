package com.ers.mapping;

import com.ers.dto.InstancesReport;
import com.ers.model.InstancesInfo;
import org.mapstruct.Mapper;

/**
 * Instances report mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public interface InstancesMapper {

    /**
     * Maps instances report to instances info entity.
     *
     * @param instancesReport - instances report entity
     * @return instances info entity
     */
    InstancesInfo map(InstancesReport instancesReport);
}
