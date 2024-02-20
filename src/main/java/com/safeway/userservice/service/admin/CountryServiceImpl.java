package com.safeway.userservice.service.admin;

import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.admin.CountryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Country getCountryById(Long id) {
        Optional<Country> country =  countryRepository.findById(id);
        if(!country.isPresent()){
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "Country");
        }
       return country.get();
    }

    @Override
    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }

    @Override
    public Page<Country> getAllCountryPageable(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    @Override
    public Country updateCountry(Long id, Country c) {
        Country country =  getCountryById(id);
        country.setCountryName(c.getCountryName());
        country.setCountryCode(c.getCountryCode());
        country.setCountryAbbr(c.getCountryAbbr());
        country.setStatus(c.getStatus());
        country.setUpdatedBy(c.getUpdatedBy());
        country.setUpdatedOn(LocalDateTime.now());
        return countryRepository.save(country);
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
