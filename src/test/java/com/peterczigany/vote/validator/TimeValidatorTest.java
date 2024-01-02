package com.peterczigany.vote.validator;

import static com.peterczigany.vote.validator.TimeValidator.TIME_BAD_FORMAT;
import static com.peterczigany.vote.validator.TimeValidator.TIME_EMPTY;
import static com.peterczigany.vote.validator.TimeValidator.TIME_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimeValidatorTest {

  private static TimeValidator timeValidator;

  @BeforeAll
  static void setup() {
    timeValidator = new TimeValidator();
  }

  @Test
  void testTimeCannotBeNull() {
    Throwable exception = assertThrows(VoteException.class, () -> timeValidator.validateTime(null));

    assertThat(exception.getMessage()).isEqualTo(TIME_NULL);
  }

  @Test
  void testTimeCannotBeEmpty() {
    Throwable exception = assertThrows(VoteException.class, () -> timeValidator.validateTime(""));

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
  void testTimeIsNotValidISO8601String(String timeString) {
    Exception exception =
        assertThrows(VoteException.class, () -> timeValidator.validateTime(timeString));

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
    assertDoesNotThrow(() -> timeValidator.validateTime(timeString));
  }
}
