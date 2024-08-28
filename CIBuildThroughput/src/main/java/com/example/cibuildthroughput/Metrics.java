package com.example.cibuildthroughput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class Metrics {
    //    Time in minutes
    public static final int HOUR = 60;
    public static final int DAY = 1440;
    public static final int WEEK = 10080;
    public static final int MONTH = 43800;
    public static final int SIXMONTHS = 262800;
    private List<Series> series = new ArrayList<>();
    private List<String> common = new ArrayList<>();
    private int durationMedian;
    private int durationVariance;
    private int intervalMedian;
    private int intervalVariance;
    private String durationMedianDORA;
    private String durationVarianceDORA;
    private String intervalMedianDORA;
    private String intervalVarianceDORA;

    public Metrics(List<Long> buildDuration, List<Long> buildIntervals, LocalDateTime[] buildStartTime) {
        this.durationMedian = convertToMinutes(median(buildDuration));
        this.durationVariance = convertToMinutes(variance(buildDuration));
        this.intervalMedian = convertToMinutes(median(buildIntervals));
        this.intervalVariance = convertToMinutes(variance(buildIntervals));
        this.durationMedianDORA = getDurationDORA(this.durationMedian);
        this.durationVarianceDORA = getDurationDORA(this.durationVariance);
        this.intervalMedianDORA = getIntervalDORA(this.intervalMedian);
        this.intervalVarianceDORA = getIntervalDORA(this.intervalVariance);
        sortData(buildDuration, buildIntervals, buildStartTime);
    }

    public void sortData(List<Long> buildDuration, List<Long> buildIntervals, LocalDateTime[] buildStartTime) {

        Map<LocalDate, List<Long>> timeDur = getListTimesByDate(buildStartTime, buildDuration);
        Map<LocalDate, List<Long>> timeInt = getListTimesByDate(buildStartTime, buildIntervals);

        List<LocalDate> dates = getUniqueDates(buildStartTime);
        Collections.sort(dates);

        Map<LocalDate, Integer> durationMedianHashMap = getMedianHashMap(timeDur);
        Map<LocalDate, Integer> durationVarianceHashMap = getVarianceHashMap(timeDur);
        Map<LocalDate, Integer> intervalMedianHashMap = getMedianHashMap(timeInt);
        Map<LocalDate, Integer> intervalVarianceHashMap = getVarianceHashMap(timeInt);

        List<Integer> durationMedianSeries = new ArrayList<>();
        List<Integer> durationVarianceSeries = new ArrayList<>();
        List<Integer> intervalMedianSeries = new ArrayList<>();
        List<Integer> intervalVarianceSeries = new ArrayList<>();

        for (LocalDate date : dates) {
            this.common.add(String.valueOf(date));
            durationMedianSeries.add(durationMedianHashMap.get(date));
            durationVarianceSeries.add(durationVarianceHashMap.get(date));
            intervalMedianSeries.add(intervalMedianHashMap.get(date));
            intervalVarianceSeries.add(intervalVarianceHashMap.get(date));
        }
        setSeries(durationMedianSeries, durationVarianceSeries, intervalMedianSeries, intervalVarianceSeries);
    }

    public List<LocalDate> getUniqueDates(LocalDateTime[] buildStartTime) {
        ArrayList<LocalDate> dates = new ArrayList<>();
        for (LocalDateTime localDateTime : buildStartTime) {
            LocalDate date = LocalDate.from(localDateTime);
            if (!dates.contains(date)) {
                dates.add(date);
            }
        }
        return dates;
    }

    public Map<LocalDate, List<Long>> getListTimesByDate(LocalDateTime[] buildStartTime, List<Long> buildData) {
        HashMap<LocalDate, List<Long>> time = new HashMap<>();

        for (int i = 0; i < buildStartTime.length; i++) {
            LocalDate date = LocalDate.from(buildStartTime[i]);

            if (time.containsKey(date)) {
                time.get(date).add(buildData.get(i));
            } else {
                List<Long> bd = new ArrayList<>();
                bd.add(buildData.get(i));
                time.put(date, bd);
            }
        }
        return time;
    }

    public Map<LocalDate, Integer> getMedianHashMap(Map<LocalDate, List<Long>> timeHashMap) {
        HashMap<LocalDate, Integer> hashMap = new HashMap<>();
        for (Map.Entry<LocalDate, List<Long>> entry : timeHashMap.entrySet()) {
            hashMap.put(entry.getKey(), convertToMinutes(median(entry.getValue())));
        }
        return hashMap;
    }

    public Map<LocalDate, Integer> getVarianceHashMap(Map<LocalDate, List<Long>> timeHashMap) {
        HashMap<LocalDate, Integer> hashMap = new HashMap<>();
        for (Map.Entry<LocalDate, List<Long>> entry : timeHashMap.entrySet()) {
            hashMap.put(entry.getKey(), convertToMinutes(variance(entry.getValue())));
        }
        return hashMap;
    }

    public void setSeries(List<Integer> durationMedianSeries, List<Integer> durationVarianceSeries, List<Integer> intervalMedianSeries, List<Integer> intervalVarianceSeries) {
        Series dmSerires = new Series("Duration Median", durationMedianSeries);
        Series dvSerires = new Series("Duration Variance", durationVarianceSeries);
        Series imSerires = new Series("Interval Median", intervalMedianSeries);
        Series ivSerires = new Series("Interval Variance", intervalVarianceSeries);

        this.series.add(dmSerires);
        this.series.add(dvSerires);
        this.series.add(imSerires);
        this.series.add(ivSerires);
    }

    public int median(List<Long> buildTimes) {
        Collections.sort(buildTimes);
        int numEntries = buildTimes.size();
        long median = 0;

        if (numEntries % 2 == 0) {
            long sumOfMiddleElements = buildTimes.get(numEntries / 2) + buildTimes.get(numEntries / 2 - 1);
            median = sumOfMiddleElements / 2;
        } else {
            median = buildTimes.get(numEntries / 2);
        }

        return (int) median;
    }

    public long variance(List<Long> buildTimes) {
        double sqDiff = 0.0;
        long sum = 0;

        for (Long buildTime : buildTimes) {
            sum += buildTime;
        }
        double mean = sum / (double) buildTimes.size();

        for (int i = 0; i < buildTimes.size(); i++) {
            sqDiff += Math.pow(buildTimes.get(i) - mean, 2);
        }

        return Math.round(Math.sqrt(sqDiff / buildTimes.size()));
    }

    private int convertToMinutes(double millisec) {
        return (int) Math.round(millisec / 60000);
    }

    public String getDurationDORA(int minutes) {
        if (minutes < HOUR) return "Elite";
        if (minutes < WEEK) return "High";
        if (minutes < SIXMONTHS) return "Medium";
        return "Low";
    }

    public String getIntervalDORA(int minutes) {
        if (minutes < DAY) return "Elite";
        if (minutes < MONTH) return "High";
        if (minutes < SIXMONTHS) return "Medium";
        return "Low";
    }

    public int getDurationMedian() {
        return durationMedian;
    }

    public int getDurationVariance() {
        return durationVariance;
    }

    public int getIntervalMedian() {
        return intervalMedian;
    }

    public int getIntervalVariance() {
        return intervalVariance;
    }

    public String getDurationMedianDORA() {
        return durationMedianDORA;
    }

    public String getDurationVarianceDORA() {
        return durationVarianceDORA;
    }

    public String getIntervalMedianDORA() {
        return intervalMedianDORA;
    }

    public String getIntervalVarianceDORA() {
        return intervalVarianceDORA;
    }

    public List<Series> getSeries() {
        return series;
    }

    public List<String> getCommon() {
        return common;
    }
}