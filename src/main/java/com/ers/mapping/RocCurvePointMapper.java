package com.ers.mapping;

import com.ers.dto.RocCurvePoint;
import com.ers.model.RocCurvePointEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Roc - curve point mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public interface RocCurvePointMapper {

    /**
     * Maps roc - curve point dto to its entity model.
     *
     * @param rocCurvePoint - roc - curve point dto
     * @return roc - curve point entity
     */
    RocCurvePointEntity map(RocCurvePoint rocCurvePoint);

    /**
     * Maps roc - curve points dto list to its entities list.
     *
     * @param rocCurvePoints - roc curve points list
     * @return roc curve points entites list
     */
    List<RocCurvePointEntity> map(List<RocCurvePoint> rocCurvePoints);
}
