package com.safeway.userservice.service;

import com.safeway.userservice.dto.response.VehicleResponse;
import com.safeway.userservice.entity.Vehicle;

public interface VehicleService {

    Vehicle saveVehicle(Vehicle vehicle);

}
