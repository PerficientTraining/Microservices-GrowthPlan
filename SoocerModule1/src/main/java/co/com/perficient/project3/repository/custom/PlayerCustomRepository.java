package co.com.perficient.project3.repository.custom;

import co.com.perficient.project3.model.entity.Player;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface PlayerCustomRepository {
    List<Player> findAllByTeam(String teamName);
}
