package co.com.perficient.project3.mapper;

import co.com.perficient.project3.model.dto.PlayerDTO;
import co.com.perficient.project3.model.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.hateoas.EntityModel;

@Mapper(uses = TeamMapper.class)
public interface PlayerMapper {

    @Mapping(target = "team", source = "playerDTO.team", qualifiedByName = "settingTeam", conditionExpression = "java(java.util.Objects.nonNull(playerDTO.team()))")
    Player toEntity(PlayerDTO playerDTO);

    @Mapping(target = "team", source = "team.name")
    PlayerDTO toDTO(Player player);

    @Mapping(target = "name", source = "playerEntityModel.content.name")
    @Mapping(target = "nationality", source = "playerEntityModel.content.nationality")
    @Mapping(target = "birthDate", source = "playerEntityModel.content.birthDate")
    @Mapping(target = "number", source = "playerEntityModel.content.number")
    @Mapping(target = "position", source = "playerEntityModel.content.position")
    @Mapping(target = "team", source = "playerEntityModel.content.team.name")
    PlayerDTO toDTO(EntityModel<Player> playerEntityModel);
}
