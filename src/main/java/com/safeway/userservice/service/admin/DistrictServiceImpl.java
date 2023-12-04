package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.District;
import com.safeway.userservice.repository.admin.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Optional<District> getDistrictById(Long id) {
        return districtRepository.findById(id);
    }

    @Override
    public List<District> getDistrictByStateId(Long stateId) {
        return districtRepository.findAllByStateId(stateId);
    }

    @Override
    public List<District> getAllDistrict() {
        return districtRepository.findAll();
    }

    @Override
    public District updateDistrict(Long id, District d) {
        District updateDistrict = districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not exist with id: " + id));
        updateDistrict.setDistrictName(d.getDistrictName());
        updateDistrict.setDistrictCode(d.getDistrictCode());
        updateDistrict.setDistrictAbbr(d.getDistrictAbbr());
        updateDistrict.setUpdatedOn(LocalDateTime.now());
        updateDistrict.setStateId(d.getStateId());
        return districtRepository.save(updateDistrict);
    }

    @Override
    public District saveDistrict(District district) {
        return districtRepository.save(district);
    }

    @Override
    public void deleteDistrict(Long id) {
        districtRepository.deleteById(id);
    }
}
