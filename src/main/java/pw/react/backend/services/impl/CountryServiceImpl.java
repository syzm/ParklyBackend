package pw.react.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.models.Country;
import pw.react.backend.repository.CountryRepository;
import pw.react.backend.services.CountryService;

import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService
{
    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    @Override
    public Country getOrCreateCountry(String countryIso3166Name) {
        // Try to find the existing Country entity
        Optional<Country> existingCountry = countryRepository.findByIso3166Name(countryIso3166Name);

        return existingCountry.orElseGet(() -> {
            // If not found, create a new Country entity
            Country newCountry = new Country();
            newCountry.setIso3166Name(countryIso3166Name);

            newCountry = countryRepository.save(newCountry);
            return newCountry;
        });
    }
}
