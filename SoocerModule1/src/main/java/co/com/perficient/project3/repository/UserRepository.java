package co.com.perficient.project3.repository;

import co.com.perficient.project3.model.entity.UserP3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserP3, UUID> {
    Optional<UserP3> findByUsername(String username);
}
