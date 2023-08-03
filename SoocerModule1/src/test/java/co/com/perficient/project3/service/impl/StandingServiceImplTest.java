package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.StandingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class StandingServiceImplTest {

    @InjectMocks
    private StandingServiceImpl standingService;
    @Mock
    private StandingRepository standingRepository;

    final UUID ID_STANDING = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    void create() {
        Standing standing = Standing.builder().id(ID_STANDING).team(Team.builder().standings(new HashSet<>()).build())
                .build();

        when(standingRepository.save(any(Standing.class))).thenReturn(standing);

        Standing standingCreated = standingService.create(standing);
        assertNotNull(standingCreated);
        assertEquals(ID_STANDING, standingCreated.getId());
    }

    @Test
    void createException() {
        Standing standing = Standing.builder().id(ID_STANDING).build();
        standing.setCompetition(Competition.builder().standings(Set.of(standing)).build());
        standing.setTeam(Team.builder().standings(Set.of(standing)).build());

        when(standingService.findByCompetitionAndTeam(any(Competition.class), any(Team.class))).thenReturn(Optional.of(Standing.builder()
                .build()));

        assertThrows(IllegalArgumentException.class, () -> standingService.create(standing));
    }

    @Test
    void findAll() {
        when(standingRepository.findAll()).thenReturn(Collections.singletonList(Standing.builder().build()));

        List<Standing> standings = standingService.findAll();
        Assertions.assertThat(standings).isNotNull().isNotEmpty();
    }

    @Test
    void findById() {
        Standing standing = Standing.builder().id(ID_STANDING).build();

        when(standingRepository.findById(any(UUID.class))).thenReturn(Optional.of(standing));

        Optional<Standing> optionalStanding = standingService.findById(ID_STANDING);
        assertNotNull(optionalStanding);
        assertEquals(standing, optionalStanding.get());
        Assertions.assertThat(optionalStanding).isPresent();
    }

    @Test
    void update() {
        Standing oldStanding = Standing.builder().build();
        final Integer WINS = 4;
        final Integer DRAWS = 5;
        final Integer LOSSES = 6;
        Standing newStanding = Standing.builder().wins(WINS).draws(DRAWS).losses(LOSSES)
                .team(Team.builder().standings(new HashSet<>()).build()).build();

        when(standingRepository.saveAndFlush(any(Standing.class))).thenReturn(oldStanding);

        Standing standingUpdated = standingService.update(oldStanding, newStanding);
        assertNotNull(standingUpdated);
        assertEquals(WINS, standingUpdated.getWins());
        assertEquals(DRAWS, standingUpdated.getDraws());
        assertEquals(LOSSES, standingUpdated.getLosses());
    }

    @Test
    void updateException() {
        Standing oldStanding = Standing.builder().id(UUID.randomUUID()).build();
        final Integer WINS = 4;
        final Integer DRAWS = 5;
        final Integer LOSSES = 6;
        Standing newStanding = Standing.builder().wins(WINS).draws(DRAWS).losses(LOSSES).build();
        newStanding.setCompetition(Competition.builder().standings(Set.of(newStanding)).build());
        newStanding.setTeam(Team.builder().standings(Set.of(newStanding)).build());

        when(standingService.findByCompetitionAndTeam(any(Competition.class), any(Team.class))).thenReturn(Optional.of(Standing.builder()
                .id(UUID.randomUUID()).build()));

        assertThrows(IllegalArgumentException.class, () -> standingService.update(oldStanding, newStanding));
    }

    @Test
    void delete() {
        standingService.delete(UUID.randomUUID());
        verify(standingRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findByCompetitionAndTeam() {
        Competition competition = Competition.builder().name("competitionA").build();
        Team team = Team.builder().name("teamA").build();
        Standing standing = Standing.builder().competition(competition).team(team).build();

        when(standingRepository.findByCompetitionAndTeam(any(Competition.class), any(Team.class))).thenReturn(Optional.of(standing));

        Optional<Standing> optionalStanding = standingService.findByCompetitionAndTeam(competition, team);
        assertNotNull(optionalStanding);
        assertEquals(standing, optionalStanding.get());
        assertEquals(competition, optionalStanding.get().getCompetition());
        assertEquals(team, optionalStanding.get().getTeam());
    }
}
