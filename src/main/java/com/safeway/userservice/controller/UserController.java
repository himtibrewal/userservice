package com.safeway.userservice.controller;

import com.safeway.userservice.dto.request.UserRequest;
import com.safeway.userservice.dto.response.PaginationResponse;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.*;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.sequrity.JwtUtils;
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

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, VehicleService vehicleService,
                          JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.vehicleService = vehicleService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest userRequest) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        String password = userRequest.getPassword() == null
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
                .countryCode(userRequest.getCountryCode())
                .status(userRequest.getStatus())
                .countryId(userRequest.getCountryId())
                .stateId(userRequest.getStateId())
                .cityId(userRequest.getCityId())
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

        userService.saveUserRole(userRoles);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<User>(user,
                "SF-201",
                "User Created Successfully",
                HttpStatus.CREATED.value()));
    }

    @GetMapping("/users")
    public ResponseEntity<?> fetchUsers(
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

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<User>(user,
                "SF-200",
                "User Found Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/role/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPermissionByRoleId(@PathVariable Long id) {
        List<User> users = userService.getAllUserByRoleId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<User>>(users,
                "SF-200",
                "User Found Successfully",
                HttpStatus.OK.value()));
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

    @PutMapping("user")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        user.setUpdatedBy(userId);
        user.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<User>(userService.updateUser(userId, user),
                "SF-200",
                "User Updated Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/user/{id}/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUserRole(@PathVariable("id") Long id, @RequestBody Set<Long> roleIds) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Set<Long> roleIdsByUser = roleService.getAllRoleIdByUserId(id);

        Set<Long> insertMapping = roleIds
                .stream()
                .filter(e -> !roleIdsByUser.contains(e))
                .collect(Collectors.toSet());

        Set<Long> deleteMapping = roleIdsByUser
                .stream()
                .filter(e -> !roleIds.contains(e))
                .collect(Collectors.toSet());

        List<UserRole> userRoles = insertMapping.stream().map(key -> {
            return UserRole.builder()
                    .role(Role.builder().id(key).build())
                    .user(User.builder().id(id).build())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        if(deleteMapping.size() > 0){
            userService.deleteUserRoles(id, deleteMapping);
        }

        if(insertMapping.size() >0){
            userService.saveUserRole(userRoles);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<UserRole>>(List.of(),
                "SF-200",
                "User Role Updated Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/user/{id}/vehicle")
    public ResponseEntity<?> fetchUsers2(
            @PathVariable Long id,  @RequestBody List<Long> vehicle) {
        List<User> users = userService.getAllUser();

        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<User>>(users,
                "SF-200",
                "User Found Successfully",
                HttpStatus.OK.value()));

    }

    @PutMapping("user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userService.getUserById(id);
        Set<Role> roleSet = new HashSet<>();
        if (userRequest.getRoleIds() != null || !userRequest.getRoleIds().isEmpty()) {
            roleSet = roleService.findAllByIdInOrderById(userRequest.getRoleIds());
        }

        String password = null;

        if (userRequest.getPassword() != null) {
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
                .createdBy(user.getCreatedBy())
                .createdOn(user.getCreatedOn())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        ;

        return ResponseEntity.ok(new Response<User>(userService.updateUser(id, updatedUser), "SF-201", "User Updated Successfully", HttpStatus.CREATED.value()));
    }

}
