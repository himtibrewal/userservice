package com.safeway.userservice.controller;

import com.safeway.userservice.dto.DistrictDTO;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.admin.District;
import com.safeway.userservice.entity.admin.State;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.DistrictService;
import com.safeway.userservice.service.admin.StateService;
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

import static com.safeway.userservice.utils.Commons.*;

@RestController
@RequestMapping("/admin")
public class CityController {

    private final StateService stateService;
    private final DistrictService districtService;

    public CityController(StateService stateService, DistrictService districtService) {
        this.stateService = stateService;
        this.districtService = districtService;
    }

    @PostMapping("/district")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveDistrict(@RequestBody DistrictDTO districtDTO) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        State state = stateService.getStateById(districtDTO.getStateId());
        District district = District.builder()
                .districtName(districtDTO.getDistrictName())
                .districtCode(districtDTO.getDistrictCode())
                .districtAbbr(districtDTO.getDistrictAbbr())
                .status(districtDTO.getStatus() == null ? 1 : districtDTO.getStatus())
                .state(state)
                .createdBy(userId)
                .createdOn(LocalDateTime.now())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<District>(districtService.saveDistrict(district),
                "SF-200",
                "District Created Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/district")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllDistrict() {
        List<District> district = districtService.getAllDistrict();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<List<District>>(district,
                "SF-200",
                "District Found Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/district/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getDistrictById(@PathVariable Long id) {
        District district = districtService.getDistrictById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<District>(district,
                "SF-200",
                "District Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/district/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateDistrict(@PathVariable("id") Long id, @RequestBody DistrictDTO districtDTO) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        State state = stateService.getStateById(districtDTO.getStateId());
        District district1 = districtService.getDistrictById(id);
        District district = District.builder()
                .id(id)
                .districtName(districtDTO.getDistrictName())
                .districtCode(districtDTO.getDistrictCode())
                .districtAbbr(districtDTO.getDistrictAbbr())
                .status(districtDTO.getStatus() == null ? 1 : districtDTO.getStatus())
                .state(state)
                .createdBy(district1.getCreatedBy())
                .createdOn(district1.getCreatedOn())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        state.setUpdatedBy(userId);
        state.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<District>(districtService.saveDistrict(district),
                "SF-200",
                "District Updated Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("state/{stateId}/district")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllDistrict( @RequestParam(name = "paginated", defaultValue = "false") boolean paginated,
                                             @RequestParam(name = "page", defaultValue = PAGE_O) int page,
                                             @PathVariable Long stateId,
                                             @RequestParam(defaultValue = PAGE_SIZE) int size,
                                             @RequestParam(name = "sort_by", defaultValue = SORT_BY_ID) String sortBy) {

        State state = stateService.getStateById(stateId);
        Response<List<District>> response = null;
        if (paginated) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<District> districts = districtService.getAllDistrictPaginated(state.getId(), pageable);
            response = Response.<List<District>>builder()
                    .data(districts.getContent())
                    .totalPages(districts.getTotalPages())
                    .currenPage(districts.getNumber())
                    .totalItems(districts.getTotalElements())
                    .responseCode("SF-200")
                    .responseMessage("District Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
        } else {
            List<District> districts = districtService.getAllDistrict();
            response = Response.<List<District>>builder()
                    .data(districts)
                    .responseCode("SF-200")
                    .responseMessage("District Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/district/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteDistrict(@PathVariable Long id) {
        districtService.deleteDistrict(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "District Deleted Successfully",
                HttpStatus.OK.value()));
    }
}
