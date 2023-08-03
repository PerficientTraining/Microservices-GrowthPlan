package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.repository.StadiumRepository;
import com.querydsl.core.types.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class StadiumServiceImplTest {

    @InjectMocks
    private StadiumServiceImpl stadiumService;
    @Mock
    private StadiumRepository stadiumRepository;

    final UUID ID_STADIUM = UUID.randomUUID();
    final String NAME = "stadiumName";
    final String COUNTRY = "colombia";

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void create() {
        Stadium stadium = Stadium.builder().id(ID_STADIUM).build();

        when(stadiumRepository.save(any(Stadium.class))).thenReturn(stadium);

        Stadium stadiumCreated = stadiumService.create(stadium);
        assertNotNull(stadiumCreated);
        assertEquals(ID_STADIUM, stadiumCreated.getId());
    }

    @Test
    void findAll() {
        when(stadiumRepository.findAll()).thenReturn(Collections.singletonList(Stadium.builder().build()));

        List<Stadium> stadiums = stadiumService.findAll();
        Assertions.assertThat(stadiums).isNotNull().isNotEmpty();
    }

    @Test
    void findById() {
        Stadium stadium = Stadium.builder().id(ID_STADIUM).build();

        when(stadiumRepository.findById(any(UUID.class))).thenReturn(Optional.of(stadium));

        Optional<Stadium> optionalStadium = stadiumService.findById(ID_STADIUM);
        assertNotNull(optionalStadium);
        assertEquals(stadium, optionalStadium.get());
        Assertions.assertThat(optionalStadium).isPresent();
    }

    @Test
    void update() {
        Stadium oldStadium = Stadium.builder().build();
        final String CITY = "bogota";
        final String CAPACITY = "2000";
        Stadium newStadium = Stadium.builder().name(NAME).country(COUNTRY).city(CITY).capacity(CAPACITY).build();

        when(stadiumRepository.saveAndFlush(any(Stadium.class))).thenReturn(oldStadium);

        Stadium stadiumUpdated = stadiumService.update(oldStadium, newStadium);
        assertNotNull(stadiumUpdated);
        assertEquals(NAME, stadiumUpdated.getName());
        assertEquals(COUNTRY, stadiumUpdated.getCountry());
        assertEquals(CITY, stadiumUpdated.getCity());
        assertEquals(CAPACITY, stadiumUpdated.getCapacity());
    }

    @Test
    void delete() {
        stadiumService.delete(UUID.randomUUID());
        verify(stadiumRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void patch() {
        Stadium stadium = Stadium.builder().build();
        Map<String, Object> fields = new HashMap<>();

        fields.put("name", NAME);
        when(stadiumRepository.saveAndFlush(any(Stadium.class))).thenReturn(stadium);

        Stadium stadiumPatched = stadiumService.patch(stadium, fields);
        assertNotNull(stadiumPatched);
        assertEquals(NAME, stadiumPatched.getName());
    }

    @Test
    void findByName() {
        Stadium stadium = Stadium.builder().name(NAME).build();

        when(stadiumRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(stadium));

        Optional<Stadium> optionalStadium = stadiumService.findByName(NAME);
        assertNotNull(optionalStadium);
        assertEquals(stadium, optionalStadium.get());
    }

    @Test
    void findByCountry() {
        when(stadiumRepository.findAll()).thenReturn(Arrays.asList(Stadium.builder().country(COUNTRY)
                .build(), Stadium.builder().build()));

        Stream<Stadium> stadiums = stadiumService.findAllByCountry(COUNTRY);
        Assertions.assertThat(stadiums).isNotNull().isNotEmpty();
    }
}
