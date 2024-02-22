package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findAllByIdInOrderById(Iterable<Long> longs);

}
