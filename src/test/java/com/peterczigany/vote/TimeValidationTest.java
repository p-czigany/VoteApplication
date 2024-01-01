package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;

class TimeValidationTest {

  private static final String TIME_NULL = "Az időpont nem lehet null. Megadása kötelező.";
  private static final String TIME_EMPTY = "Az időpont nem lehet üres. Megadása kötelező.";
  private static final String TIME_BAD_FORMAT =
      "Az időpont formátuma nem megfelelő. ISO8601 (YYYY-MM-DDThh:mm:ssZ) formátumban szükséges megadni.)";

  static class MyValidator {
    void validateTime(String timeString) throws VoteException {
      if (timeString == null) {
        throw new VoteException(TIME_NULL);
      }
      if (timeString.isEmpty()) {
        throw new VoteException(TIME_EMPTY);
      }
      try {
        LocalDateTime.parse(timeString);
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

  @Test
  void testTimeMustBeISO8601String() throws VoteException {
    MyValidator myValidator = new MyValidator();

    Exception exception =
        assertThrows(VoteException.class, () -> myValidator.validateTime("2023-12-23 14:30:45"));

    assertThat(exception.getMessage()).isEqualTo(TIME_BAD_FORMAT);
  }

  @Test
  void testSuccessfulTimeStringValidation() {
    MyValidator myValidator = new MyValidator();

    assertDoesNotThrow(() -> myValidator.validateTime("2023-12-23T14:30:45Z"));
    assertDoesNotThrow(() -> myValidator.validateTime("2023-12-23T14:30:45+01:00"));
    assertDoesNotThrow(() -> myValidator.validateTime("2023-12-23T14:30:45+0100"));
    assertDoesNotThrow(() -> myValidator.validateTime("2023-12-23T14:30:45+01"));
  }
}
