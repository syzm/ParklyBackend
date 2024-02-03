package pw.react.backend.mapper;

import pw.react.backend.dto.Spot.SpotInfoDto;
import pw.react.backend.models.Spot;

public class SpotMapper {
    public static SpotInfoDto mapToSpotInfoDto(Spot spot) {
        SpotInfoDto spotInfoDto = new SpotInfoDto();
        spotInfoDto.setSpotId(spot.getId());
        spotInfoDto.setName(spot.getName());

        return spotInfoDto;
    }

}
