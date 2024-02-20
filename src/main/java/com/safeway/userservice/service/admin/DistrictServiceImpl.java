package com.safeway.userservice.service.admin;

import com.safeway.userservice.dto.response.DistrictResponse;
import com.safeway.userservice.dto.response.StateResponse;
import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.entity.admin.District;
import com.safeway.userservice.entity.admin.State;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.admin.CountryRepository;
import com.safeway.userservice.repository.admin.DistrictRepository;
import com.safeway.userservice.repository.admin.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository, StateRepository stateRepository, CountryRepository countryRepository) {
        this.districtRepository = districtRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public District getDistrictById(Long id) {
        Optional<District> district =  districtRepository.findById(id);
        if(!district.isPresent()){
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "State");
        }
        return district.get();
    }

    @Override
    public DistrictResponse getDistrictByIdWithStateANDCountry(Long id) {
        Optional<District> district = districtRepository.findById(id);
        if (district.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "District");
        }

        Optional<State> state = stateRepository.getStateByDistrictId(id);
        if (state.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "state-District");
        }

        Optional<Country> country = countryRepository.getCountryByStateId(state.get().getId());
        if (country.isEmpty()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Country-state");
        }

        return DistrictResponse.builder()
                .id(district.get().getId())
                .districtName(district.get().getDistrictName())
                .districtCode(district.get().getDistrictCode())
                .districtAbbr(district.get().getDistrictAbbr())
                .status(district.get().getStatus())
                .stateId(state.get().getId())
                .stateName(state.get().getStateName())
                .countryId(country.get().getId())
                .countryName(country.get().getCountryName())
                .build();
    }

    @Override
    public List<District> getAllDistrict() {
        return districtRepository.findAll();
    }

    @Override
    public List<District> getDistrictByStateId(Long stateId) {
        return districtRepository.findAllByStateId(stateId);
    }

    @Override
    public Page<District> getAllDistrictPaginated(Long stateId, Pageable pageable) {
        return districtRepository.findAllByStateId(stateId, pageable);
    }

    @Override
    public District saveDistrict(District state) {
        return districtRepository.save(state);
    }

    @Override
    public void deleteDistrict(Long id) {
        districtRepository.deleteById(id);
    }
}
