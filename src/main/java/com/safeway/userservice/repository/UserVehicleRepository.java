package com.safeway.userservice.repository;

import com.safeway.userservice.entity.User;
import com.safeway.userservice.entity.UserVehicle;
import com.safeway.userservice.entity.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserVehicleRepository extends JpaRepository<UserVehicle, Long> {

    @Query("select uv from UserVehicle uv where uv.user.id IN (?1)")
    List<UserVehicle> findAllByUserIsIn(Set<Long> userIds);

    @Query("select uv.vehicle.id from UserVehicle uv where uv.user.id = ?1")
    Set<Long> findAllVehicleIdByUserId(Long userId);

    @Query("select uv.user.id from UserVehicle uv where uv.vehicle.id = ?1")
    Set<Long> findAllUserIdByVehicleId(Long vehicleId);

    @Query("select uv.vehicle from UserVehicle uv where uv.user.id = ?1")
    List<Vehicle> findAllVehicleByUserId(Long userId);

    @Query("select uv.user from UserVehicle uv where uv.vehicle.id = ?1")
    List<User> findAllUserByVehicleId(Long vehicleId);

    @Modifying
    @Transactional
    @Query("delete from UserVehicle uv where uv.user.id = ?1 AND uv.vehicle.id IN (?2)")
    void deleteByUserIdAndVehicleIds(Long userId, Set<Long> vehicleIds);

    @Modifying
    @Transactional
    @Query("delete from UserVehicle uv where uv.user.id = ?1")
    void deleteByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("delete from UserVehicle uv where uv.vehicle.id = ?1 AND uv.user.id IN (?2)")
    void deleteByVehicleIdAndUserIds(Long vehicleId, Set<Long> userIds);

}