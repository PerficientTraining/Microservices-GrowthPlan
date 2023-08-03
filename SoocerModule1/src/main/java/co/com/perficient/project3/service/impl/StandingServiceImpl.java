package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.StandingRepository;
import co.com.perficient.project3.service.StandingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StandingServiceImpl implements StandingService {

    @Autowired
    private StandingRepository standingRepository;

    @Override
    public Standing create(Standing standing) {
        if (findByCompetitionAndTeam(standing.getCompetition(), standing.getTeam()).isEmpty()) {
            return standingRepository.save(standing);
        } else throw new IllegalArgumentException("The standing already exists");
    }

    @Override
    public List<Standing> findAll() {
        return standingRepository.findAll();
    }

    @Override
    public Optional<Standing> findById(UUID id) {
        return standingRepository.findById(id);
    }

    @Override
    public Standing update(Standing oldStanding, Standing newStanding) {
        Optional<Standing> optionalStanding = findByCompetitionAndTeam(newStanding.getCompetition(), newStanding.getTeam());
        if (optionalStanding.isPresent() && !oldStanding.getId().equals(optionalStanding.get().getId())) {
            throw new IllegalArgumentException("The standing already exists");
        } else {
            oldStanding.setWins(newStanding.getWins());
            oldStanding.setDraws(newStanding.getDraws());
            oldStanding.setLosses(newStanding.getLosses());
            oldStanding.setCompetition(newStanding.getCompetition());
            oldStanding.setTeam(newStanding.getTeam());
            return standingRepository.saveAndFlush(oldStanding);
        }
    }

    @Override
    public void delete(UUID id) {
        standingRepository.deleteById(id);
    }

    @Override
    public Optional<Standing> findByCompetitionAndTeam(Competition competition, Team team) {
        return standingRepository.findByCompetitionAndTeam(competition, team);
    }
}
