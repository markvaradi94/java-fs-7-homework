package ro.fasttrackit.curs7.homework.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fasttrackit.curs7.homework.domain.Restaurant;
import ro.fasttrackit.curs7.homework.domain.RestaurantFilters;
import ro.fasttrackit.curs7.homework.exception.ResourceNotFoundException;
import ro.fasttrackit.curs7.homework.repository.RestaurantRepository;
import ro.fasttrackit.curs7.homework.service.validator.RestaurantValidator;

import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;
    private final RestaurantValidator validator;
    private final ObjectMapper mapper;

    public Page<Restaurant> getAll(RestaurantFilters filters, Pageable pageable) {
        if (!isEmpty(filters.stars()) && filters.city() != null) {
            return repository.findByStarsInAndCityIgnoreCase(filters.stars(), filters.city(), pageable);
        } else if (!isEmpty(filters.stars())) {
            return repository.findByStarsIn(filters.stars(), pageable);
        } else if (filters.city() != null) {
            return repository.findByCityIgnoreCase(filters.city(), pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public Optional<Restaurant> findRestaurantById(Long restaurantId) {
        return repository.findById(restaurantId);
    }

    public Restaurant addRestaurant(Restaurant newRestaurant) {
        validator.validateNewThrow(newRestaurant);
        return repository.save(newRestaurant);
    }

    public Optional<Restaurant> deleteRestaurant(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = repository.findById(restaurantId);
        restaurantOptional.ifPresent(repository::delete);
        return restaurantOptional;
    }

    public Restaurant replaceRestaurant(Long restaurantId, Restaurant newRestaurant) {
        newRestaurant.setId(restaurantId);
        validator.validateReplaceThrow(restaurantId, newRestaurant);
        Restaurant restaurantToReplace = getOrThrow(restaurantId);
        copyRestaurant(newRestaurant, restaurantToReplace);
        return repository.save(restaurantToReplace);
    }

    @SneakyThrows
    public Restaurant patchRestaurant(Long restaurantId, JsonPatch patch) {
        validator.validateExistsOrThrow(restaurantId);
        Restaurant restaurantToPatch = getOrThrow(restaurantId);
        JsonNode patchedRestaurantJson = patch.apply(mapper.valueToTree(restaurantToPatch));
        Restaurant patchedRestaurant = mapper.treeToValue(patchedRestaurantJson, Restaurant.class);
        return replaceRestaurant(restaurantId, patchedRestaurant);
    }

    private Restaurant getOrThrow(Long restaurantId) {
        return repository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find restaurant with id " + restaurantId));
    }

    private void copyRestaurant(Restaurant newRestaurant, Restaurant dbRestaurant) {
        dbRestaurant.setName(newRestaurant.getName());
        dbRestaurant.setCity(newRestaurant.getCity());
        dbRestaurant.setStars(newRestaurant.getStars());
        dbRestaurant.setSince(newRestaurant.getSince());
    }
}
