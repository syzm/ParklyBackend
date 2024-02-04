package pw.react.backend.mapper;


import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.models.Reservation;
import pw.react.backend.models.User;
import pw.react.backend.models.Spot;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.Street;
import pw.react.backend.models.City;
import pw.react.backend.models.Country;

public class ReservationMapper {
    public static ReservationInfoDto mapToDto(Reservation reservation) {
        User user = reservation.getUser();
        Spot spot = reservation.getSpot();
        CarPark carPark = spot.getCarPark();
        Street street = carPark.getStreet();
        City city = street.getCity();
        Country country = city.getCountry();

        return new ReservationInfoDto(
                reservation.getId(),
                user.getId(),
                carPark.getId(),
                spot.getName(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                country.getIso3166Name(),
                city.getName(),
                carPark.getPostalCode(),
                street.getName(),
                carPark.getBuildingNumber(),
                carPark.getDailyCost(),
                reservation.getStatus().name(),
                reservation.getExternalUserId()
        );
    }
}
