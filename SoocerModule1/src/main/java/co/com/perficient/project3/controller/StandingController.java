package co.com.perficient.project3.controller;

import co.com.perficient.project3.mapper.StandingMapper;
import co.com.perficient.project3.model.dto.StandingDTO;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.service.StandingService;
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

import static co.com.perficient.project3.utils.constant.StandingConstants.STANDING;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = STANDING, produces = MediaType.APPLICATION_JSON_VALUE)
public class StandingController {

    @Autowired
    private StandingService standingService;
    @Autowired
    private StandingMapper standingMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandingDTO> createStanding(@RequestBody StandingDTO standingDTO) {
        Standing standing = standingService.create(standingMapper.toEntity(standingDTO));
        return new ResponseEntity<>(standingMapper.toDTO(standing), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StandingDTO>> findAllStandings() {
        List<StandingDTO> standings = standingService.findAll().stream()
                .map(standing -> standing.add(linkTo(methodOn(StandingController.class).findStandingById(standing.getId())).withSelfRel()))
                .map(standingMapper::toDTO).toList();
        return new ResponseEntity<>(standings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandingDTO> findStandingById(@PathVariable UUID id) {
        Optional<Standing> optionalStanding = standingService.findById(id);
        if (optionalStanding.isPresent()) {
            Standing standing = optionalStanding.get();
            standing.add(linkTo(methodOn(StandingController.class).findAllStandings()).withRel("allStandings"));
            return new ResponseEntity<>(standingMapper.toDTO(standing), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandingDTO> updateStanding(@PathVariable UUID id, @RequestBody StandingDTO standingDTO) {
        Optional<Standing> optionalStanding = standingService.findById(id);
        if (optionalStanding.isPresent()) {
            Standing standing = standingService.update(optionalStanding.get(), standingMapper.toEntity(standingDTO));
            return new ResponseEntity<>(standingMapper.toDTO(standing), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandingDTO> deleteStanding(@PathVariable UUID id) {
        standingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
