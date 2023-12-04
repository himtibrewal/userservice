package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.State;
import com.safeway.userservice.repository.admin.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    @Autowired
    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public Optional<State> getStateById(Long id) {
        return stateRepository.findById(id);
    }

    @Override
    public List<State> getStateByCountryId(Long countryId) {
        return stateRepository.findAllByCountryId(countryId);
    }

    @Override
    public List<State> getAllState() {
        return stateRepository.findAll();
    }

    @Override
    public State updateState(Long id, State s) {
        State updateState = stateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("State not exist with id: " + id));
        updateState.setStateName(s.getStateName());
        updateState.setStateCode(s.getStateCode());
        updateState.setStateAbbr(s.getStateAbbr());
        updateState.setUpdatedOn(LocalDateTime.now());
        updateState.setCountryId(s.getCountryId());
        return stateRepository.save(updateState);
    }

    @Override
    public State saveState(State state) {
        return stateRepository.save(state);
    }

    @Override
    public void deleteState(Long id) {
        stateRepository.deleteById(id);
    }
}
