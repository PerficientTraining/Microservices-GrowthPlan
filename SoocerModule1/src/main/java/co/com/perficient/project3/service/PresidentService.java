package co.com.perficient.project3.service;

import co.com.perficient.project3.model.entity.President;

import java.util.Optional;
import java.util.UUID;

public interface PresidentService extends CrudService<President, UUID> {
    Optional<President> findByName(String name);
}
