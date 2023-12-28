package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TimeValidationTest {

  static class MyValidator {
    void validateTime(String timeString) throws VoteException {
      if (timeString == null) {
        throw new VoteException("TIME:NULL");
      }
      if (timeString.isEmpty()) {
        throw new VoteException("TIME:EMPTY");
      }
    }
  }

  @Test
  void testTimeCannotBeNull() throws VoteException {
    MyValidator myValidator = new MyValidator();

    Throwable exception = assertThrows(VoteException.class, () -> myValidator.validateTime(null));

    assertThat(exception.getMessage()).isEqualTo("TIME:NULL");
  }

  @Test
  void testTimeCannotBeEmpty() throws VoteException {
    MyValidator myValidator = new MyValidator();

    Throwable exception = assertThrows(VoteException.class, () -> myValidator.validateTime(""));

    assertThat(exception.getMessage()).isEqualTo("TIME:EMPTY");
  }
}
