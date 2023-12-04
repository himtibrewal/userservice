package com.safeway.userservice.repository;

import com.safeway.userservice.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UseRolesRepository extends JpaRepository<UserRoles, Long> {

    List<UserRoles> findAllByUserId(Long aLong);
}
