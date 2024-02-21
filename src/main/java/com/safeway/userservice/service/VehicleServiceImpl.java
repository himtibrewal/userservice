package com.safeway.userservice.service;


import com.safeway.userservice.dto.response.VehicleResponse;
import com.safeway.userservice.entity.Vehicle;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.UserVehicleRepository;
import com.safeway.userservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    private final UserVehicleRepository userVehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, UserVehicleRepository userVehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userVehicleRepository = userVehicleRepository;
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        Optional<Vehicle> vehicle  = vehicleRepository.findById(id);
        if(!vehicle.isPresent()){
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "vehicle");
        }
        return vehicle.get();
    }

    @Override
    public Vehicle findVehicleByRegNo(String regNo) {
        Optional<Vehicle> vehicle  = vehicleRepository.findFirstByRegistrationNo(regNo);
        if(!vehicle.isPresent()){
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "vehicle");
        }
        return vehicle.get();
    }

    @Override
    public List<Vehicle> getAllVehicle() {
        return vehicleRepository.findAll();
    }

    @Override
    public Page<Vehicle> getAllVehicle(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public Set<Long> getAllVehicleIdByUserId(Long userId) {
        return userVehicleRepository.findAllVehicleIdByUserId(userId);
    }

    @Override
    public List<Vehicle> getAllVehicleByUserId(Long userId) {
        return userVehicleRepository.findAllVehicleByUserId(userId);
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        return null;
    }

    @Override
    public Set<Vehicle> findAllByIdInOrderById(List<Long> ids) {
        return null;
    }
}
