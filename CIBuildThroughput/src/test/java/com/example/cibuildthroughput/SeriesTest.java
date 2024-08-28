package com.example.cibuildthroughput;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SeriesTest {
    Integer[] array = {34, 435, 22, 324};
    private List<Integer> values = Arrays.asList(array);
    private Series series = new Series("testName", values);

    @Test
    void testGetName() {
        assertThat(series.getName(), is("testName"));
    }

    @Test
    void testGetGroup() {
        assertThat(series.getGroup(), is("a"));
    }

    @Test
    void testGetValues() {
        assertThat(series.getValues(), is(values));
    }
}
