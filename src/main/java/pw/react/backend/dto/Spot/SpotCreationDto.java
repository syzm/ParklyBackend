package pw.react.backend.dto.Spot;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotCreationDto {
    @NotNull
    private Long carParkId;
    private String name;
}
