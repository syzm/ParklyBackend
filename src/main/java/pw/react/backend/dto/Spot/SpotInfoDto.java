package pw.react.backend.dto.Spot;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotInfoDto {
    @NotNull
    private Long spotId;
    private String name;
}
