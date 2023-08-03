package co.com.perficient.project3.repository;

import co.com.perficient.project3.model.entity.President;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PresidentRepository extends JpaRepository<President, UUID> {
    Optional<President> findByNameEqualsIgnoreCase(String name);
}
