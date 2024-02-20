package com.safeway.userservice.service.admin;

import com.safeway.userservice.dto.response.StateResponse;
import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.entity.admin.State;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.admin.CountryRepository;
import com.safeway.userservice.repository.admin.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    private final CountryRepository countryRepository;


    @Autowired
    public StateServiceImpl(StateRepository stateRepository, CountryRepository countryRepository) {
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public StateResponse getStateByIdWithCountry(Long id) {
        Optional<State> state = stateRepository.findById(id);
        if (state.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "State");
        }
        Optional<Country> country = countryRepository.getCountryByStateId(id);
        if (country.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Country-state");
        }

        return StateResponse.builder()
                .id(state.get().getId())
                .stateName(state.get().getStateName())
                .stateAbbr(state.get().getStateAbbr())
                .stateCode(state.get().getStateCode())
                .status(state.get().getStatus())
                .countryId(country.get().getId())
                .countryName(country.get().getCountryName())
                .build();
    }

    @Override
    public State getStateById(Long id) {
        Optional<State> state = stateRepository.findById(id);
        if (state.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "State");
        }
        return state.get();
    }

    @Override
    public List<State> getAllState() {
        return stateRepository.findAll();
    }

    @Override
    public Page<State> getAllStatePaginated(Long countryId, Pageable pageable) {
        return stateRepository.findAllByCountryId(countryId, pageable);
    }

    @Override
    public List<State> getStateByCountryId(Long countryId) {
        return stateRepository.findAllByCountryId(countryId);
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
