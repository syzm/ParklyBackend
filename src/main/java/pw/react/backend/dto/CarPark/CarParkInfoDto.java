package pw.react.backend.dto.CarPark;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CarParkInfoDto {
    @NotNull
    private Long id;

    @NotBlank
    private String iso3166Country;

    @NotBlank
    private String cityName;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String streetName;

    @NotBlank
    private String buildingNumber;

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    @NotNull
    @DecimalMin("0.0")
    private double dailyCost;

    @NotNull
    private boolean isActive;
}
