package co.com.perficient.project3.service;

import co.com.perficient.project3.model.entity.Match;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchService {
    Match create(Match match, String competitionName);

    List<Match> findAll();

    Optional<Match> findById(UUID id);

    Match update(Match oldMatch, Match newMatch, String competitionName);

    void delete(UUID id);

    List<Match> findLast3Matches();
}
