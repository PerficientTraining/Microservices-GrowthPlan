package co.com.perficient.project3.repository;

import co.com.perficient.project3.model.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoachRepository extends JpaRepository<Coach, UUID> {
    Optional<Coach> findByNameEqualsIgnoreCase(String name);
}
