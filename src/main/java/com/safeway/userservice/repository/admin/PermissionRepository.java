package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.admin.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Set<Permission> findAllByIdInOrderById(Iterable<Long> longs);

    List<Permission> findAllByPermissionCodeInOrderById(List<String> permissionCode);

}
