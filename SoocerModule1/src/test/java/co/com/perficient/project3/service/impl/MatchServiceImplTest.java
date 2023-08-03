package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Match;
import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.MatchRepository;
import co.com.perficient.project3.repository.custom.MatchCustomRepository;
import co.com.perficient.project3.service.CompetitionService;
import co.com.perficient.project3.service.StandingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class MatchServiceImplTest {

    @InjectMocks
    private MatchServiceImpl matchService;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchCustomRepository matchCustomRepository;
    @Mock
    private StandingService standingService;
    @Mock
    private CompetitionService competitionService;

    final UUID ID_MATCH = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void create() {
        final String COMPETITION_NAME = "ABC";
        Match match = Match.builder().id(ID_MATCH).score("0-0").homeTeam(Team.builder().id(UUID.randomUUID()).build())
                .awayTeam(Team.builder().id(UUID.randomUUID()).build()).build();
        Competition competition = Competition.builder().name(COMPETITION_NAME).build();

        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(competitionService.findByName(anyString())).thenReturn(Optional.of(competition));

        Match matchCreated = matchService.create(match, COMPETITION_NAME);
        assertNotNull(matchCreated);
        assertEquals(ID_MATCH, matchCreated.getId());
    }

    @Test
    void createException() {
        final UUID ID = UUID.randomUUID();
        final String COMPETITION_NAME = "ABC";
        Match match = Match.builder().id(ID_MATCH).score("0-0").homeTeam(Team.builder().id(ID).build())
                .awayTeam(Team.builder().id(ID).build()).build();

        assertThrows(IllegalArgumentException.class, () -> matchService.create(match, COMPETITION_NAME));
    }

    @Test
    void findAll() {
        when(matchRepository.findAll()).thenReturn(Collections.singletonList(Match.builder().build()));

        List<Match> matches = matchService.findAll();
        Assertions.assertThat(matches).isNotNull().isNotEmpty();
    }

    @Test
    void findById() {
        Match match = Match.builder().id(ID_MATCH).build();

        when(matchRepository.findById(any(UUID.class))).thenReturn(Optional.of(match));

        Optional<Match> optionalMatch = matchService.findById(ID_MATCH);
        assertNotNull(optionalMatch);
        assertEquals(match, optionalMatch.get());
        Assertions.assertThat(optionalMatch).isPresent();
    }

    @Test
    void update() {
        final String COMPETITION_NAME = "ABC";
        Competition competition = Competition.builder().name(COMPETITION_NAME).build();
        Match oldMatch = Match.builder().build();
        final String ROUND = "lAST 16";
        Match newMatch = Match.builder().date(LocalDate.now()).stadium(Stadium.builder().build()).round(ROUND)
                .score("1-0").homeTeam(Team.builder().id(UUID.randomUUID()).name("homeTeam").build())
                .awayTeam(Team.builder().id(UUID.randomUUID()).name("awayTeam").build()).build();

        when(matchRepository.saveAndFlush(any(Match.class))).thenReturn(oldMatch);
        when(competitionService.findByName(anyString())).thenReturn(Optional.of(competition));

        Match matchUpdated = matchService.update(oldMatch, newMatch, COMPETITION_NAME);
        assertNotNull(matchUpdated);
        assertNotNull(matchUpdated.getDate());
        assertNotNull(matchUpdated.getStadium());
        assertEquals(ROUND, matchUpdated.getRound());
        assertEquals("1-0", matchUpdated.getScore());
        assertEquals("homeTeam", matchUpdated.getHomeTeam().getName());
        assertEquals("awayTeam", matchUpdated.getAwayTeam().getName());

        matchUpdated.setScore("0-1");
        Match matchUpdatedB = matchService.update(oldMatch, matchUpdated, COMPETITION_NAME);
        assertNotNull(matchUpdatedB);
        assertEquals("0-1", matchUpdatedB.getScore());
    }

    @Test
    void updateException() {
        final String COMPETITION_NAME = "ABC";
        final UUID ID = UUID.randomUUID();
        Match oldMatch = Match.builder().build();
        final String ROUND = "lAST 16";
        Match newMatch = Match.builder().date(LocalDate.now()).stadium(Stadium.builder().build()).round(ROUND)
                .score("1-0").homeTeam(Team.builder().id(ID).name("homeTeam").build())
                .awayTeam(Team.builder().id(ID).name("awayTeam").build()).build();

        assertThrows(IllegalArgumentException.class, () -> matchService.update(oldMatch, newMatch, COMPETITION_NAME));
    }

    @Test
    void delete() {
        matchService.delete(UUID.randomUUID());
        verify(matchRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findLast3Matches() {
        when(matchCustomRepository.findLast3Matches()).thenReturn(Collections.nCopies(3, Match.builder().build()));

        List<Match> lastMatches = matchService.findLast3Matches();
        assertNotNull(lastMatches);
        assertEquals(3, lastMatches.size());
        verify(matchCustomRepository, times(1)).findLast3Matches();
    }

    @Test
    void createOrUpdateStanding() {
        Competition competition = Competition.builder().build();
        Team team = Team.builder().build();
        when(standingService.findByCompetitionAndTeam(any(Competition.class), any(Team.class))).thenReturn(Optional.of(Standing.builder()
                .wins(0).draws(0).losses(0).build()));

        ReflectionTestUtils.invokeMethod(matchService, "createOrUpdateStanding", competition, team, 1, 0, 0);

        verify(standingService, times(1)).update(any(Standing.class), any(Standing.class));
    }
}
