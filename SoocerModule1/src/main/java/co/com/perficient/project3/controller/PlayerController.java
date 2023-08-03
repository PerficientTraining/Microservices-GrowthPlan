package co.com.perficient.project3.controller;

import co.com.perficient.project3.mapper.PlayerMapper;
import co.com.perficient.project3.model.dto.PlayerDTO;
import co.com.perficient.project3.model.entity.Player;
import co.com.perficient.project3.service.PlayerService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.PlayerConstants.PLAYER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = PLAYER, produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerMapper playerMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerService.create(playerMapper.toEntity(playerDTO));
        return new ResponseEntity<>(playerMapper.toDTO(player), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> findAllPlayers(@RequestParam(required = false) String team) {
        List<Player> players;
        if (Objects.isNull(team)) {
            players = playerService.findAll();
        } else players = playerService.findAllByTeam(team);
        List<PlayerDTO> playersDTO = players.stream()
                .map(player -> EntityModel.of(player, linkTo(methodOn(PlayerController.class).findPlayerById(player.getId())).withSelfRel()))
                .map(playerMapper::toDTO).toList();
        return new ResponseEntity<>(playersDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> findPlayerById(@PathVariable UUID id) {
        Optional<Player> optionalPlayer = playerService.findById(id);
        if (optionalPlayer.isPresent()) {
            List<Link> links = new ArrayList<>();
            links.add(linkTo(PlayerController.class).withRel("allPlayers"));
            optionalPlayer.map(Player::getTeam)
                    .ifPresent(team -> links.add(linkTo(methodOn(TeamController.class).findTeamById(team.getId())).withRel("team")));

            EntityModel<Player> playerEntityModel = EntityModel.of(optionalPlayer.get(), links);
            return new ResponseEntity<>(playerMapper.toDTO(playerEntityModel), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable UUID id, @RequestBody PlayerDTO playerDTO) {
        Optional<Player> optionalPlayer = playerService.findById(id);
        if (optionalPlayer.isPresent()) {
            Player player = playerService.update(optionalPlayer.get(), playerMapper.toEntity(playerDTO));
            return new ResponseEntity<>(playerMapper.toDTO(player), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerDTO> deletePlayerById(@PathVariable UUID id) {
        playerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
