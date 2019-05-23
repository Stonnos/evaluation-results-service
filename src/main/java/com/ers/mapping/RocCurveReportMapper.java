package com.ers.mapping;

import com.ers.dto.RocCurveReport;
import com.ers.model.RocCurveInfo;
import org.mapstruct.Mapper;

/**
 * Roc - curve report mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public interface RocCurveReportMapper {

    /**
     * Maps roc - curve report to roc - curve info model.
     *
     * @param rocCurveReport - roc - curve report
     * @return roc - curve info
     */
    RocCurveInfo map(RocCurveReport rocCurveReport);

    /**
     * Maps roc - curve info to its dto model.
     *
     * @param rocCurveInfo - roc - curve info
     * @return roc - curve dto
     */
    RocCurveReport map(RocCurveInfo rocCurveInfo);
}
