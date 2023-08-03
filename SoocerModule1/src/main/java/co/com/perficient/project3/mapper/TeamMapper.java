package co.com.perficient.project3.mapper;

import co.com.perficient.project3.model.dto.TeamDTO;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.service.TeamService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

@Mapper(uses = StadiumMapper.class)
public abstract class TeamMapper {

    @Autowired
    protected TeamService teamService;

    @Mapping(target = "stadium", source = "teamDTO.stadium", qualifiedByName = "settingStadium", conditionExpression = "java(java.util.Objects.nonNull(teamDTO.stadium()))")
    @Mapping(target = "president", ignore = true)
    @Mapping(target = "coach", ignore = true)
    public abstract Team toEntity(TeamDTO teamDTO);

    @Mapping(target = "stadium", source = "team.stadium.name")
    @Mapping(target = "president", source = "team.president.name")
    @Mapping(target = "coach", source = "team.coach.name")
    public abstract TeamDTO toDTO(Team team);

    @Named("settingTeam")
    protected Team settingTeam(String teamName) {
        return teamService.findByName(teamName)
                .orElseThrow(() -> new NoSuchElementException(String.format("No value present for team: %s", teamName)));
    }
}
