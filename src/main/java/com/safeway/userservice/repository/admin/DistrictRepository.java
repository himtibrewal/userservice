package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.admin.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findAllByStateId(Long stateId);
}
