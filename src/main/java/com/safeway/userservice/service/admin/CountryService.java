package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    Optional<Country> getCountryById(Long id);

    List<Country> getAllCountry();

    Country updateCountry(Long id, Country country);

    Country saveCountry(Country country);

    void deleteCountry(Long id);
}
