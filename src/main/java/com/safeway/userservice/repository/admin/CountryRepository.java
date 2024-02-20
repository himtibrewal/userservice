package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.admin.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("select c from Country c left join State s ON c.id = s.country.id WHERE s.id = ?1")
    Optional<Country> getCountryByStateId(Long stateId);
}
