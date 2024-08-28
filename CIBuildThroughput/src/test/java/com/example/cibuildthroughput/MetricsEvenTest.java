package com.example.cibuildthroughput;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class MetricsEvenTest {

    Long[] buildDur = {600000L, 1200000L, 600000L, 1200000L}; // 10mins, 20mins, 10mins, 20mins
    List<Long> buildDuration = Arrays.asList(buildDur);
    Long[] buildIntv = {100000L, 200000L, 300000L, 400000L}; // 1.66mins, 3.33mins, 5mins, 6.66mins
    List<Long> buildIntervals = Arrays.asList(buildIntv);

    LocalDateTime[] buildStartTimes = {
            LocalDateTime.of(2022,01,01,12,30,04),
            LocalDateTime.of(2022,01,01,16,55,55),
            LocalDateTime.of(2022,01,02,9,40,33),
            LocalDateTime.of(2022,01,03,15,15,15)
    };

    private Metrics metrics = new Metrics(buildDuration, buildIntervals, buildStartTimes);

    @Test
    void testDurationMedian001() {
        int durationMedian = metrics.getDurationMedian();
        assertThat(durationMedian, is(15));
    }

    @Test
    void testIntervalMedian002() {
        int intervalMedian = metrics.getIntervalMedian();
        assertThat(intervalMedian, is(4));
    }

    @Test
    void testDurationVariance003() {
        int durationVariance = metrics.getDurationVariance();
        assertThat(durationVariance, is(5));
    }

    @Test
    void testIntervalVariance004() {
        int intervalVariance = metrics.getIntervalVariance();
        assertThat(intervalVariance, is(2));
    }

}
