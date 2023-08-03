package co.com.perficient.project3.service;

import co.com.perficient.project3.model.entity.Stadium;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StadiumService extends CrudService<Stadium, UUID> {

    Stadium patch(Stadium stadium, Map<String, Object> fields);

    Optional<Stadium> findByName(String name);

    Stream<Stadium> findAllByCountry(String country);
}
