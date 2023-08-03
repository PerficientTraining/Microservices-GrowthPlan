package co.com.perficient.project3.controller;

import co.com.perficient.project3.mapper.MatchMapper;
import co.com.perficient.project3.model.dto.MatchDTO;
import co.com.perficient.project3.model.entity.Match;
import co.com.perficient.project3.service.MatchService;
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
import java.util.Optional;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.Constants.LAST;
import static co.com.perficient.project3.utils.constant.MatchConstants.MATCH;

@RestController
@RequestMapping(value = MATCH, produces = MediaType.APPLICATION_JSON_VALUE)
public class MatchController {

    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchMapper matchMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO matchDTO, @RequestParam String competitionName) {
        Match match = matchService.create(matchMapper.toEntity(matchDTO), competitionName);
        return new ResponseEntity<>(matchMapper.toDTO(match), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MatchDTO>> findAllMatches() {
        List<MatchDTO> matches = matchService.findAll().stream().map(matchMapper::toDTO).toList();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> findMatchById(@PathVariable UUID id) {
        Optional<Match> optionalMatch = matchService.findById(id);
        return optionalMatch.map(match -> new ResponseEntity<>(matchMapper.toDTO(match), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MatchDTO> updateMatch(@PathVariable UUID id, @RequestBody MatchDTO matchDTO, @RequestParam String competitionName) {
        Optional<Match> optionalMatch = matchService.findById(id);
        if (optionalMatch.isPresent()) {
            Match match = matchService.update(optionalMatch.get(), matchMapper.toEntity(matchDTO), competitionName);
            return new ResponseEntity<>(matchMapper.toDTO(match), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MatchDTO> deleteMatch(@PathVariable UUID id) {
        matchService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(LAST)
    public ResponseEntity<List<MatchDTO>> findLast3Matches() {
        List<MatchDTO> recentMatches = matchService.findLast3Matches().stream().map(matchMapper::toDTO).toList();
        return new ResponseEntity<>(recentMatches, HttpStatus.OK);
    }
}
