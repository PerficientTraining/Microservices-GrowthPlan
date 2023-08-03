package co.com.perficient.project3.mapper;

import co.com.perficient.project3.model.dto.StadiumDTO;
import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.service.StadiumService;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

@Mapper
public abstract class StadiumMapper {

    @Autowired
    protected StadiumService stadiumService;

    public abstract Stadium toEntity(StadiumDTO stadiumDTO);

    public abstract StadiumDTO toDTO(Stadium stadium);

    @Named("settingStadium")
    protected Stadium settingStadium(String stadiumName) {
        return stadiumService.findByName(stadiumName)
                .orElseThrow(() -> new NoSuchElementException(String.format("No value present for stadium: %s", stadiumName)));
    }
}
