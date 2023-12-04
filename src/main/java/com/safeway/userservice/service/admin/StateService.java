package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.State;

import java.util.List;
import java.util.Optional;

public interface StateService {
    Optional<State> getStateById(Long id);

    List<State> getStateByCountryId(Long countryId);

    List<State> getAllState();

    State updateState(Long id, State state);

    State saveState(State state);

    void deleteState(Long id);
}
