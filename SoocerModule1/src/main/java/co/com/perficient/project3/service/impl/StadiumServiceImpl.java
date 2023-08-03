package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.QStadium;
import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.repository.StadiumRepository;
import co.com.perficient.project3.service.StadiumService;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StadiumServiceImpl implements StadiumService {

    @Autowired
    private StadiumRepository stadiumRepository;

    @Override
    public Stadium create(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

    @Override
    public List<Stadium> findAll() {
        return stadiumRepository.findAll();
    }

    @Override
    public Optional<Stadium> findById(UUID id) {
        return stadiumRepository.findById(id);
    }

    @Override
    public Stadium update(Stadium oldStadium, Stadium newStadium) {
        oldStadium.setName(newStadium.getName());
        oldStadium.setCountry(newStadium.getCountry());
        oldStadium.setCity(newStadium.getCity());
        oldStadium.setCapacity(newStadium.getCapacity());
        return stadiumRepository.saveAndFlush(oldStadium);
    }

    @Override
    public void delete(UUID id) {
        stadiumRepository.deleteById(id);
    }

    @Override
    public Stadium patch(Stadium stadium, Map<String, Object> fields) {
        fields.forEach((s, value) -> {
            Field field = ReflectionUtils.findField(Stadium.class, s);
            if (Objects.nonNull(field)) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, stadium, value);
            }
        });
        return stadiumRepository.saveAndFlush(stadium);
    }

    @Override
    public Optional<Stadium> findByName(String name) {
        QStadium stadium = QStadium.stadium;
        Predicate predicateName = stadium.name.equalsIgnoreCase(name);
        return stadiumRepository.findOne(predicateName);
    }

    @Override
    public Stream<Stadium> findAllByCountry(String country) {
        return stadiumRepository.findAll().stream().filter(stadium -> country.equalsIgnoreCase(stadium.getCountry()));
    }
}
