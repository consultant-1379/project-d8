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
class MetricsOddTest {

    Long[] buildDur = {600000L, 1200000L, 600000L, 1200000L, 660000L}; // 10mins, 20mins, 10mins, 20mins, 11mins
    List<Long> buildDuration = Arrays.asList(buildDur);
    Long[] buildIntv = {100000L, 200000L, 300000L, 400000L, 500000L}; // 1.66mins, 3.33mins, 5mins, 6.66mins, 8.33mins
    List<Long> buildIntervals = Arrays.asList(buildIntv);
    LocalDateTime[] buildStartTimes = {
            LocalDateTime.of(2022,01,01,12,12,12),
            LocalDateTime.of(2022,01,01,14,15,16),
            LocalDateTime.of(2022,01,02,16,10,55),
            LocalDateTime.of(2022,01,03,17,44,44),
            LocalDateTime.of(2022,01,02,10,30,20)
    };

    private Metrics metrics = new Metrics(buildDuration, buildIntervals, buildStartTimes);

    @Test
    void testDurationMedian001() {
        int durationMedian = metrics.getDurationMedian();
        assertThat(durationMedian, is(11));
    }

    @Test
    void testIntervalMedian002() {
        int intervalMedian = metrics.getIntervalMedian();
        assertThat(intervalMedian, is(5));
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

    @Test
    void testDurationMedianDora() {
        String durationMedianDORA = metrics.getDurationMedianDORA();
        assertThat(durationMedianDORA, is("Elite"));
    }

    @Test
    void testDurationVarianceDora() {
        String durationVarianceDORA = metrics.getDurationVarianceDORA();
        assertThat(durationVarianceDORA, is("Elite"));
    }

    @Test
    void testIntervalMedianDora() {
        String intervalMedianDORA = metrics.getIntervalMedianDORA();
        assertThat(intervalMedianDORA, is("Elite"));
    }

    @Test
    void testIntervalVarianceDora() {
        String intervalVarianceDora = metrics.getIntervalVarianceDORA();
        assertThat(intervalVarianceDora, is("Elite"));
    }

    @Test
    void testReturnHighDuration() {
        String durationDORA = metrics.getDurationDORA(1441);
        assertThat(durationDORA, is("High"));
    }

    @Test
    void testReturnMediumDuration() {
        String durationDORA = metrics.getDurationDORA(10080);
        assertThat(durationDORA, is("Medium"));
    }

    @Test
    void testReturnLowDuration() {
        String durationDORA = metrics.getDurationDORA(262801);
        assertThat(durationDORA, is("Low"));
    }

    @Test
    void testReturnHighInterval() {
        String intervalVarianceDora = metrics.getIntervalDORA(1440);
        assertThat(intervalVarianceDora, is("High"));
    }

    @Test
    void testReturnMediumInterval() {
        String intervalVarianceDora = metrics.getIntervalDORA(43800);
        assertThat(intervalVarianceDora, is("Medium"));
    }

    @Test
    void testReturnLowInterval() {
        String intervalVarianceDora = metrics.getIntervalDORA(262801);
        assertThat(intervalVarianceDora, is("Low"));
    }

    @Test
    void testGetSeries() {
        List<Series> series = metrics.getSeries();

        assertThat(series.size(), is(4));
        assertThat(series.get(0).getName(), is("Duration Median"));
        assertThat(series.get(1).getName(), is("Duration Variance"));
        assertThat(series.get(2).getName(), is("Interval Median"));
        assertThat(series.get(3).getName(), is("Interval Variance"));
        assertThat(series.get(0).getGroup(), is("a"));
//        assertThat(series.get(0).getValues(), is(0));
    }

    @Test
    void testGetCommon() {
        String[] dates = {"2022-01-01","2022-01-02","2022-01-03"};
        List<String> datesString = Arrays.asList(dates);
        assertThat(metrics.getCommon(), is(datesString));
    }
}
