package com.ers.mapping;

import com.ers.dto.RocCurveData;
import com.ers.model.RocCurveDataEntity;
import org.mapstruct.Mapper;

import java.util.List;

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
    List<RocCurveDataEntity> map(List<RocCurveData> rocCurveDataList);
}
