package ru.caloriemanager.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.caloriemanager.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilTest {

    //Arrange
    //Act
    //Assert

    @ParameterizedTest
    @MethodSource("testDateArguments")
    void isBetweenInclusive(LocalDateTime endDateTime, LocalDateTime testDate, LocalDateTime startDateTime, boolean result) {
        //Arrange
        //Act
        //Assert
        Assertions.assertEquals(result, DateTimeUtil.isBetweenInclusive(testDate, startDateTime, endDateTime));
    }

    private static Stream<Arguments> testDateArguments() {
        LocalDateTime endDateTime1 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59));
        LocalDateTime testDate1 = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIN);
        LocalDateTime startDateTime1 = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 1));

        LocalDateTime endDateTime2 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0));
        LocalDateTime testDate2 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0));
        LocalDateTime startDateTime2 = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 1));

        LocalDateTime endDateTime3 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59));
        LocalDateTime testDate3 = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 0));
        LocalDateTime startDateTime3 = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 1));
        return Stream.of(
                Arguments.of(endDateTime1, testDate1, startDateTime1, true),
                Arguments.of(endDateTime2, testDate2, startDateTime2, true),
                Arguments.of(endDateTime3, testDate3, startDateTime3, false));
    }

    @Test
    void parseToLocalDate() {
        //Arrange
        //Act
        //Assert
    }

    @Test
    void parseToLocalTime() {
        //Arrange
        //Act
        //Assert
    }

    @Test
    void testToString() {
        //Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 0));
        //Act
        String result = DateTimeUtil.toString(dateTime);
        //Assert
        Assertions.assertEquals(dateTime.format(formatter), result);
    }

    @Test
    void createDateTime() {
        //Arrange
        LocalDate localDate = LocalDate.now();
        LocalDate localDateDefault = LocalDate.now().minusDays(1);
        LocalTime localTime = LocalTime.now();
        //Act
        LocalDateTime actual = DateTimeUtil.createDateTime(localDate, localDateDefault, localTime);
        //Assert
        Assertions.assertEquals(LocalDateTime.of(localDate, localTime), actual);
    }

    @Test
    void createDateTimeIfLocalDateNull() {
        //Arrange
        LocalDate localDate = null;
        LocalDate localDateDefault = LocalDate.now().minusDays(1);
        LocalTime localTime = LocalTime.now();
        //Act
        LocalDateTime actual = DateTimeUtil.createDateTime(localDate, localDateDefault, localTime);
        //Assert
        Assertions.assertEquals(LocalDateTime.of(localDateDefault, localTime), actual);
    }
}