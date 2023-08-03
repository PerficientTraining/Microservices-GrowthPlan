package co.com.perficient.project3.repository.custom;

import co.com.perficient.project3.model.entity.Match;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface MatchCustomRepository {

    List<Match> findLast3Matches();
}
