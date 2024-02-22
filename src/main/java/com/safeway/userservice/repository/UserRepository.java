package com.safeway.userservice.repository;

import com.safeway.userservice.dto.request.UserRequest;
import com.safeway.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    Optional<User> findFirstByMobile(String mobile);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.regKey = ?2, u.deviceKey = ?3 where u.id = ?1")
    void updateMobileData(Long userId, String regKey, String deviceKey);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = ?2 where u.id = ?1")
    void updateUserPassword(Long userId, String password);


}
