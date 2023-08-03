package co.com.perficient.project3.service;

import co.com.perficient.project3.model.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerService extends CrudService<Player, UUID> {

    List<Player> findAllByTeam(String team);
}
