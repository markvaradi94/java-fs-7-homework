package ro.fasttrackit.curs7.homework.domain;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("hungary")
public record Hungary() implements Country {
    @Override
    public List<String> getMainCities() {
        return List.of(
                "Budapest",
                "Debrecen",
                "Gyor",
                "Sopron",
                "Nyiregyhaza"
        );
    }
}
