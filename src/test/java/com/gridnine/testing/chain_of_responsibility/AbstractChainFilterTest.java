package com.gridnine.testing.chain_of_responsibility;

import com.gridnine.testing.domain.Flight;
import com.gridnine.testing.domain.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class AbstractChainFilterTest {
    private AbstractChainFilter chainFilter;
    private final ChainFilter mockedChainFilter = Mockito.mock(ChainFilter.class);

    private List<Flight> getFlightsList() {
        Segment segment1 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(5)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Segment segment2 = new Segment(LocalDateTime.now().plus(Duration.ofMinutes(15)),
                LocalDateTime.now().plus(Duration.ofMinutes(10)));
        Flight flight1 = new Flight(List.of(segment1, segment2));

        Segment segment3 = new Segment(LocalDateTime.now().plus(Duration.ofHours(5)),
                LocalDateTime.now().plus(Duration.ofHours(10)));
        Flight flight2 = new Flight(List.of(segment3));
        return List.of(flight1, flight2);
    }

    @BeforeEach
    void setUp() {
        chainFilter = Mockito.spy(new AbstractChainFilter() {
            @Override
            protected List<Flight> getFilteredList(Collection<Flight> flights) {
                return new ArrayList<>(flights);
            }
        });
    }


    @Test
    @DisplayName("linkWith() set and return next filter")
    void linkWith_returnChainFilter() throws Exception {
        ChainFilter actual = chainFilter.linkWith(mockedChainFilter);
        assertThat(actual).isEqualTo(mockedChainFilter);

        Field field = AbstractChainFilter.class.getDeclaredField("next");
        field.setAccessible(true);
        actual = (ChainFilter) field.get(chainFilter);
        assertThat(actual).isEqualTo(mockedChainFilter);
    }

    @Test
    @DisplayName("getFilteredFlightsList() filter and return list")
    void getFilteredFlightsList() {
        var list = getFlightsList();
        var expected = new ArrayList<>(getFlightsList());
        expected.remove(1);
        Mockito.when(chainFilter.getFilteredList(list)).thenReturn(list);
        Mockito.when(chainFilter.checkNext(list)).thenReturn(expected);

        var actual = chainFilter.getFilteredFlightsList(list);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("checkNext() return list when next is null")
    void checkNext_whenNextNull_thenReturnList() {
        var expected = getFlightsList();
        var actual = chainFilter.checkNext(expected);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    @DisplayName("checkNext() return list when list is empty")
    void checkNext_whenEmptyList_thenReturnList() throws Exception {
        Field field = AbstractChainFilter.class.getDeclaredField("next");
        field.setAccessible(true);
        field.set(chainFilter, mockedChainFilter);

        List<Flight> expected = Collections.emptyList();
        var actual = chainFilter.checkNext(expected);
        assertThat(actual).isEqualTo(expected);
        Mockito.verifyNoInteractions(mockedChainFilter);
    }

    @Test
    @DisplayName("checkNext() call next.getFilteredFlightsList when list not empty and next exists")
    void checkNext_whenEmptyList_thenCallNext() throws Exception {
        Field field = AbstractChainFilter.class.getDeclaredField("next");
        field.setAccessible(true);
        field.set(chainFilter, mockedChainFilter);

        List<Flight> expected = getFlightsList();
        Mockito.when(mockedChainFilter.getFilteredFlightsList(expected)).thenReturn(expected);

        var actual = chainFilter.checkNext(expected);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(mockedChainFilter).getFilteredFlightsList(expected);
        Mockito.verifyNoMoreInteractions(mockedChainFilter);
    }
}