package ro.fasttrackit.curs7.homework.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.fasttrackit.curs7.homework.domain.Country;
import ro.fasttrackit.curs7.homework.domain.Restaurant;
import ro.fasttrackit.curs7.homework.exception.ValidationException;
import ro.fasttrackit.curs7.homework.repository.RestaurantRepository;

import java.util.Arrays;
import java.util.Optional;

import static java.time.LocalDate.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@RequiredArgsConstructor
public class RestaurantValidator {
    private final RestaurantRepository repository;
    private final Country country;

    public void validateNewThrow(Restaurant newRestaurant) {
        validate(newRestaurant, true)
                .ifPresent(validationException -> {
                    throw validationException;
                });
    }

    public void validateReplaceThrow(Long restaurantId, Restaurant newRestaurant) {
        exists(restaurantId)
                .or(() -> validate(newRestaurant, false))
                .ifPresent(validationException -> {
                    throw validationException;
                });
    }

    private Optional<ValidationException> validate(Restaurant restaurant, boolean newRestaurant) {
        if (restaurant.getName() == null) {
            return of(new ValidationException("Restaurant name cannot be null"));
        } else if (newRestaurant && repository.existsByName(restaurant.getName())) {
            return of(new ValidationException("Name cannot be duplicate"));
        } else if (!newRestaurant && repository.existsByNameAndIdNot(restaurant.getName(), restaurant.getId())) {
            return of(new ValidationException("Name cannot be duplicate"));
        } else if (!country.getMainCities().contains(restaurant.getCity())) {
            return of(new ValidationException("Restaurant must be from one of these cities: "
                    + Arrays.toString(country.getMainCities().toArray(new String[0]))));
        } else if (now().isBefore(restaurant.getSince())) {
            return of(new ValidationException("Since date cannot be before now for new restaurant"));
        } else {
            return empty();
        }
    }

    public void validateExistsOrThrow(Long restaurantId) {
        exists(restaurantId)
                .ifPresent(validationException -> {
                    throw validationException;
                });
    }

    private Optional<ValidationException> exists(Long restaurantId) {
        return repository.existsById(restaurantId)
                ? empty()
                : of(new ValidationException("Restaurant with id " + restaurantId + " doesn't exist."));
    }
}
