package com.company.repository;

import com.company.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    Optional<TagEntity> findByKey(String key);


    @Transactional
    @Modifying
    @Query("update TagEntity set visible =: bool where id =: aId")
    void updateVisible(@Param("bool") Boolean bool, @Param("aId") Integer aId);

    List<TagEntity> findAllByVisible(boolean b);

    Optional<TagEntity> findByIdAndVisible(Integer regionId, boolean b);

}
