package ro.fasttrackit.curs7.homework.domain;

import java.util.List;

public record RestaurantFilters(
        List<Integer> stars,
        String city
) {
}
