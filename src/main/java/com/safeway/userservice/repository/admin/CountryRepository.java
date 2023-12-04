package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.admin.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

}
