package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Coach;
import co.com.perficient.project3.repository.CoachRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
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

class CoachServiceImplTest {

    @InjectMocks
    private CoachServiceImpl coachService;
    @Mock
    private CoachRepository coachRepository;

    final UUID ID_COACH = UUID.randomUUID();
    final String NAME = "coachName";

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void create() {
        Coach coach = Coach.builder().id(ID_COACH).build();

        when(coachRepository.save(any(Coach.class))).thenReturn(coach);

        Coach coachCreated = coachService.create(coach);
        assertNotNull(coachCreated);
        assertEquals(ID_COACH, coachCreated.getId());
    }

    @Test
    void findAll() {
        when(coachRepository.findAll()).thenReturn(Collections.singletonList(Coach.builder().build()));

        List<Coach> coaches = coachService.findAll();
        Assertions.assertThat(coaches).isNotNull().isNotEmpty();
    }

    @Test
    void findById() {
        Coach coach = Coach.builder().id(ID_COACH).build();

        when(coachRepository.findById(any(UUID.class))).thenReturn(Optional.of(coach));

        Optional<Coach> optionalCoach = coachService.findById(ID_COACH);
        assertNotNull(optionalCoach);
        assertEquals(coach, optionalCoach.get());
        Assertions.assertThat(optionalCoach).isPresent();
    }

    @Test
    void update() {
        Coach oldCoach = Coach.builder().build();
        final String NATIONALITY = "colombian";
        Coach newCoach = Coach.builder().name(NAME).nationality(NATIONALITY).birthDate(LocalDate.now()).build();

        when(coachRepository.saveAndFlush(any(Coach.class))).thenReturn(oldCoach);

        Coach coachUpdated = coachService.update(oldCoach, newCoach);
        assertNotNull(coachUpdated);
        assertEquals(NAME, coachUpdated.getName());
        assertEquals(NATIONALITY, coachUpdated.getNationality());
        assertNotNull(coachUpdated.getBirthDate());
    }

    @Test
    void delete() {
        coachService.delete(UUID.randomUUID());

        verify(coachRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findByName() {
        Coach coach = Coach.builder().name(NAME).build();

        when(coachRepository.findByNameEqualsIgnoreCase(anyString())).thenReturn(Optional.of(coach));

        Optional<Coach> optionalCoach = coachService.findByName(NAME);
        assertNotNull(optionalCoach);
        assertEquals(coach, optionalCoach.get());
    }
}
