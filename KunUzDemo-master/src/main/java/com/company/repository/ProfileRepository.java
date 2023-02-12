package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    ProfileEntity findByEmail(String email);

    Optional<ProfileEntity> findByIdAndVisible(Integer id, Boolean visible);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible =: bool where id =: pId")
    void updateVisible(@Param("bool") Boolean bool, @Param("pId") Integer pId) ;

    Page<ProfileEntity> findByVisible(Boolean visible, Pageable pageable);

    ProfileEntity findByEmailAndPassword(String email, String password);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status =: active where id =: userId")
    void updateStatus(@Param("active") ProfileStatus active, @Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set attach.id =: attachId where id =: userId")
    void updateAttachId(@Param("active") String attachId, @Param("userId") Integer userId);


}
