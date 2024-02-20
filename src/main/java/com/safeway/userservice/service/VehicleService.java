package com.safeway.userservice.service;

import com.safeway.userservice.dto.response.VehicleResponse;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {

    Vehicle getVehicleById(Long id);

    Vehicle findVehicleByRegNo(String regNo);

    Page<Vehicle> getAllVehicle(Pageable pageable);
    List<Vehicle> getAllVehicle();

    Vehicle saveVehicle(Vehicle vehicle);

    void deleteVehicle(Long id);

}
