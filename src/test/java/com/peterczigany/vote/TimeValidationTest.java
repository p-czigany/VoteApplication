package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimeValidationTest {

  private static final String TIME_NULL = "Az időpont nem lehet null. Megadása kötelező.";
  private static final String TIME_EMPTY = "Az időpont nem lehet üres. Megadása kötelező.";
  private static final String TIME_BAD_FORMAT =
      "Az időpont formátuma nem megfelelő. (MSZ ISO 8601:2003 formátumban szükséges megadni.)";

  static class MyValidator {
    void validateTime(String timeString) throws VoteException {
      if (timeString == null) {
        throw new VoteException(TIME_NULL);
      }
      if (timeString.isEmpty()) {
        throw new VoteException(TIME_EMPTY);
      }
      try {
        ZonedDateTime.parse(timeString.replace(" ", "T"));
      } catch (DateTimeParseException e) {
        throw new VoteException(TIME_BAD_FORMAT);
      }
    }
  }

  @Test
  void testTimeCannotBeNull() throws VoteException {
    MyValidator myValidator = new MyValidator();

    Throwable exception = assertThrows(VoteException.class, () -> myValidator.validateTime(null));

    assertThat(exception.getMessage()).isEqualTo(TIME_NULL);
  }

  @Test
  void testTimeCannotBeEmpty() throws VoteException {
    MyValidator myValidator = new MyValidator();

    Throwable exception = assertThrows(VoteException.class, () -> myValidator.validateTime(""));

    assertThat(exception.getMessage()).isEqualTo(TIME_EMPTY);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "2023-12-23 14:30:45",
        "2023-12-23T14:30:45+0130",
        "20240101T173952Z",
        "2024-01-01T10:39:52−08:00",
        "2024-01-01T10:39:52−08",
        "2024-01-01T10:39:52−0630"
      })
  void testTimeIsNotValidISO8601String(String timeString) throws VoteException {
    MyValidator myValidator = new MyValidator();

    Exception exception =
        assertThrows(VoteException.class, () -> myValidator.validateTime(timeString));

    assertThat(exception.getMessage()).isEqualTo(TIME_BAD_FORMAT);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "2023-12-23T14:30:45Z",
        "2024-01-01T10:39:52-08:00[America/Los_Angeles]",
        "2023-12-23 14:30:45Z",
        "2023-12-23 14:30Z",
        "2023-12-23T14:30:45+01:00[Europe/Paris]",
        "2023-12-23T14:30:45+01:00",
        "2023-12-23T14:30:45+01"
      })
  void testSuccessfulTimeStringValidation(String timeString) {
    MyValidator myValidator = new MyValidator();

    assertDoesNotThrow(() -> myValidator.validateTime(timeString));
  }
}
