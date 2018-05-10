package com.ers.mapping;

import com.ers.dto.StatisticsReport;
import com.ers.model.StatisticsInfo;
import org.mapstruct.Mapper;

/**
 * Statistics report mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public interface StatisticsReportMapper {

    /**
     * Maps statistics report to statistics info entity.
     *
     * @param statisticsReport - statistics report
     * @return statistics info entity
     */
    StatisticsInfo map(StatisticsReport statisticsReport);
}
