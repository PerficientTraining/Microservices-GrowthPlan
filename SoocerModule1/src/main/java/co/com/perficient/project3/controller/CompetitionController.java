package co.com.perficient.project3.controller;

import co.com.perficient.project3.mapper.CompetitionMapper;
import co.com.perficient.project3.model.dto.CompetitionDTO;
import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.CompetitionConstants.COMPETITION;

@RestController
@RequestMapping(value = COMPETITION, produces = MediaType.APPLICATION_JSON_VALUE)
public class CompetitionController {

    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private CompetitionMapper competitionMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompetitionDTO> createCompetition(@RequestBody CompetitionDTO competitionDTO) {
        Competition competition = competitionService.create(competitionMapper.toEntity(competitionDTO));
        return new ResponseEntity<>(competitionMapper.toDTO(competition), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CompetitionDTO>> findAllCompetitions() {
        List<CompetitionDTO> competitions = competitionService.findAll().stream().map(competitionMapper::toDTO)
                .toList();
        return new ResponseEntity<>(competitions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDTO> findCompetitionById(@PathVariable UUID id) {
        Optional<Competition> optionalCompetition = competitionService.findById(id);
        if (optionalCompetition.isPresent()) {
            Competition competition = optionalCompetition.get();
            return new ResponseEntity<>(competitionMapper.toDTO(competition), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompetitionDTO> updateCompetition(@PathVariable UUID id, @RequestBody CompetitionDTO competitionDTO) {
        Optional<Competition> optionalCompetition = competitionService.findById(id);
        if (optionalCompetition.isPresent()) {
            Competition competition = competitionService.update(optionalCompetition.get(), competitionMapper.toEntity(competitionDTO));
            return new ResponseEntity<>(competitionMapper.toDTO(competition), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompetitionDTO> deleteCompetition(@PathVariable UUID id) {
        competitionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
