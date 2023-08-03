package co.com.perficient.project3.controller;

import co.com.perficient.project3.mapper.CoachMapper;
import co.com.perficient.project3.model.dto.CoachDTO;
import co.com.perficient.project3.model.entity.Coach;
import co.com.perficient.project3.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.CoachConstants.COACH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = COACH, produces = MediaType.APPLICATION_JSON_VALUE)
public class CoachController {

    @Autowired
    private CoachService coachService;
    @Autowired
    private CoachMapper coachMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoachDTO> createCoach(@RequestBody CoachDTO coachDTO) {
        Coach coach = coachService.create(coachMapper.toEntity(coachDTO));
        return new ResponseEntity<>(coachMapper.toDTO(coach), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CoachDTO>> findAllCoaches() {
        List<CoachDTO> coaches = coachService.findAll().stream()
                .map(coach -> EntityModel.of(coach, linkTo(methodOn(CoachController.class).findCoachById(coach.getId())).withSelfRel()))
                .map(coachMapper::toDTO).toList();
        return new ResponseEntity<>(coaches, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachDTO> findCoachById(@PathVariable UUID id) {
        Optional<Coach> optionalCoach = coachService.findById(id);
        if (optionalCoach.isPresent()) {
            ArrayList<Link> links = new ArrayList<>();
            links.add(linkTo(methodOn(CoachController.class).findAllCoaches()).withRel("allCoaches"));
            optionalCoach.map(Coach::getTeam)
                    .ifPresent(team -> links.add(linkTo(methodOn(TeamController.class).findTeamById(team.getId())).withRel("team")));

            EntityModel<Coach> coachEntityModel = EntityModel.of(optionalCoach.get(), links);
            return new ResponseEntity<>(coachMapper.toDTO(coachEntityModel), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoachDTO> updateCoach(@PathVariable UUID id, @RequestBody CoachDTO coachDTO) {
        Optional<Coach> optionalCoach = coachService.findById(id);
        if (optionalCoach.isPresent()) {
            Coach coach = coachService.update(optionalCoach.get(), coachMapper.toEntity(coachDTO));
            return new ResponseEntity<>(coachMapper.toDTO(coach), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CoachDTO> deleteCoach(@PathVariable UUID id) {
        coachService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
