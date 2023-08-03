package co.com.perficient.project3.controller;

import co.com.perficient.project3.mapper.StadiumMapper;
import co.com.perficient.project3.model.dto.StadiumDTO;
import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.Constants.COUNTRY;
import static co.com.perficient.project3.utils.constant.StadiumConstants.STADIUM;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = STADIUM, produces = MediaType.APPLICATION_JSON_VALUE)
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private StadiumMapper stadiumMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StadiumDTO> createStadium(@RequestBody StadiumDTO stadiumDTO) {
        Stadium stadium = stadiumService.create(stadiumMapper.toEntity(stadiumDTO));
        return new ResponseEntity<>(stadiumMapper.toDTO(stadium), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StadiumDTO>> findAllStadiums() {
        List<StadiumDTO> stadiums = stadiumService.findAll().stream()
                .map(stadium -> stadium.add(linkTo(methodOn(StadiumController.class).findStadiumById(stadium.getId())).withSelfRel()))
                .map(stadiumMapper::toDTO).toList();
        return new ResponseEntity<>(stadiums, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumDTO> findStadiumById(@PathVariable UUID id) {
        Optional<Stadium> optionalStadium = stadiumService.findById(id);
        if (optionalStadium.isPresent()) {
            Stadium stadium = optionalStadium.get();
            stadium.add(linkTo(methodOn(StadiumController.class).findAllStadiums()).withRel("allStadiums"));
            return new ResponseEntity<>(stadiumMapper.toDTO(stadium), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StadiumDTO> updateStadium(@PathVariable UUID id, @RequestBody StadiumDTO stadiumDTO) {
        Optional<Stadium> optionalStadium = stadiumService.findById(id);
        if (optionalStadium.isPresent()) {
            Stadium stadium = stadiumService.update(optionalStadium.get(), stadiumMapper.toEntity(stadiumDTO));
            return new ResponseEntity<>(stadiumMapper.toDTO(stadium), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StadiumDTO> deleteStadium(@PathVariable UUID id) {
        stadiumService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StadiumDTO> patchStadium(@PathVariable UUID id, @RequestBody Map<String, Object> fields) {
        Optional<Stadium> optionalStadium = stadiumService.findById(id);
        if (optionalStadium.isPresent()) {
            Stadium stadium = stadiumService.patch(optionalStadium.get(), fields);
            return new ResponseEntity<>(stadiumMapper.toDTO(stadium), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(COUNTRY + "/{country}")
    public ResponseEntity<List<StadiumDTO>> findStadiumsByCountry(@PathVariable String country) {
        List<StadiumDTO> stadiums = stadiumService.findAllByCountry(country).map(stadiumMapper::toDTO).toList();
        return new ResponseEntity<>(stadiums, HttpStatus.OK);
    }
}
