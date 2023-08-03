package co.com.perficient.project3.service;


import co.com.perficient.project3.model.entity.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamService extends CrudService<Team, UUID> {
    Optional<Team> findByName(String name);

    List<Team> findAllByCountry(String country, Integer size);
}
