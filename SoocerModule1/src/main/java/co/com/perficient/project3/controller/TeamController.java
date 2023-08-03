package co.com.perficient.project3.controller;

import co.com.perficient.project3.mapper.TeamMapper;
import co.com.perficient.project3.model.dto.TeamDTO;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.service.TeamService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.Constants.COUNTRY;
import static co.com.perficient.project3.utils.constant.TeamConstants.TEAM;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = TEAM, produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamMapper teamMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        Team team = teamService.create(teamMapper.toEntity(teamDTO));
        return new ResponseEntity<>(teamMapper.toDTO(team), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> findAllTeams() {
        List<TeamDTO> teams = teamService.findAll().stream()
                .map(team -> team.add(linkTo(methodOn(TeamController.class).findTeamById(team.getId())).withSelfRel()))
                .map(teamMapper::toDTO).toList();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> findTeamById(@PathVariable UUID id) {
        Optional<Team> optionalTeam = teamService.findById(id);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            team.add(linkTo(methodOn(TeamController.class).findAllTeams()).withRel("allTeams"));
            if (Objects.nonNull(team.getStadium())) {
                team.add(linkTo(methodOn(StadiumController.class).findStadiumById(team.getStadium()
                        .getId())).withRel("stadium"));
            }
            if (Objects.nonNull(team.getPresident())) {
                team.add(linkTo(methodOn(PresidentController.class).findPresidentById(team.getPresident()
                        .getId())).withRel("president"));
            }
            if (Objects.nonNull(team.getCoach())) {
                team.add(linkTo(methodOn(CoachController.class).findCoachById(team.getCoach()
                        .getId())).withRel("coach"));
            }
            team.add(linkTo(methodOn(PlayerController.class).findAllPlayers(team.getName())).withRel("players"));
            return new ResponseEntity<>(teamMapper.toDTO(team), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable UUID id, @RequestBody TeamDTO teamDTO) {
        Optional<Team> optionalTeam = teamService.findById(id);
        if (optionalTeam.isPresent()) {
            Team team = teamService.update(optionalTeam.get(), teamMapper.toEntity(teamDTO));
            return new ResponseEntity<>(teamMapper.toDTO(team), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TeamDTO> deleteTeam(@PathVariable UUID id) {
        teamService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(COUNTRY + "/{country}")
    public ResponseEntity<List<TeamDTO>> findTeamsByCountry(@PathVariable String country, @RequestParam(defaultValue = "5") Integer size) {
        List<TeamDTO> teams = teamService.findAllByCountry(country, size).stream().map(teamMapper::toDTO).toList();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
}
