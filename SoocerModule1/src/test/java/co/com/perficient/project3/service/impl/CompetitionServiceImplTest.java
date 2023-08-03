package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.repository.CompetitionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

class CompetitionServiceImplTest {

    @InjectMocks
    private CompetitionServiceImpl competitionService;
    @Mock
    private CompetitionRepository competitionRepository;

    private final UUID ID_COMPETITION = UUID.randomUUID();
    private final String NAME = "competitionName";

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void create() {
        Competition competition = Competition.builder().id(ID_COMPETITION).build();

        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        Competition competitionCreated = competitionService.create(competition);
        assertNotNull(competitionCreated);
        assertEquals(ID_COMPETITION, competitionCreated.getId());
    }

    @Test
    void findAll() {
        when(competitionRepository.findAll()).thenReturn(Collections.singletonList(Competition.builder().build()));

        List<Competition> competitions = competitionService.findAll();
        Assertions.assertThat(competitions).isNotNull().isNotEmpty();
    }

    @Test
    void findById() {
        Competition competition = Competition.builder().id(ID_COMPETITION).build();

        when(competitionRepository.findById(any(UUID.class))).thenReturn(Optional.of(competition));

        Optional<Competition> optionalCompetition = competitionService.findById(ID_COMPETITION);
        assertNotNull(optionalCompetition);
        assertEquals(competition, optionalCompetition.get());
        Assertions.assertThat(optionalCompetition).isPresent();
    }

    @Test
    void update() {
        Competition oldCompetition = Competition.builder().build();
        Competition newCompetition = Competition.builder().name(NAME).build();

        when(competitionRepository.saveAndFlush(any(Competition.class))).thenReturn(oldCompetition);

        Competition competitionUpdated = competitionService.update(oldCompetition, newCompetition);
        assertNotNull(competitionUpdated);
        assertEquals(NAME, competitionUpdated.getName());
    }

    @Test
    void delete() {
        competitionService.delete(UUID.randomUUID());
        verify(competitionRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findByName() {
        Competition competition = Competition.builder().name(NAME).build();

        when(competitionRepository.findByNameEqualsIgnoreCase(anyString())).thenReturn(Optional.of(competition));

        Optional<Competition> optionalCompetition = competitionService.findByName(NAME);
        assertNotNull(optionalCompetition);
        assertEquals(competition, optionalCompetition.get());
        Assertions.assertThat(optionalCompetition).isPresent();
    }
}