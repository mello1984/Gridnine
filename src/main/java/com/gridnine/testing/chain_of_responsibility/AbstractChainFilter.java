package com.gridnine.testing.chain_of_responsibility;

import com.gridnine.testing.domain.Flight;

import java.util.Collection;
import java.util.List;

public abstract class AbstractChainFilter implements ChainFilter {
    private ChainFilter next;

    @Override
    public final ChainFilter linkWith(ChainFilter chainFilter) {
        next = chainFilter;
        return next;
    }

    @Override
    public final List<Flight> getFilteredFlightsList(Collection<Flight> flights) {
        var filtered = getFilteredList(flights);
        return checkNext(filtered);
    }

    protected List<Flight> checkNext(List<Flight> flights) {
        if (next == null || flights.isEmpty()) return flights;
        else return next.getFilteredFlightsList(flights);
    }

    protected abstract List<Flight> getFilteredList(Collection<Flight> flights);
}
