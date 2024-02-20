package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.District;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.admin.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
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
