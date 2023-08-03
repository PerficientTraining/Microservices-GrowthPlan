package co.com.perficient.project3.repository;

import co.com.perficient.project3.model.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, UUID> {
    Optional<Competition> findByNameEqualsIgnoreCase(String name);
}
