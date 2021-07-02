package com.gridnine.testing.chain_of_responsibility;

import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.Segment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExcludeDepartureBeforeNowChainFilterImplTest {

    @Test
    @DisplayName("exclude flight when segment departure before now")
    void departureBeforeNowChainFilter_whenSegmentDepartureBeforeNow_thenExcludeFlight() {
        var filter = new ExcludeDepartureBeforeNowChainFilterImpl();
        Segment incorrect1 = new Segment(LocalDateTime.now().minus(Duration.ofMinutes(25)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment correct1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(45)),
                LocalDateTime.now().plus(Duration.ofMinutes(50)));
        Segment incorrect2 = new Segment(LocalDateTime.now().minus(Duration.ofHours(1)),
                LocalDateTime.now().minus(Duration.ofMinutes(55)));
        Segment correct2 = new Segment(LocalDateTime.now().plus(Duration.ofHours(2)),
                LocalDateTime.now().plus(Duration.ofHours(3)));
        Flight flight1 = new Flight(List.of(incorrect1, correct1));
        Flight flight2 = new Flight(List.of(incorrect1));
        Flight flight3 = new Flight(List.of(correct1));
        Flight flight4 = new Flight(List.of(incorrect1, correct1, incorrect2));
        Flight flight5 = new Flight(List.of(incorrect1, correct1, incorrect2, correct2));
        Flight flight6 = new Flight(List.of(incorrect1, correct1, correct2));
        Flight flight7 = new Flight(List.of(correct1, correct2));

        var flights = List.of(flight1, flight2, flight3, flight4, flight5, flight6, flight7);
        var expected = List.of(flight3, flight7);
        var actual = filter.getFilteredList(flights);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("return empty list when list empty")
    void departureBeforeNowChainFilter_whenEmptyList_thenReturnEmptyList() {
        var filter = new ExcludeDepartureBeforeNowChainFilterImpl();

        List<Flight> expected = Collections.emptyList();
        var actual = filter.getFilteredList(expected);
        assertThat(actual).isEqualTo(expected);
    }
}