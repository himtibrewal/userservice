package com.safeway.userservice.controller;

import com.safeway.userservice.dto.request.UserRequest;
import com.safeway.userservice.dto.response.PaginationResponse;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.dto.response.UserResponse;
import com.safeway.userservice.entity.*;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.UserService;
import com.safeway.userservice.service.VehicleService;
import com.safeway.userservice.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.safeway.userservice.utils.Commons.*;

@RestController
public class UserController extends BaseController {

    private final UserService userService;

    private final RoleService roleService;

    private final VehicleService vehicleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, VehicleService vehicleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.vehicleService = vehicleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest userRequest) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        String password = isNullOrEmpty(userRequest.getPassword())
                ? passwordEncoder.encode(DEFAULT_PASSWORD)
                : passwordEncoder.encode(userRequest.getPassword());

        User newUser = User.builder()
                .username(userRequest.getUserName())
                .email(userRequest.getEmail())
                .mobile(userRequest.getMobile())
                .password(password)
                .bloodGroup(userRequest.getBloodGroup())
                .emergencyContact1(userRequest.getEmergencyContact1())
                .emergencyContact2(userRequest.getEmergencyContact2())
                .status(userRequest.getStatus())
                .countryId(userRequest.getCountryId())
                .stateId(userRequest.getStateId())
                .districtId(userRequest.getDistrictId())
                .address1(userRequest.getAddress1())
                .address2(userRequest.getAddress2())
                .createdBy(userId)
                .createdOn(LocalDateTime.now())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();

        User user = userService.saveUser(newUser);

        List<UserRole> userRoles = userRequest.getRoleIds().stream().map(key -> {
            return UserRole.builder()
                    .user(User.builder().id(user.getId()).build())
                    .role(Role.builder().id(key).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        List<UserVehicle> userVehicles = userRequest.getVehicleIds().stream().map(key -> {
            return UserVehicle.builder()
                    .user(User.builder().id(user.getId()).build())
                    .vehicle(Vehicle.builder().id(key).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        if(userRoles.size() > 0){
            userService.saveUserRole(userRoles);
        }

        if(userVehicles.size() > 0){
            userService.saveUserVehicle(userVehicles);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<User>(user,
                "SF-201",
                "User Created Successfully",
                HttpStatus.CREATED.value()));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser(
            @RequestParam(name = "paginated", defaultValue = PAGINATED_DEFAULT) boolean paginated,
            @RequestParam(name = "page", defaultValue = PAGE_O) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size,
            @RequestParam(name = "sort_by", defaultValue = SORT_BY_ID) String sortBy) {

        if (paginated) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<User> users = userService.getAllUser(pageable);
            PaginationResponse<List<User>> response = PaginationResponse.<List<User>>builder()
                    .data(users.getContent())
                    .totalPages(users.getTotalPages())
                    .currentPage(users.getNumber())
                    .totalItems(users.getTotalElements())
                    .responseCode("SF-200")
                    .responseMessage("User Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            List<User> users = userService.getAllUser();
            Response<List<User>> response = Response.<List<User>>builder()
                    .data(users)
                    .responseCode("SF-200")
                    .responseMessage("User Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

//    @GetMapping("/user/{id}")
//    public ResponseEntity<?> getUser(@PathVariable Long id) {
//        User user = userService.getUserById(id);
//        return ResponseEntity.status(HttpStatus.OK).body(new Response<User>(user,
//                "SF-200",
//                "User Found Successfully",
//                HttpStatus.OK.value()));
//    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserWithRoleAndVehicle(@PathVariable Long id) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getAllRoleByUserId(user.getId());
        List<Vehicle> vehicles = vehicleService.getAllVehicleByUserId(user.getId());
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .status(user.getStatus())
                .emergencyContact1(user.getEmergencyContact1())
                .emergencyContact2(user.getEmergencyContact2())
                .address1(user.getAddress1())
                .address2(user.getAddress2())
                .countryId(user.getCountryId())
                .stateId(user.getStateId())
                .districtId(user.getDistrictId())
                .bloodGroup(user.getBloodGroup())
                .roles(roles)
                .vehicles(vehicles)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<UserResponse>(response,
                "SF-200",
                "User Found Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/role/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserByRoleId(@PathVariable Long id) {
        List<User> users = userService.getAllUserByRoleId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<User>>(users,
                "SF-200",
                "User Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userService.getUserById(id);

        Set<Long> roleIdsByUser = roleService.getAllRoleIdByUserId(id);

        Set<Long> vehicleIdsByUser = vehicleService.getAllVehicleIdByUserId(id);

        Set<Long> insertMappingRole = userRequest.getRoleIds()
                .stream()
                .filter(e -> !roleIdsByUser.contains(e))
                .collect(Collectors.toSet());

        Set<Long> insertMappingVehicle = userRequest.getVehicleIds()
                .stream()
                .filter(e -> !vehicleIdsByUser.contains(e))
                .collect(Collectors.toSet());

        Set<Long> deleteMappingRole = roleIdsByUser
                .stream()
                .filter(e -> !userRequest.getRoleIds().contains(e))
                .collect(Collectors.toSet());

        Set<Long> deleteMappingVehicle = vehicleIdsByUser
                .stream()
                .filter(e -> !userRequest.getVehicleIds().contains(e))
                .collect(Collectors.toSet());

        List<UserRole> userRoles = insertMappingRole.stream().map(key -> {
            return UserRole.builder()
                    .role(Role.builder().id(key).build())
                    .user(User.builder().id(id).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        List<UserVehicle> userVehicles = insertMappingVehicle.stream().map(key -> {
            return UserVehicle.builder()
                    .vehicle(Vehicle.builder().id(key).build())
                    .user(User.builder().id(id).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        if(deleteMappingRole.size() > 0){
            userService.deleteUserRoles(id, deleteMappingRole);
        }

        if(insertMappingRole.size() >0){
            userService.saveUserRole(userRoles);
        }


        if(deleteMappingVehicle.size() > 0){
            userService.deleteUserVehicles(id, deleteMappingVehicle);
        }

        if(insertMappingVehicle.size() >0){
            userService.saveUserVehicle(userVehicles);
        }

        String password = user.getPassword();

        if (isNotNullANDEmpty(userRequest.getPassword())) {
            password = passwordEncoder.encode(userRequest.getPassword());
        }

        User updatedUser = User.builder()
                .id(user.getId())
                .username(Objects.requireNonNullElse(userRequest.getUserName(), user.getUsername()))
                .email(Objects.requireNonNullElse(userRequest.getEmail(), user.getEmail()))
                .mobile(Objects.requireNonNullElse(userRequest.getMobile(), user.getMobile()))
                .password(Objects.requireNonNullElse(password, user.getPassword()))
                .bloodGroup(Objects.requireNonNullElse(userRequest.getBloodGroup(), user.getBloodGroup()))
                .emergencyContact1(Objects.requireNonNullElse(userRequest.getEmergencyContact1(), user.getEmergencyContact1()))
                .emergencyContact2(Objects.requireNonNullElse(userRequest.getEmergencyContact2(), user.getEmergencyContact2()))
                .status(Objects.requireNonNullElse(userRequest.getStatus(), user.getStatus()))
                .address1(Objects.requireNonNullElse(userRequest.getAddress1(), user.getAddress1()))
                .address2(Objects.requireNonNullElse(userRequest.getAddress2(), user.getAddress2()))
                .countryId(Objects.requireNonNullElse(userRequest.getCountryId(), user.getCountryId()))
                .stateId(Objects.requireNonNullElse(userRequest.getStateId(), user.getStateId()))
                .districtId(Objects.requireNonNullElse(userRequest.getDistrictId(), user.getDistrictId()))
                .createdBy(user.getCreatedBy())
                .createdOn(user.getCreatedOn())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();

        User user1 =  userService.saveUser(updatedUser);

        return ResponseEntity.ok(new Response<User>(user1, "SF-201", "User Updated Successfully", HttpStatus.CREATED.value()));
    }


    @PutMapping("user/{id}/vehicle")
    public ResponseEntity<?> updateUserVehicle(@PathVariable Long id,  @RequestBody List<Long> vehicleIds) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userService.getUserById(id);
        Set<Long> vehicleList = vehicleService.getAllVehicleIdByUserId(id);
        if(vehicleIds.size() != 1){
            throw new NotFoundException(ErrorEnum.ERROR_BAD_REQUEST);
        }
        Vehicle vehicle = vehicleService.getVehicleById(vehicleIds.get(0));

        if(vehicleList.contains(vehicle.getId())){
            throw new NotFoundException(ErrorEnum.ERROR_VEHICLE_ALREADY_AVAILABLE);
        }

        UserVehicle userVehicles = UserVehicle.builder()
                    .vehicle(Vehicle.builder().id(vehicle.getId()).build())
                    .user(User.builder().id(user.getId()).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();

        List<UserVehicle> userVehicles1 = userService.saveUserVehicle(List.of(userVehicles));

        return ResponseEntity.ok(new Response<List<UserVehicle>>(userVehicles1, "SF-201", "Vehicle Updated Successfully", HttpStatus.CREATED.value()));
    }

    @GetMapping("/user/search")
    public ResponseEntity<?> fetchUsersBySearchKeys(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    ) {

        List<User> userList = List.of();
        if(id > 0){
            userList=  List.of(userService.getUserById(id));
        } else if (email != null && email.length() > 3) {
            userList=  List.of(userService.findUserByEmail(email));
        } else if(phone != null && phone.length() > 3){
            userList=  List.of(userService.findUserByMobile(phone));
        } else if(username != null && username.length() > 3){
            userList=  List.of(userService.findUserByMobile(phone));
        } else {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "user");
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<User>>(userList,
                "SF-200",
                "User Found Successfully",
                HttpStatus.OK.value()));
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "User Deleted Successfully",
                HttpStatus.OK.value()));
    }

}
