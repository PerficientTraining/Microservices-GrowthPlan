package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Match;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.MatchRepository;
import co.com.perficient.project3.repository.custom.MatchCustomRepository;
import co.com.perficient.project3.service.CompetitionService;
import co.com.perficient.project3.service.MatchService;
import co.com.perficient.project3.service.StandingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchCustomRepository matchCustomRepository;
    @Autowired
    private StandingService standingService;
    @Autowired
    private CompetitionService competitionService;

    @Override
    public Match create(Match match, String competitionName) {
        if (!match.getHomeTeam().getId().equals(match.getAwayTeam().getId())) {
            Match matchSaved = matchRepository.save(match);
            addTeamsStandings(matchSaved.getScore(), matchSaved.getHomeTeam(), matchSaved.getAwayTeam(), competitionName);
            return matchSaved;
        } else throw new IllegalArgumentException("A team can't play against itself");
    }

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    @Override
    public Optional<Match> findById(UUID id) {
        return matchRepository.findById(id);
    }

    @Override
    public Match update(Match oldMatch, Match newMatch, String competitionName) {
        if (!newMatch.getHomeTeam().getId().equals(newMatch.getAwayTeam().getId())) {
            oldMatch.setDate(newMatch.getDate());
            oldMatch.setStadium(newMatch.getStadium());
            oldMatch.setRound(newMatch.getRound());
            oldMatch.setScore(newMatch.getScore());
            oldMatch.setHomeTeam(newMatch.getHomeTeam());
            oldMatch.setAwayTeam(newMatch.getAwayTeam());
            Match matchUpdated = matchRepository.saveAndFlush(oldMatch);
            addTeamsStandings(matchUpdated.getScore(), matchUpdated.getHomeTeam(), matchUpdated.getAwayTeam(), competitionName);
            return matchUpdated;
        } else throw new IllegalArgumentException("A team can't play against itself");

    }

    @Override
    public void delete(UUID id) {
        matchRepository.deleteById(id);
    }

    @Override
    public List<Match> findLast3Matches() {
        return matchCustomRepository.findLast3Matches();
    }

    private void addTeamsStandings(String score, Team homeTeam, Team awayTeam, String competitionName) {
        Competition competition = competitionService.findByName(competitionName)
                .orElseThrow(() -> new NoSuchElementException(String.format("No value present for competition: %s", competitionName)));
        Integer homeTeamGoals = Integer.valueOf(score.split("-")[0]);
        Integer awayTeamGoals = Integer.valueOf(score.split("-")[1]);

        int s = homeTeamGoals.compareTo(awayTeamGoals);
        if (s > 0) {
            createOrUpdateStanding(competition, homeTeam, 1, 0, 0);
            createOrUpdateStanding(competition, awayTeam, 0, 0, 1);
        } else if (s < 0) {
            createOrUpdateStanding(competition, homeTeam, 0, 0, 1);
            createOrUpdateStanding(competition, awayTeam, 1, 0, 0);
        } else {
            createOrUpdateStanding(competition, homeTeam, 0, 1, 0);
            createOrUpdateStanding(competition, awayTeam, 0, 1, 0);
        }
    }

    private void createOrUpdateStanding(Competition competition, Team team, Integer wins, Integer draws, Integer losses) {
        Optional<Standing> optionalStanding = standingService.findByCompetitionAndTeam(competition, team);
        if (optionalStanding.isEmpty()) {
            standingService.create(Standing.builder().competition(competition).team(team).wins(wins).draws(draws)
                    .losses(losses).build());
        } else {
            Standing standing = optionalStanding.get();
            standingService.update(standing, Standing.builder().competition(competition).team(team)
                    .wins(standing.getWins() + wins).draws(standing.getDraws() + draws)
                    .losses(standing.getLosses() + losses).build());
        }
    }
}
