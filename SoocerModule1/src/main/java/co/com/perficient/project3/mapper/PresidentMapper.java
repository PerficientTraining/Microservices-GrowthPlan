package co.com.perficient.project3.mapper;

import co.com.perficient.project3.model.dto.PresidentDTO;
import co.com.perficient.project3.model.entity.President;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.hateoas.EntityModel;

@Mapper(uses = TeamMapper.class)
public interface PresidentMapper {

    @Mapping(target = "team", source = "presidentDTO.team", qualifiedByName = "settingTeam", conditionExpression = "java(java.util.Objects.nonNull(presidentDTO.team()))")
    President toEntity(PresidentDTO presidentDTO);

    @Mapping(target = "team", source = "team.name")
    PresidentDTO toDTO(President president);

    @Mapping(target = "name", source = "presidentEntityModel.content.name")
    @Mapping(target = "nationality", source = "presidentEntityModel.content.nationality")
    @Mapping(target = "birthDate", source = "presidentEntityModel.content.birthDate")
    @Mapping(target = "team", source = "presidentEntityModel.content.team.name")
    PresidentDTO toDTO(EntityModel<President> presidentEntityModel);
}
