package com.safeway.userservice.service.admin;

import com.safeway.userservice.dto.response.DistrictResponse;
import com.safeway.userservice.dto.response.StateResponse;
import com.safeway.userservice.entity.admin.District;
import com.safeway.userservice.entity.admin.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DistrictService {
    District getDistrictById(Long id);
    DistrictResponse getDistrictByIdWithStateANDCountry(Long id);
    List<District> getAllDistrict();
    List<District> getDistrictByStateId(Long stateId);
    Page<District> getAllDistrictPaginated(Long stateId, Pageable pageable);
    District saveDistrict(District state);
    void deleteDistrict(Long id);
}
