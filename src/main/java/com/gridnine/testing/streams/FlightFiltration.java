package com.gridnine.testing.streams;

import com.gridnine.testing.domain.Flight;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface FlightFiltration {

    List<Flight> getFilteredFlightsList(Collection<Flight> flights, Collection<Predicate<Flight>> filter);

    List<Flight> getFilteredFlightsList(Collection<Flight> flights, Predicate<Flight> filter);
}
