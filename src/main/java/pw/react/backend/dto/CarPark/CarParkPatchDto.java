package pw.react.backend.dto.CarPark;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarParkPatchDto {

    @NotNull
    @DecimalMin("0.0")
    private double dailyCost;

    @NotNull
    private boolean isActive;
}
