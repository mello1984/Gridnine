package com.gridnine.testing.chain_of_responsibility;

import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.Segment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExcludeArrivalBeforeDepartureInSegmentChainFilterImpl extends AbstractChainFilter {

    @Override
    protected List<Flight> getFilteredList(Collection<Flight> flights) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : flights) {
            boolean flag = true;
            for (Segment s : f.getSegments()) {
                if (s.getArrivalDate().isBefore(s.getDepartureDate())) {
                    flag = false;
                    break;
                }
            }
            if (flag) result.add(f);
        }
        return result;
    }
}