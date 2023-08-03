package co.com.perficient.project3.mapper;

import co.com.perficient.project3.model.dto.MatchDTO;
import co.com.perficient.project3.model.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {StadiumMapper.class, TeamMapper.class})
public interface MatchMapper {

    @Mapping(target = "stadium", source = "matchDTO.stadium", qualifiedByName = "settingStadium", conditionExpression = "java(java.util.Objects.nonNull(matchDTO.stadium()))")
    @Mapping(target = "homeTeam", source = "matchDTO.homeTeam", qualifiedByName = "settingTeam", conditionExpression = "java(java.util.Objects.nonNull(matchDTO.homeTeam()))")
    @Mapping(target = "awayTeam", source = "matchDTO.awayTeam", qualifiedByName = "settingTeam", conditionExpression = "java(java.util.Objects.nonNull(matchDTO.awayTeam()))")
    Match toEntity(MatchDTO matchDTO);

    @Mapping(target = "stadium", source = "stadium.name")
    @Mapping(target = "homeTeam", source = "homeTeam.name")
    @Mapping(target = "awayTeam", source = "awayTeam.name")
    MatchDTO toDTO(Match match);
}
