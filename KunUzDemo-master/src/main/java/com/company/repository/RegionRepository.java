package com.company.repository;

import com.company.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    Optional<RegionEntity> findByKey(String key);

    @Transactional
    @Modifying
    @Query("update RegionEntity set visible =: vis where id =: rId")
    void updateVisible(@Param("vis") Boolean visible, @Param("rId") Integer regionId);

    List<RegionEntity> findAllByVisible(Boolean visible);

    Optional<RegionEntity> findByIdAndVisible(Integer id, Boolean visible);
}
