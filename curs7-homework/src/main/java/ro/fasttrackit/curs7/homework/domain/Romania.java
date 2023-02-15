package ro.fasttrackit.curs7.homework.domain;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("romania")
public record Romania() implements Country {
    @Override
    public List<String> getMainCities() {
        return List.of(
                "Bors",
                "Cluj",
                "Timisoara",
                "Brasov",
                "Iasi",
                "Oradea",
                "Sighisoara",
                "Arad",
                "Zalau",
                "Galati",
                "Salonta"
        );
    }
}
