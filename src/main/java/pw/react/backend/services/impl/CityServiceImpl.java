package pw.react.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.models.City;
import pw.react.backend.models.Country;
import pw.react.backend.repository.CityRepository;
import pw.react.backend.services.CityService;
import pw.react.backend.services.CountryService;

import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryService countryService;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CountryService countryService) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
    }


    @Override
    public City getOrCreateCity(String cityName, String countryIso3166Name) {
        // Retrieve or create Country entity
        Country country = countryService.getOrCreateCountry(countryIso3166Name);

        // Try to find the existing City entity
        Optional<City> existingCity = cityRepository.findByNameAndCountry_Id(cityName, country.getId());

        return existingCity.orElseGet(() -> {
            // If not found, create a new City entity
            City newCity = new City();
            newCity.setName(cityName);
            newCity.setCountry(country);
            // Save the new City entity
            newCity = cityRepository.save(newCity);

            return newCity;
        });
    }

}
