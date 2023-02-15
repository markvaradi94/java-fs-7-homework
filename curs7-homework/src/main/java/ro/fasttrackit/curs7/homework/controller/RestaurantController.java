package ro.fasttrackit.curs7.homework.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.curs7.homework.domain.Restaurant;
import ro.fasttrackit.curs7.homework.domain.RestaurantFilters;
import ro.fasttrackit.curs7.homework.exception.ResourceNotFoundException;
import ro.fasttrackit.curs7.homework.model.CollectionResponse;
import ro.fasttrackit.curs7.homework.model.PageInfo;
import ro.fasttrackit.curs7.homework.service.RestaurantService;

@RestController
@RequestMapping("restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

    @GetMapping
    CollectionResponse<Restaurant> getAll(RestaurantFilters filters, Pageable pageable) {
        Page<Restaurant> restaurantPage = service.getAll(filters, pageable);
        return CollectionResponse.<Restaurant>builder()
                .content(restaurantPage.getContent())
                .pageInfo(PageInfo.builder()
                        .totalPages(restaurantPage.getTotalPages())
                        .totalElements(restaurantPage.getNumberOfElements())
                        .crtPage(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .build())
                .build();
    }

    @GetMapping("{restaurantId}")
    Restaurant getRestaurant(@PathVariable Long restaurantId) {
        return service.findRestaurantById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find restaurant with id " + restaurantId));
    }

    @PostMapping
    Restaurant addRestaurant(@RequestBody Restaurant newRestaurant) {
        return service.addRestaurant(newRestaurant);
    }

    @DeleteMapping("{restaurantId}")
    Restaurant deleteRestaurant(@PathVariable Long restaurantId) {
        return service.deleteRestaurant(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find restaurant with id " + restaurantId));
    }

    @PutMapping("{restaurantId}")
    Restaurant replaceRestaurant(@RequestBody Restaurant newRestaurant, @PathVariable Long restaurantId) {
        return service.replaceRestaurant(restaurantId, newRestaurant);
    }

    @PatchMapping("{restaurantId}")
    Restaurant patchRestaurant(@RequestBody JsonPatch patch, @PathVariable Long restaurantId) {
        return service.patchRestaurant(restaurantId, patch);
    }
}
