package pw.react.backend.dto.Reservation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pw.react.backend.models.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfoDto {
    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long carParkId;

    private String spotName;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

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
    @DecimalMin("0.0")
    private double cost;

    @NotBlank
    private String reservationStatus;

    private Long externalUserId;

    public ReservationInfoDto(Reservation reservation) {
        this.id = reservation.getId();
        this.userId = reservation.getUser().getId();
        Spot spot = reservation.getSpot();
        CarPark carPark = spot.getCarPark();
        Street street = carPark.getStreet();
        City city = street.getCity();
        Country country = city.getCountry();
        this.carParkId = carPark.getId();
        this.spotName = spot.getName();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.iso3166Country = country.getIso3166Name();
        this.cityName = city.getName();
        this.postalCode = carPark.getPostalCode();
        this.streetName = street.getName();
        this.buildingNumber = carPark.getBuildingNumber();
        this.cost = reservation.getCost();
        this.reservationStatus = reservation.getStatus().toString();
        this.externalUserId = reservation.getExternalUserId();
    }
}
