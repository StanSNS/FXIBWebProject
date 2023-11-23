package UtilTest;

import fxibBackend.util.CustomDateFormatter;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomDateFormatterTest {

    @Test
    public void testFormatLocalDateTimeNowAsString() {
        CustomDateFormatter customDateFormatter = new CustomDateFormatter();
        LocalDateTime testDateTime = LocalDateTime.now();

        String formattedDateTime = customDateFormatter.formatLocalDateTimeNowAsString(testDateTime);

        assertNotNull(formattedDateTime);
    }

    @Test
    public void testTimeStampToDate() {
        CustomDateFormatter customDateFormatter = new CustomDateFormatter();
        long testTimestamp = 1637680800L;

        LocalDateTime resultDateTime = customDateFormatter.timeStampToDate(testTimestamp);

        assertNotNull(resultDateTime);
    }

    @Test
    public void testFormatCustomDateTime() {
        CustomDateFormatter customDateFormatter = new CustomDateFormatter();
        String testDateToConvert = "03/03/2023 15:46";

        String formattedDate = customDateFormatter.formatCustomDateTime(testDateToConvert);

        assertNotNull(formattedDate);
    }
}
