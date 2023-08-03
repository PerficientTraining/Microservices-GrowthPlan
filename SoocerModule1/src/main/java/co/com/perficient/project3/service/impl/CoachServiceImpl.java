package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Coach;
import co.com.perficient.project3.repository.CoachRepository;
import co.com.perficient.project3.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachRepository coachRepository;

    @Override
    public Coach create(Coach coach) {
        return coachRepository.save(coach);
    }

    @Override
    public List<Coach> findAll() {
        return coachRepository.findAll();
    }

    @Override
    public Optional<Coach> findById(UUID id) {
        return coachRepository.findById(id);
    }

    @Override
    public Coach update(Coach oldCoach, Coach newCoach) {
        oldCoach.setName(newCoach.getName());
        oldCoach.setNationality(newCoach.getNationality());
        oldCoach.setBirthDate(newCoach.getBirthDate());
        oldCoach.setTeam(newCoach.getTeam());
        return coachRepository.saveAndFlush(oldCoach);
    }

    @Override
    public void delete(UUID id) {
        coachRepository.deleteById(id);
    }

    @Override
    public Optional<Coach> findByName(String name) {
        return coachRepository.findByNameEqualsIgnoreCase(name);
    }
}
