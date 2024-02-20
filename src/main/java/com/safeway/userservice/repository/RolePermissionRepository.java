package com.safeway.userservice.repository;

import com.safeway.userservice.entity.Permission;
import com.safeway.userservice.entity.Role;
import com.safeway.userservice.entity.RolePermission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Set;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    @Query("select rp.permission from RolePermission rp where rp.role.id IN (?1)")
    List<Permission> findAllPermissionByRoleIsIn(Set<Long> roleIds);

    @Query("select rp.permission.id from RolePermission rp where rp.role.id = ?1")
    Set<Long> findAllPermissionIdByRoleId(Long roleId);

    @Query("select rp.role.id from RolePermission rp where rp.permission.id = ?1")
    Set<Long> findAllRoleIdByPermissionId(Long permissionId);

    @Query("select rp.permission from RolePermission rp where rp.role.id = ?1")
    List<Permission> findAllPermissionByRoleId(Long roleId);


    @Query("select rp.role from RolePermission rp where rp.permission.id = ?1")
    List<Role> findAllRoleByPermissionId(Long permissionId);

    @Modifying
    @Transactional
    @Query("delete from RolePermission rp where rp.role.id = ?1 AND rp.permission.id IN (?2)")
    void deleteByRoleIdAndPermissionIds(Long roleId, Set<Long> permissionIds);

    @Modifying
    @Transactional
    @Query("delete from RolePermission rp where rp.role.id = ?1")
    void deleteByRoleId(Long roleId);


    @Modifying
    @Transactional
    @Query("delete from RolePermission rp where rp.permission.id = ?1 AND rp.role.id IN (?2)")
    void deleteByPermissionIdAndRoleIds(Long permissionId, Set<Long> roleIds);

}