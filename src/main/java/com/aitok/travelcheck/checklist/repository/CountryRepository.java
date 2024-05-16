package com.aitok.travelcheck.checklist.repository;

import com.aitok.travelcheck.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
