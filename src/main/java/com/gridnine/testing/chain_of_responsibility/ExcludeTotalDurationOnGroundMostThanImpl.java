package com.gridnine.testing.chain_of_responsibility;

import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.Segment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExcludeTotalDurationOnGroundMostThanImpl extends AbstractChainFilter {
    private final Duration duration;

    public ExcludeTotalDurationOnGroundMostThanImpl(Duration duration) {
        this.duration = duration;
    }

    @Override
    protected List<Flight> getFilteredList(Collection<Flight> flights) {
        List<Flight> result = new ArrayList<>();

        for (Flight f : flights) {
            Duration total = Duration.ZERO;
            List<Segment> segments = f.getSegments();
            for (int i = 0; i < segments.size() - 1; i++) {
                total = total.plus(Duration.between(segments.get(i).getArrivalDate(), segments.get(i + 1).getDepartureDate()));
            }
            if (total.compareTo(duration) <= 0) result.add(f);
        }
        return result;
    }
}
