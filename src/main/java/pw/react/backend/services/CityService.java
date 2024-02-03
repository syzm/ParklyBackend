package pw.react.backend.services;

import pw.react.backend.models.City;

public interface CityService {
    City getOrCreateCity(String cityName,  String countryIso3166Name);
}
