package co.com.perficient.project3.service;

import co.com.perficient.project3.model.entity.Coach;

import java.util.Optional;
import java.util.UUID;

public interface CoachService extends CrudService<Coach, UUID> {
    Optional<Coach> findByName(String name);
}
