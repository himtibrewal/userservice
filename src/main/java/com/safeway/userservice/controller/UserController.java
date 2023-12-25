package com.safeway.userservice.controller;

import com.safeway.userservice.dto.request.VehicleRequest;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.dto.response.VehicleResponse;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.entity.Vehicle;
import com.safeway.userservice.sequrity.JwtUtils;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.UserService;
import com.safeway.userservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, VehicleService vehicleService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("user/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("user")
    public User updateUser(@RequestBody User user) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return userService.updateUser(userId, user);
    }

    @PutMapping("user")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }



    @PostMapping("vehicle")
    public ResponseEntity<?>  saveVehicle(@RequestBody VehicleRequest vehicleRequest) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleRequest.getType());
        vehicle.setBrand(vehicleRequest.getBrand());
        vehicle.setModel(vehicleRequest.getModel());
        vehicle.setRegistrationNo(vehicleRequest.getRegistrationNo());
        vehicle.setUserId(userID);
        vehicle.setCreatedBy(userID);
        vehicle.setUpdatedBy(userID);
        Vehicle v =  vehicleService.saveVehicle(vehicle);
        VehicleResponse vehicleResponse = new VehicleResponse(v.getType(), v.getBrand(), v.getModel(), v.getRegistrationNo());
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(vehicleResponse, "SF-201",
                "Vehicle Added Successfully", HttpStatus.CREATED.value()));
    }

}
