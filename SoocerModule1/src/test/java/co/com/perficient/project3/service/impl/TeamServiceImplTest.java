package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.TeamRepository;
import com.querydsl.core.types.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TeamServiceImplTest {

    @InjectMocks
    private TeamServiceImpl teamService;
    @Mock
    private TeamRepository teamRepository;

    final UUID ID_TEAM = UUID.randomUUID();
    final String NAME = "teamName";
    final String COUNTRY = "colombia";

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void create() {
        Team team = Team.builder().id(ID_TEAM).build();

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        Team teamCreated = teamService.create(team);
        assertNotNull(teamCreated);
        assertEquals(ID_TEAM, teamCreated.getId());
    }

    @Test
    void findAll() {
        when(teamRepository.findAll()).thenReturn(Collections.singletonList(Team.builder().build()));

        List<Team> teams = teamService.findAll();
        Assertions.assertThat(teams).isNotNull().isNotEmpty();
    }

    @Test
    void findById() {
        Team team = Team.builder().id(ID_TEAM).build();

        when(teamRepository.findById(any(UUID.class))).thenReturn(Optional.of(team));

        Optional<Team> optionalTeam = teamService.findById(ID_TEAM);
        assertNotNull(optionalTeam);
        assertEquals(team, optionalTeam.get());
        Assertions.assertThat(optionalTeam).isPresent();
    }

    @Test
    void update() {
        Team oldTeam = Team.builder().build();
        Team newTeam = Team.builder().name(NAME).stadium(Stadium.builder().build()).build();

        when(teamRepository.saveAndFlush(any(Team.class))).thenReturn(oldTeam);

        Team teamUpdated = teamService.update(oldTeam, newTeam);
        assertNotNull(teamUpdated);
        assertEquals(NAME, teamUpdated.getName());
        assertNotNull(teamUpdated.getStadium());
    }

    @Test
    void delete() {
        teamService.delete(UUID.randomUUID());
        verify(teamRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findByName() {
        Team team = Team.builder().name(NAME).build();

        when(teamRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(team));

        Optional<Team> optionalTeam = teamService.findByName(NAME);
        assertNotNull(optionalTeam);
        assertEquals(team, optionalTeam.get());
        Assertions.assertThat(optionalTeam).isNotNull().isPresent();
    }

    @Test
    void findAllByCountry() {
        Team team = Team.builder().country(COUNTRY).build();

        when(teamRepository.findAllByCountryEqualsIgnoreCase(anyString(), any(Pageable.class))).thenReturn(Collections.nCopies(5, team));

        List<Team> teams = teamService.findAllByCountry(COUNTRY, 5);
        assertNotNull(teams);
        assertEquals(5, teams.size());
    }
}
