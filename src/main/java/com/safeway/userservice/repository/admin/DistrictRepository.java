package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.admin.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query("select d from District d WHERE d.state.id = ?1")
    List<District> findAllByStateId(Long stateId);

    @Query("select d from District d WHERE d.state.id = ?1")
    Page<District> findAllByStateId(Long stateId, Pageable pageable);

}
