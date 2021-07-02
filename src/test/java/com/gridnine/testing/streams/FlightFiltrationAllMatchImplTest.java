package com.gridnine.testing.streams;

import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.Segment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class FlightFiltrationAllMatchImplTest {
    private final FlightFiltration filtration = new FlightFiltrationAllMatchImpl();

    private List<Flight> getFlightsList() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(5)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Flight flight1 = new Flight(List.of(segment1, segment2));

        Segment segment3 = new Segment(LocalDateTime.now().plus(Duration.ofHours(5)),
                LocalDateTime.now().plus(Duration.ofHours(10)));
        Flight flight2 = new Flight(List.of(segment3));
        return List.of(flight1, flight2);
    }

    @Test
    @DisplayName("return full list when predicate is true")
    void getFilteredFlightsList_whenPredicateTrue_thenReturnFullList() {
        List<Flight> expected = getFlightsList();
        Predicate<Flight> predicate = flight -> true;
        List<Flight> actual = filtration.getFilteredFlightsList(expected, predicate);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("return empty list when predicate is false")
    void getFilteredFlightsList_whenPredicateFalse_thenReturnEmptyList() {
        List<Flight> flights = getFlightsList();
        Predicate<Flight> predicate = flight -> false;
        List<Flight> actual = filtration.getFilteredFlightsList(flights, predicate);
        assertThat(actual).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("return full list when all predicates are true")
    void getFilteredFlightsList_whenAllPredicatesTrue_thenReturnFullList() {
        List<Flight> expected = getFlightsList();
        Predicate<Flight> predicate1 = flight -> true;
        Predicate<Flight> predicate2 = flight -> true;
        var predicates = List.of(predicate1, predicate2);
        List<Flight> actual = filtration.getFilteredFlightsList(expected, predicates);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("return empty list when all predicates are false")
    void getFilteredFlightsList_whenAllPredicatesFalse_thenReturnEmptyList() {
        List<Flight> flights = getFlightsList();
        Predicate<Flight> predicate1 = flight -> false;
        Predicate<Flight> predicate2 = flight -> false;
        var predicates = List.of(predicate1, predicate2);
        List<Flight> actual = filtration.getFilteredFlightsList(flights, predicates);
        assertThat(actual).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("return empty list when not all predicates are true")
    void getFilteredFlightsList_whenNotAllPredicatesTrue_thenReturnEmptyList() {
        List<Flight> flights = getFlightsList();
        Predicate<Flight> predicate1 = flight -> true;
        Predicate<Flight> predicate2 = flight -> false;
        var predicates = List.of(predicate1, predicate2);
        List<Flight> actual = filtration.getFilteredFlightsList(flights, predicates);
        assertThat(actual).isEqualTo(Collections.emptyList());
    }
}