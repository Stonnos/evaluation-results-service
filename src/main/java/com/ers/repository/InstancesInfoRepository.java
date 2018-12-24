package com.ers.repository;

import com.ers.model.InstancesInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to manage with {@link InstancesInfo} persistence entity.
 *
 * @author Roman Batygin
 */
public interface InstancesInfoRepository extends JpaRepository<InstancesInfo, Long> {

    /**
     * Finds instances info by MD5 hash.
     *
     * @param dataMd5Hash - data MD5 hash
     * @return instances info
     */
    InstancesInfo findByDataMd5Hash(String dataMd5Hash);

    List<InstancesInfo> findByDataPathIsNotNull();
}
