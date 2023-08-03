package co.com.perficient.project3.mapper;

import co.com.perficient.project3.model.dto.CoachDTO;
import co.com.perficient.project3.model.entity.Coach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.hateoas.EntityModel;

@Mapper(uses = TeamMapper.class)
public interface CoachMapper {

    @Mapping(target = "team", source = "coachDTO.team", qualifiedByName = "settingTeam", conditionExpression = "java(java.util.Objects.nonNull(coachDTO.team()))")
    Coach toEntity(CoachDTO coachDTO);

    @Mapping(target = "team", source = "team.name")
    CoachDTO toDTO(Coach coach);

    @Mapping(target = "name", source = "coachEntityModel.content.name")
    @Mapping(target = "nationality", source = "coachEntityModel.content.nationality")
    @Mapping(target = "birthDate", source = "coachEntityModel.content.birthDate")
    @Mapping(target = "team", source = "coachEntityModel.content.team.name")
    CoachDTO toDTO(EntityModel<Coach> coachEntityModel);
}
