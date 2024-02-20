package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.entity.admin.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    @Query("select s from State s WHERE s.country.id = ?1")
    List<State> findAllByCountryId(Long countryId);

    @Query("select s from State s WHERE s.country.id = ?1")
    Page<State> findAllByCountryId(Long countryId, Pageable pageable);
}

