package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TimeValidationTest {

  private static final String TIME_NULL =
      "Az időpont nem lehet null. Megadása kötelező. (ÉÉÉÉ-HH-NNTÓÓ:PP:MMZ formátumban.)";
  private static final String TIME_EMPTY =
      "Az időpont nem lehet üres. Megadása kötelező. (ÉÉÉÉ-HH-NNTÓÓ:PP:MMZ formátumban.)";

  static class MyValidator {
    void validateTime(String timeString) throws VoteException {
      if (timeString == null) {
        throw new VoteException(TIME_NULL);
      }
      if (timeString.isEmpty()) {
        throw new VoteException(TIME_EMPTY);
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
}
