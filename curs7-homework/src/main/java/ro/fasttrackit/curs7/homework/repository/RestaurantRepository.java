package ro.fasttrackit.curs7.homework.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.curs7.homework.domain.Restaurant;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<Restaurant> findByCityIgnoreCase(String city, Pageable pageable);

    Page<Restaurant> findByStarsInAndCityIgnoreCase(List<Integer> stars, String city, Pageable pageable);

    Page<Restaurant> findByStarsIn(List<Integer> starts, Pageable pageable);
}
