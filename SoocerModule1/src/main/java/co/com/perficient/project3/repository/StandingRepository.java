package co.com.perficient.project3.repository;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StandingRepository extends JpaRepository<Standing, UUID> {
    Optional<Standing> findByCompetitionAndTeam(Competition competition, Team team);
}
