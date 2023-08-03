package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.President;
import co.com.perficient.project3.repository.PresidentRepository;
import co.com.perficient.project3.service.PresidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PresidentServiceImpl implements PresidentService {

    @Autowired
    private PresidentRepository presidentRepository;

    @Override
    public President create(President president) {
        return presidentRepository.save(president);
    }

    @Override
    public List<President> findAll() {
        return presidentRepository.findAll();
    }

    @Override
    public Optional<President> findById(UUID id) {
        return presidentRepository.findById(id);
    }

    @Override
    public President update(President oldPresident, President newPresident) {
        oldPresident.setName(newPresident.getName());
        oldPresident.setNationality(newPresident.getNationality());
        oldPresident.setBirthDate(newPresident.getBirthDate());
        oldPresident.setTeam(newPresident.getTeam());
        return presidentRepository.saveAndFlush(oldPresident);
    }

    @Override
    public void delete(UUID id) {
        presidentRepository.deleteById(id);
    }

    @Override
    public Optional<President> findByName(String name) {
        return presidentRepository.findByNameEqualsIgnoreCase(name);
    }
}
