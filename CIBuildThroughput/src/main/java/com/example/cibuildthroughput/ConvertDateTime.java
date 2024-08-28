package com.example.cibuildthroughput;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class ConvertDateTime {
    public LocalDateTime convertToDateTime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    public LocalDate convertToDate(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate();
    }

    public LocalTime convertToTime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalTime();
    }

    public Duration convertToDuration(long millis) {
        return Duration.ofMillis(millis);
    }

    public LocalTime convertToInterval(long startFirst, long startSecond) {
        LocalDateTime firstDate = Instant.ofEpochMilli(startFirst).atZone(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime secondDate = Instant.ofEpochMilli(startSecond).atZone(ZoneOffset.UTC).toLocalDateTime();

        return Instant.ofEpochMilli(secondDate.until(firstDate, ChronoUnit.MILLIS)).atZone(ZoneOffset.UTC).toLocalTime();
    }
}
