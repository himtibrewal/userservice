//package com.safeway.userservice.repository;
//
//import com.safeway.userservice.entity.UserRoles;
//import com.safeway.userservice.entity.admin.Role;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface UseRolesRepository extends JpaRepository<UserRoles, Long> {
//
//    List<UserRoles> findAllByUserId(Long aLong);
//
//    @Query("select r.roleName, r.roleCode from UserRoles ur LEFT join Role r on ur.role.id = r.id  where ur.user =: userId")
//    List<Role> getAllRoleByUser(@Param("user_id") Long userId);
//}
