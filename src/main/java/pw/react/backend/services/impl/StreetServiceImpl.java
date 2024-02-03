package pw.react.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.models.City;
import pw.react.backend.models.Street;
import pw.react.backend.repository.StreetRepository;
import pw.react.backend.services.CityService;
import pw.react.backend.services.StreetService;

import java.util.Optional;

@Service
public class StreetServiceImpl implements StreetService {

    private final StreetRepository streetRepository;
    private final CityService cityService;

    @Autowired
    public StreetServiceImpl(StreetRepository streetRepository, CityService cityService) {
        this.streetRepository = streetRepository;
        this.cityService = cityService;
    }


    @Override
    public Street getOrCreateStreet(String streetName, String cityName, String countryIso3166Name) {
        // Find existing City entity or create one
        City existingCity = cityService.getOrCreateCity(cityName, countryIso3166Name);

        // Try to find existing street
        Optional<Street> existingStreet = streetRepository.findByNameAndCity_Id(streetName, existingCity.getId());

        return existingStreet.orElseGet(() -> {
            // If not found, create a new Street entity
            Street newStreet = new Street();
            newStreet.setName(streetName);
            newStreet.setCity(existingCity);
            // Save the new Street entity
            newStreet = streetRepository.save(newStreet);
            return newStreet;
        });

    }
}
