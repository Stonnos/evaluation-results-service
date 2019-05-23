package com.ers.mapping;

import com.ers.dto.RocCurveData;
import com.ers.model.RocCurveDataEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

/**
 * Roc - curve data mapper.
 *
 * @author Roman Batygin
 */
@Mapper(uses = RocCurvePointMapper.class)
public interface RocCurveDataMapper {

    /**
     * Maps roc - curve data dto to its entity model.
     *
     * @param rocCurveData - roc - curve data dto
     * @return roc - curve data entity
     */
    RocCurveDataEntity map(RocCurveData rocCurveData);

    /**
     * Maps roc - curve data list dto to its entities list.
     *
     * @param rocCurveDataList - roc curve data list
     * @return roc curve data entities list
     */
    Set<RocCurveDataEntity> map(List<RocCurveData> rocCurveDataList);

    /**
     * Maps roc - curve entity to its dto model.
     *
     * @param rocCurveDataEntity - roc - curve data entity
     * @return roc - curve data dto
     */
    RocCurveData map(RocCurveDataEntity rocCurveDataEntity);
}
