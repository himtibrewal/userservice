package com.safeway.userservice.controller;

import com.safeway.userservice.dto.response.PaginationResponse;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.Vehicle;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.safeway.userservice.utils.Commons.*;
import static com.safeway.userservice.utils.Commons.SORT_BY_ID;

@RestController
public class VehicleController extends BaseController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }


    @GetMapping("/vehicle")
    public ResponseEntity<?> getAllVehicle(
            @RequestParam(name = "paginated", defaultValue = PAGINATED_DEFAULT) boolean paginated,
            @RequestParam(name = "page", defaultValue = PAGE_O) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size,
            @RequestParam(name = "sort_by", defaultValue = SORT_BY_ID) String sortBy) {

        if (paginated) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<Vehicle> vehicles = vehicleService.getAllVehicle(pageable);
            PaginationResponse<List<Vehicle>> response = PaginationResponse.<List<Vehicle>>builder()
                    .data(vehicles.getContent())
                    .totalPages(vehicles.getTotalPages())
                    .currentPage(vehicles.getNumber())
                    .totalItems(vehicles.getTotalElements())
                    .responseCode("SF-200")
                    .responseMessage("Vehicle Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            List<Vehicle> vehicles = vehicleService.getAllVehicle();
            Response<List<Vehicle>> response = Response.<List<Vehicle>>builder()
                    .data(vehicles)
                    .responseCode("SF-200")
                    .responseMessage("Vehicle Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping("/vehicle/search")
    public ResponseEntity<?> fetchUsersSearchByKey(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String regNo) {
        List<Vehicle> vehicleList = List.of();
        if(id > 0){
            vehicleList=  List.of(vehicleService.getVehicleById(id));
        } else if (regNo != null && regNo.length() > 3) {
            vehicleList=  List.of(vehicleService.findVehicleByRegNo(regNo));
        } else {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Vehicle");
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<Vehicle>>(vehicleList,
                "SF-200",
                "Vehicle Found Successfully",
                HttpStatus.OK.value()));

    }

    @GetMapping("vehicle/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<Vehicle>(vehicle,
                "SF-201",
                "Vehicle Created Successfully",
                HttpStatus.CREATED.value()));
    }

    @PutMapping("vehicle/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Vehicle vehicle1 = vehicleService.getVehicleById(id);

        Vehicle updatedVehicle = Vehicle.builder()
                .id(vehicle1.getId())
                .registrationNo(Objects.requireNonNullElse(vehicle.getRegistrationNo(), vehicle1.getRegistrationNo()))
                .brand(Objects.requireNonNullElse(vehicle.getBrand(), vehicle1.getBrand()))
                .model(Objects.requireNonNullElse(vehicle.getModel(),vehicle1.getModel()))
               // .image(Objects.requireNonNullElse(vehicle.getImage(), vehicle1.getImage()))
                .type(Objects.requireNonNullElse(vehicle.getType(),vehicle1.getType()))
                .createdBy(vehicle1.getCreatedBy())
                .createdOn(vehicle1.getCreatedOn())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        ;

        return ResponseEntity.ok(new Response<Vehicle>(vehicleService.saveVehicle(updatedVehicle), "SF-200", "Vehicle Updated Successfully", HttpStatus.OK.value()));
    }

    @PostMapping("vehicle")
    public ResponseEntity<?> saveUser(@RequestBody Vehicle vehicle) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Vehicle updatedVehicle = Vehicle.builder()
                .registrationNo(vehicle.getRegistrationNo())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .image(vehicle.getImage())
                .type(vehicle.getType())
                .createdBy(userId)
                .createdOn(LocalDateTime.now())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(new Response<Vehicle>(vehicleService.saveVehicle(updatedVehicle), "SF-200", "Vehicle Updated Successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/vehicle/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "Vehicle Deleted Successfully",
                HttpStatus.OK.value()));

    }

}
