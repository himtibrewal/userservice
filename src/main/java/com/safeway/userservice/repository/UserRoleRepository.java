package com.safeway.userservice.repository;

import com.safeway.userservice.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("select ur from UserRole ur where ur.user.id IN (?1)")
    List<UserRole> findAllByUserIsIn(Set<Long> userIds);

    @Query("select ur.role.id from UserRole ur where ur.user.id = ?1")
    Set<Long> findAllRoleIdByUserId(Long userId);

    @Query("select ur.user.id from UserRole ur where ur.role.id = ?1")
    Set<Long> findAllUserIdByRoleId(Long roleId);

    @Query("select ur.role from UserRole ur where ur.user.id = ?1")
    List<Role> findAllRoleByUserId(Long userId);


    @Query("select ur.user from UserRole ur where ur.role.id = ?1")
    List<User> findAllUserByRoleId(Long roleId);

    @Modifying
    @Transactional
    @Query("delete from UserRole ur where ur.user.id = ?1 AND ur.role.id IN (?2)")
    void deleteByUserIdAndRoleIds(Long userId, Set<Long> roleIds);

    @Modifying
    @Transactional
    @Query("delete from UserRole ur where ur.user.id = ?1")
    void deleteByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("delete from UserRole ur where ur.role.id = ?1")
    void deleteByRoleId(Long roleId);

    @Modifying
    @Transactional
    @Query("delete from UserRole ur where ur.role.id = ?1 AND ur.user.id IN (?2)")
    void deleteByRoleIdAndUserIds(Long roleId, Set<Long> userIds);

}
