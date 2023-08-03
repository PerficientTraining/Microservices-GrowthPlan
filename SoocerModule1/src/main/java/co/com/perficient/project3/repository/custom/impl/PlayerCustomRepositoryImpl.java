package co.com.perficient.project3.repository.custom.impl;

import co.com.perficient.project3.model.entity.Player;
import co.com.perficient.project3.model.entity.QPlayer;
import co.com.perficient.project3.repository.custom.PlayerCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerCustomRepositoryImpl implements PlayerCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Player> findAllByTeam(String teamName) {
        QPlayer player = QPlayer.player;
        JPAQuery<Player> query = new JPAQuery<>(entityManager);

        query.select(player).from(player).where(player.team.name.equalsIgnoreCase(teamName));

        return query.fetch();
    }
}
