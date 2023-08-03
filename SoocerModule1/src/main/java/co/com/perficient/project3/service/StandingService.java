package co.com.perficient.project3.service;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.model.entity.Team;

import java.util.Optional;
import java.util.UUID;

public interface StandingService extends CrudService<Standing, UUID> {
    Optional<Standing> findByCompetitionAndTeam(Competition competition, Team team);
}
