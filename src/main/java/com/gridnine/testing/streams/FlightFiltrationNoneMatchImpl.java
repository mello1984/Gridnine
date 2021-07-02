package com.gridnine.testing.streams;

import com.gridnine.testing.domain.Flight;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlightFiltrationNoneMatchImpl implements FlightFiltration {

    @Override
    public List<Flight> getFilteredFlightsList(Collection<Flight> flights, Predicate<Flight> filter) {
        return flights.stream().filter(filter.negate()).collect(Collectors.toList());
    }

    @Override
    public List<Flight> getFilteredFlightsList(Collection<Flight> flights, Collection<Predicate<Flight>> filters) {
        Predicate<Flight> predicate = filters.stream().map(Predicate::negate).reduce(Predicate::and).orElse(x -> true);
        return getFilteredFlightsList(flights, predicate.negate());
    }
}
