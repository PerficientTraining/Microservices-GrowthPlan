package co.com.perficient.project3.mapper;

import co.com.perficient.project3.model.dto.CompetitionDTO;
import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.service.CompetitionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

@Mapper
public abstract class CompetitionMapper {

    @Autowired
    private CompetitionService competitionService;

    @Mapping(target = "standings", ignore = true)
    public abstract Competition toEntity(CompetitionDTO competitionDTO);

    public abstract CompetitionDTO toDTO(Competition competition);

    @Named("settingCompetition")
    protected Competition settingCompetition(String competitionName) {
        return competitionService.findByName(competitionName)
                .orElseThrow(() -> new NoSuchElementException(String.format("No value present for competition: %s", competitionName)));
    }
}
