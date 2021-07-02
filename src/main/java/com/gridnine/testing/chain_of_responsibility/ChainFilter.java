package com.gridnine.testing.chain_of_responsibility;

import com.gridnine.testing.domain.Flight;

import java.util.Collection;
import java.util.List;

public interface ChainFilter {
    ChainFilter linkWith(ChainFilter filter);

    List<Flight> getFilteredFlightsList(Collection<Flight> flights);
}
