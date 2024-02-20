package com.safeway.userservice.controller;

import com.safeway.userservice.dto.StateDTO;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.entity.admin.State;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.CountryService;
import com.safeway.userservice.service.admin.StateService;
import jakarta.validation.Valid;
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
public class StateController {

    private final CountryService countryService;
    private final StateService stateService;

    public StateController(CountryService countryService, StateService stateService) {
        this.countryService = countryService;
        this.stateService = stateService;
    }

    @PostMapping("/state")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveState(@Valid @RequestBody StateDTO stateDTO) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Country country = countryService.getCountryById(stateDTO.getCountryId());
        State state = State.builder()
                .stateName(stateDTO.getStateName())
                .stateCode(stateDTO.getStateCode())
                .stateAbbr(stateDTO.getStateAbbr())
                .status(stateDTO.getStatus() == null ? 1 : stateDTO.getStatus())
                .country(country)
                .createdBy(userId)
                .createdOn(LocalDateTime.now())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<State>(stateService.saveState(state),
                "SF-200",
                "State Created Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/country/{countryId}/state")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllState(
            @RequestParam(name = "paginated", defaultValue = "false") boolean paginated,
            @RequestParam(name = "page", defaultValue = PAGE_O) int page,
            @PathVariable Long countryId,
            @RequestParam(defaultValue = PAGE_SIZE) int size,
            @RequestParam(name = "sort_by", defaultValue = SORT_BY_ID) String sortBy) {
        Country country = countryService.getCountryById(countryId);
        Response<List<State>> response = null;
        if (paginated) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<State> states = stateService.getAllStatePaginated(country.getId(), pageable);
            response = Response.<List<State>>builder()
                    .data(states.getContent())
                    .totalPages(states.getTotalPages())
                    .currenPage(states.getNumber())
                    .totalItems(states.getTotalElements())
                    .responseCode("SF-200")
                    .responseMessage("State Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
        } else {
            List<State> states = stateService.getAllState();
            response = Response.<List<State>>builder()
                    .data(states)
                    .responseCode("SF-200")
                    .responseMessage("State Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
        }


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/state/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getStateById(@PathVariable Long id) {
        State state = stateService.getStateById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<State>(state,
                "SF-200",
                "State Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/state/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateState(@PathVariable("id") Long id, @RequestBody StateDTO stateDTO) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Country country = countryService.getCountryById(stateDTO.getCountryId());
        State state1 = stateService.getStateById(id);
        State state = State.builder()
                .id(id)
                .stateName(stateDTO.getStateName())
                .stateCode(stateDTO.getStateCode())
                .stateAbbr(stateDTO.getStateAbbr())
                .status(stateDTO.getStatus() == null ? 1 : stateDTO.getStatus())
                .country(country)
                .createdBy(state1.getCreatedBy())
                .createdOn(state1.getCreatedOn())
                .updatedBy(userId)
                .updatedOn(LocalDateTime.now())
                .build();
        state.setUpdatedBy(userId);
        state.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<State>(stateService.saveState(state),
                "SF-200",
                "State Updated Successfully",
                HttpStatus.OK.value()));
    }

    @DeleteMapping("/state/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteState(@PathVariable Long id) {
        stateService.deleteState(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "State Deleted Successfully",
                HttpStatus.OK.value()));
    }
}
