package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.repository.CompetitionRepository;
import co.com.perficient.project3.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Override
    public Competition create(Competition competition) {
        return competitionRepository.save(competition);
    }

    @Override
    public List<Competition> findAll() {
        return competitionRepository.findAll();
    }

    @Override
    public Optional<Competition> findById(UUID id) {
        return competitionRepository.findById(id);
    }

    @Override
    public Competition update(Competition oldCompetition, Competition newCompetition) {
        oldCompetition.setName(newCompetition.getName());
        return competitionRepository.saveAndFlush(oldCompetition);
    }

    @Override
    public void delete(UUID id) {
        competitionRepository.deleteById(id);
    }

    @Override
    public Optional<Competition> findByName(String name) {
        return competitionRepository.findByNameEqualsIgnoreCase(name);
    }
}
