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

class ExcludeTotalDurationOnGroundMostThanImplTest {

    @Test
    @DisplayName("exclude flight when total duration on ground more than threshold")
    void totalDurationOnGroundMostThan_whenTotalDurationOnGroundMostThanThreshold_thenReturnTrue() {
        var filter = new ExcludeTotalDurationOnGroundMostThanImpl(Duration.ofHours(1).plus(Duration.ofMinutes(30)));
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(10)),
                LocalDateTime.now().plus(Duration.ofMinutes(15)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(30)),
                LocalDateTime.now().plus(Duration.ofHours(2)));
        Segment segment3 = new Segment(LocalDateTime.now().plus(Duration.ofHours(3)),
                LocalDateTime.now().plus(Duration.ofHours(4)));
        Segment segment4 = new Segment(LocalDateTime.now().plus(Duration.ofHours(3).plus(Duration.ofMinutes(20))),
                LocalDateTime.now().plus(Duration.ofHours(7)));
        Flight correct1 = new Flight(List.of(segment1));
        Flight correct2 = new Flight(List.of(segment1, segment2));
        Flight correct3 = new Flight(List.of(segment1, segment2, segment3));
        Flight incorrect1 = new Flight(List.of(segment1, segment3));
        Flight incorrect2 = new Flight(List.of(segment1, segment2, segment4));

        var flights = List.of(correct1, correct2, correct3, incorrect1, incorrect2);
        var expected = List.of(correct1, correct2, correct3);
        var actual = filter.getFilteredList(flights);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("return empty list when list empty")
    void totalDurationOnGroundMostThan_whenEmptyList_thenReturnEmptyList() {
        var filter = new ExcludeTotalDurationOnGroundMostThanImpl(Duration.ZERO);

        List<Flight> expected = Collections.emptyList();
        var actual = filter.getFilteredList(expected);
        assertThat(actual).isEqualTo(expected);
    }
}