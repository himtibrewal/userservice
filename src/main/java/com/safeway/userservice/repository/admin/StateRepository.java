package com.safeway.userservice.repository.admin;

import com.safeway.userservice.entity.admin.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    List<State> findAllByCountryId(Long countryId);
}
