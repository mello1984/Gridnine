package com.gridnine.testing.streams;

import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class PredicateFlightFactory {
    public static Predicate<Flight> departureBeforeNow() {
        return flight -> flight
                .getSegments().stream()
                .anyMatch(s->s.getDepartureDate().isBefore(LocalDateTime.now()));
    }

    public static Predicate<Flight> arrivalBeforeDepartureInSegment() {
        return flight -> flight
                .getSegments().stream()
                .anyMatch(s -> s.getArrivalDate().isBefore(s.getDepartureDate()));
    }

    public static Predicate<Flight> totalDurationOnGroundMoreThan(Duration duration) {
        return flight -> {
            Duration total = Duration.ZERO;
            List<Segment> segments = flight.getSegments();
            for (int i = 0; i < segments.size() - 1; i++) {
                total = total.plus(Duration.between(segments.get(i).getArrivalDate(), segments.get(i + 1).getDepartureDate()));
            }
            return total.compareTo(duration) > 0;
        };
    }
}
