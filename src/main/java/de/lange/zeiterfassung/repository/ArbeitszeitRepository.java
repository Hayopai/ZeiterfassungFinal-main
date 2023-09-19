package de.lange.zeiterfassung.repository;

import de.lange.zeiterfassung.model.ArbeitszeitEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArbeitszeitRepository extends JpaRepository<ArbeitszeitEntity, Long> {

    Optional<ArbeitszeitEntity> findByUserId(Long userId);
}
