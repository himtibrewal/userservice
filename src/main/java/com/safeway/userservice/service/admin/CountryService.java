package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    Country getCountryById(Long id);

    List<Country> getAllCountry();

    Page<Country> getAllCountryPageable(Pageable pageable);

    Country updateCountry(Long id, Country country);

    Country saveCountry(Country country);

    void deleteCountry(Long id);
}
