package co.com.perficient.project3.service;

import co.com.perficient.project3.model.entity.Competition;

import java.util.Optional;
import java.util.UUID;

public interface CompetitionService extends CrudService<Competition, UUID> {
    Optional<Competition> findByName(String name);
}
