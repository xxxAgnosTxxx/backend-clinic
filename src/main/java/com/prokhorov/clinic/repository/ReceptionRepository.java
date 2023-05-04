package com.prokhorov.clinic.repository;

import com.prokhorov.clinic.entity.Reception;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {
}
