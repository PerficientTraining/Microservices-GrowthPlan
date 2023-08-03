package co.com.perficient.project3.repository.custom.impl;

import co.com.perficient.project3.model.entity.Match;
import co.com.perficient.project3.model.entity.QMatch;
import co.com.perficient.project3.repository.custom.MatchCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchCustomRepositoryImpl implements MatchCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Match> findLast3Matches() {
        QMatch match = QMatch.match;
        JPAQuery<Match> query = new JPAQuery<>(entityManager);

        query.select(match).from(match).orderBy(match.date.desc());

        return query.limit(3).fetch();
    }
}
