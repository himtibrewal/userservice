package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.District;

import java.util.List;
import java.util.Optional;

public interface DistrictService {
    Optional<District> getDistrictById(Long id);

    List<District> getDistrictByStateId(Long stateId);

    List<District> getAllDistrict();

    District updateDistrict(Long id, District district);

    District saveDistrict(District district);

    void deleteDistrict(Long id);
}
