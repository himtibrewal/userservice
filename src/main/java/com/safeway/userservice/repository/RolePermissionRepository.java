package com.safeway.userservice.repository;

import com.safeway.userservice.entity.UserRole;
import com.safeway.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UseRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findAllByUserId(Long userId);

    @Query("select r.roleName, r.roleCode from UserRole ur LEFT join Role r on ur.role.id = r.id  where ur.user =: userId")
    List<Role> getAllRoleByUser(@Param("user_id") Long userId);
}
