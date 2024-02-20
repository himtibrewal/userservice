package com.safeway.userservice.service.admin;

import com.safeway.userservice.dto.response.StateResponse;
import com.safeway.userservice.entity.admin.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StateService {
    StateResponse getStateByIdWithCountry(Long id);
    State getStateById(Long id);
    List<State> getAllState();
    List<State> getStateByCountryId(Long countryId);
    Page<State> getAllStatePaginated(Long countryId, Pageable pageable);
    State saveState(State state);
    void deleteState(Long id);
}
