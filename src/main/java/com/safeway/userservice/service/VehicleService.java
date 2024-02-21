package com.safeway.userservice.service;

import com.safeway.userservice.dto.response.VehicleResponse;
import com.safeway.userservice.entity.Role;
import com.safeway.userservice.entity.RolePermission;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface VehicleService {
    Vehicle saveVehicle(Vehicle vehicle);
    Vehicle getVehicleById(Long id);
    List<Vehicle> getAllVehicle();
    Page<Vehicle> getAllVehicle(Pageable pageable);
    Set<Long> getAllVehicleIdByUserId(Long userId);
    List<Vehicle> getAllVehicleByUserId(Long userId);

    Vehicle findVehicleByRegNo(String regNo);
    Vehicle updateVehicle(Long id, Vehicle vehicle);
    void deleteVehicle(Long id);
    Set<Vehicle> findAllByIdInOrderById(List<Long> ids);

}
