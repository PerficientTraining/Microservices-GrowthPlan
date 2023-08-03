package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.model.entity.Player;
import co.com.perficient.project3.repository.PlayerRepository;
import co.com.perficient.project3.repository.custom.PlayerCustomRepository;
import co.com.perficient.project3.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerCustomRepository playerCustomRepository;

    @Override
    public Player create(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> findAllByTeam(String team) {
        return playerCustomRepository.findAllByTeam(team);
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return playerRepository.findById(id);
    }

    @Override
    public Player update(Player oldPlayer, Player newPlayer) {
        oldPlayer.setName(newPlayer.getName());
        oldPlayer.setNationality(newPlayer.getNationality());
        oldPlayer.setBirthDate(newPlayer.getBirthDate());
        oldPlayer.setNumber(newPlayer.getNumber());
        oldPlayer.setPosition(newPlayer.getPosition());
        oldPlayer.setTeam(newPlayer.getTeam());
        return playerRepository.saveAndFlush(oldPlayer);
    }

    @Override
    public void delete(UUID id) {
        playerRepository.deleteById(id);
    }
}
