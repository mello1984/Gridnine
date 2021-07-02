package com.gridnine.testing;

import com.gridnine.testing.chain_of_responsibility.ChainFilter;
import com.gridnine.testing.chain_of_responsibility.ExcludeArrivalBeforeDepartureInSegmentChainFilterImpl;
import com.gridnine.testing.chain_of_responsibility.ExcludeDepartureBeforeNowChainFilterImpl;
import com.gridnine.testing.chain_of_responsibility.ExcludeTotalDurationOnGroundMostThanImpl;
import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.FlightBuilder;
import com.gridnine.testing.streams.FlightFiltration;
import com.gridnine.testing.streams.FlightFiltrationNoneMatchImpl;
import com.gridnine.testing.streams.PredicateFlightFactory;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        printFlightsList(flights, "Original list");


        // Streams realisation
        System.out.println("Streams realisation");

        FlightFiltration filtration = new FlightFiltrationNoneMatchImpl();
        printFlightsList(filtration.getFilteredFlightsList(flights, PredicateFlightFactory.departureBeforeNow()), "Excluded flights with departure before now");
        printFlightsList(filtration.getFilteredFlightsList(flights, PredicateFlightFactory.arrivalBeforeDepartureInSegment()), "Excluded flights with arrival before departure in segment");
        printFlightsList(filtration.getFilteredFlightsList(flights, PredicateFlightFactory.totalDurationOnGroundMoreThan(Duration.ofHours(2))), "Excluded flights with total duration on the ground more than 2 hour");


        // Chain of responsibility realisation
        System.out.println("Chain of responsibility realisation");

        ChainFilter chainFilter1 = new ExcludeDepartureBeforeNowChainFilterImpl();
        printFlightsList(chainFilter1.getFilteredFlightsList(flights), "Excluded flights with departure before now");

        ChainFilter chainFilter2 = new ExcludeArrivalBeforeDepartureInSegmentChainFilterImpl();
        printFlightsList(chainFilter2.getFilteredFlightsList(flights), "Excluded flights with arrival before departure in segment");

        ChainFilter chainFilter3 = new ExcludeTotalDurationOnGroundMostThanImpl(Duration.ofHours(2));
        printFlightsList(chainFilter3.getFilteredFlightsList(flights), "Excluded flights with total duration on the ground more than 2 hour");


        // Many filters implementation
        List<Predicate<Flight>> filters = List.of(
                PredicateFlightFactory.departureBeforeNow(),
                PredicateFlightFactory.arrivalBeforeDepartureInSegment(),
                PredicateFlightFactory.totalDurationOnGroundMoreThan(Duration.ofHours(2)));
        printFlightsList(filtration.getFilteredFlightsList(flights, filters), "Stream implementation all filters");

        ChainFilter allChainFilter = new ExcludeDepartureBeforeNowChainFilterImpl();
        allChainFilter
                .linkWith(new ExcludeArrivalBeforeDepartureInSegmentChainFilterImpl())
                .linkWith(new ExcludeTotalDurationOnGroundMostThanImpl(Duration.ofHours(2)));
        printFlightsList(allChainFilter.getFilteredFlightsList(flights), "Chain implementation all filters");
    }

    private static void printFlightsList(List<Flight> flights, String message) {
        System.out.println("*******************************");
        System.out.println(message + ", list size: " + flights.size());
        System.out.println("*******************************");
        flights.forEach(System.out::println);
        System.out.println("*******************************");
        System.out.println();
    }
}
