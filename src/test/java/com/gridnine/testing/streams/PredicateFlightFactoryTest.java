package com.gridnine.testing.streams;

import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.Segment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PredicateFlightFactoryTest {

    @Test
    @DisplayName("departureBeforeNow() return true when first segment departure before now")
    void departureBeforeNow_whenFirstSegmentDepartureBeforeNow_thenReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().minus(Duration.ofMinutes(5)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.departureBeforeNow().test(flight)).isTrue();
    }

    @Test
    @DisplayName("departureBeforeNow() return true when second segment departure before now")
    void departureBeforeNow_whenSecondSegmentDepartureBeforeNow_thenReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(5)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().minus(Duration.ofMinutes(15)),
                LocalDateTime.now().minus(Duration.ofMinutes(10)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.departureBeforeNow().test(flight)).isTrue();
    }

    @Test
    @DisplayName("departureBeforeNow() return true when all segments departure before now")
    void departureBeforeNow_whenAllSegmentsDepartureBeforeNow_thenReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().minus(Duration.ofMinutes(30)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Segment segment2 = new Segment(LocalDateTime.now().minus(Duration.ofMinutes(15)),
                LocalDateTime.now().minus(Duration.ofMinutes(10)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.departureBeforeNow().test(flight)).isTrue();
    }

    @Test
    @DisplayName("departureBeforeNow() return false when all segments departure after now")
    void departureBeforeNow_whenAllSegmntsDepartureAfter_thenReturnFalse() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(5)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.departureBeforeNow().test(flight)).isFalse();
    }

    @Test
    @DisplayName("arrivalBeforeDepartureInSegment() return true when first segment departure before arrival")
    void arrivalBeforeDepartureInSegment_whenFirstSegmentArrivalBeforeDeparture_thenReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(25)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(45)),
                LocalDateTime.now().plus(Duration.ofMinutes(50)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.arrivalBeforeDepartureInSegment().test(flight)).isTrue();
    }

    @Test
    @DisplayName("arrivalBeforeDepartureInSegment() return true when second segment departure before arrival")
    void arrivalBeforeDepartureInSegment_whenSecondSegmentArrivalBeforeDeparture_thenReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(5)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(45)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.arrivalBeforeDepartureInSegment().test(flight)).isTrue();
    }

    @Test
    @DisplayName("arrivalBeforeDepartureInSegment() return true when all segments departure before arrival")
    void arrivalBeforeDepartureInSegment_whenAllSegmentArrivalBeforeDeparture_thenReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(45)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.arrivalBeforeDepartureInSegment().test(flight)).isTrue();
    }

    @Test
    @DisplayName("arrivalBeforeDepartureInSegment() return false when all segments departure after arrival")
    void arrivalBeforeDepartureInSegment_whenAllSegmentArrivalAfterDeparture_thenReturnFalse() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(45)),
                LocalDateTime.now().plus(Duration.ofMinutes(50)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.arrivalBeforeDepartureInSegment().test(flight)).isFalse();
    }

    @Test
    @DisplayName("totalDurationOnGroundMoreThan() return true when total time on ground more than threshold")
    void totalDurationOnGroundMostThan_whenDurationMoreThanThreshold_thanReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofHours(4)),
                LocalDateTime.now().plus(Duration.ofHours(5)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.totalDurationOnGroundMoreThan(Duration.ofHours(2)).test(flight)).isTrue();
    }

    @Test
    @DisplayName("totalDurationOnGroundMoreThan() return false when total time on ground less than threshold")
    void totalDurationOnGroundMostThan_whenDurationLessThanThreshold_thanReturnFalse() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofHours(1)),
                LocalDateTime.now().plus(Duration.ofHours(5)));
        Flight flight = new Flight(List.of(segment1, segment2));

        assertThat(PredicateFlightFactory.totalDurationOnGroundMoreThan(Duration.ofHours(2)).test(flight)).isFalse();
    }

    @Test
    @DisplayName("totalDurationOnGroundMoreThan() return false when one segment in flight")
    void totalDurationOnGroundMostThan_whenOneSegment_thanReturnFalse() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Flight flight = new Flight(List.of(segment1));

        assertThat(PredicateFlightFactory.totalDurationOnGroundMoreThan(Duration.ofHours(2)).test(flight)).isFalse();
    }

    @Test
    @DisplayName("totalDurationOnGroundMoreThan() return false when total time of 3 segment on ground less than threshold")
    void totalDurationOnGroundMostThan_whenDuration3SegmentsLessThanThreshold_thanReturnFalse() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(20)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(40)),
                LocalDateTime.now().plus(Duration.ofHours(1)));
        Segment segment3 = new Segment(LocalDateTime.now().plus(Duration.ofHours(2)),
                LocalDateTime.now().plus(Duration.ofHours(5)));
        Flight flight = new Flight(List.of(segment1, segment2, segment3));

        assertThat(PredicateFlightFactory.totalDurationOnGroundMoreThan(Duration.ofHours(2)).test(flight)).isFalse();
    }

    @Test
    @DisplayName("totalDurationOnGroundMoreThan() return true when total time of 3 segment on ground more than threshold")
    void totalDurationOnGroundMostThan_whenDuration3SegmentsMoreThanThreshold_thanReturnTrue() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofHours(1)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofHours(2)),
                LocalDateTime.now().plus(Duration.ofHours(3)));
        Segment segment3 = new Segment(LocalDateTime.now().plus(Duration.ofHours(4)).plus(Duration.ofMinutes(40)),
                LocalDateTime.now().plus(Duration.ofHours(5)));
        Flight flight = new Flight(List.of(segment1, segment2, segment3));

        assertThat(PredicateFlightFactory.totalDurationOnGroundMoreThan(Duration.ofHours(2)).test(flight)).isTrue();
    }
}