package com.example.cibuildthroughput;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class DateTimeTest {

    private ConvertDateTime convertDateTime = new ConvertDateTime();

    @Test
    void testGettingLocalDateTime() {
        LocalDateTime localDateTime = convertDateTime.convertToDateTime(1660125260010L);

        assertThat(localDateTime,
                is(LocalDateTime.of(
                        LocalDate.of(2022, 8, 10),
                        LocalTime.of(9, 54, 20, 10000000))));
    }

    @Test
    void testGettingLocalDate() {
        LocalDate localDate = convertDateTime.convertToDate(1660125260010L);

        assertThat(localDate,
                is(localDate.of(2022, 8, 10)));
    }

    @Test
    void testGettingLocalTime() {
        LocalTime localTime = convertDateTime.convertToTime(1660125260010L);

        assertThat(localTime,
                is(LocalTime.of(9, 54, 20, 10000000)));
    }

    @Test
    void testConvertToDuration() {
        Duration duration = convertDateTime.convertToDuration(1380884);

        assertThat(duration, is(Duration.ofMillis(1380884)));
    }

    @Test
    void testConvertToInterval() {
        LocalTime interval = convertDateTime.convertToInterval(1660125260010L, 1660061707595L);

        assertThat(interval, is(LocalTime.of(17, 39, 12, 415000000)));
    }
}
