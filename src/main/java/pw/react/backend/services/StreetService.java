package pw.react.backend.services;

import pw.react.backend.models.Street;

public interface StreetService {
    Street getOrCreateStreet(String streetName, String cityName,  String countryIso3166Name);
}
