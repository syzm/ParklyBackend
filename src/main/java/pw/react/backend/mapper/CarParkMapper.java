package pw.react.backend.mapper;


import pw.react.backend.dto.CarPark.CarParkInfoDto;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.City;
import pw.react.backend.models.Street;

public class CarParkMapper {
    public static CarParkInfoDto mapToDto(CarPark carPark) {
        Street street = carPark.getStreet();
        City city = street.getCity();

        return new CarParkInfoDto(
                carPark.getId(),
                city.getCountry().getIso3166Name(),
                city.getName(),
                carPark.getPostalCode(),
                street.getName(),
                carPark.getBuildingNumber(),
                carPark.getLongitude(),
                carPark.getLatitude(),
                carPark.getDailyCost(),
                carPark.isActive()
        );
    }
}
