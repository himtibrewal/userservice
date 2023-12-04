package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.repository.admin.CountryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Optional<Country> getCountryById(Long id) {
        return countryRepository.findById(id);
    }

    @Override
    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }

    @Override
    public Country updateCountry(Long id, Country c) {
        Country updateCountry = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not exist with id: " + id));
        updateCountry.setCountryName(c.getCountryName());
        updateCountry.setCountryCode(c.getCountryCode());
        updateCountry.setCountryAbbr(c.getCountryAbbr());
        updateCountry.setUpdatedOn(LocalDateTime.now());
        return countryRepository.save(updateCountry);
    }

    @Override
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }
}
