package pw.react.backend.services;

import pw.react.backend.models.Country;

public interface CountryService {
    Country getOrCreateCountry(String countryIso3166Name);
}
