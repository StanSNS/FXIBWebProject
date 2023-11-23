package fxibBackend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static fxibBackend.constants.ConfigConst.CUSTOM_DATE_FORMAT_PRESENT;
import static fxibBackend.constants.ConfigConst.CUSTOM_DATE_FORMAT_RECEIVED;

@Component
@RequiredArgsConstructor
public class CustomDateFormatter {

    /**
     * Formats a LocalDateTime object as a string using a custom date-time format.
     *
     * @param localDateTime The LocalDateTime object to be formatted.
     * @return A string representation of the LocalDateTime in the specified format.
     */
    public String formatLocalDateTimeNowAsString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CUSTOM_DATE_FORMAT_PRESENT);
        return localDateTime.format(formatter);
    }

    /**
     * Converts a timestamp to a LocalDateTime object.
     *
     * @param timestamp The timestamp to convert.
     * @return The corresponding LocalDateTime.
     */
    public LocalDateTime timeStampToDate(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


    /**
     * Formats a date string according to a custom date format.
     *
     * @param dateToConvert The input date string to be formatted.
     * @return The formatted date string.
     */
    public String formatCustomDateTime(String dateToConvert) {
        return LocalDateTime
                .parse(dateToConvert, DateTimeFormatter
                        .ofPattern(CUSTOM_DATE_FORMAT_RECEIVED))
                .format(DateTimeFormatter
                        .ofPattern(CUSTOM_DATE_FORMAT_PRESENT));

    }


}
