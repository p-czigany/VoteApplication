package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TimeValidationTest {

  static class MyValidator {
    void validateTime(LocalDateTime time) throws VoteException {
      throw new VoteException("TIME:NULL");
    }
  }

  @Test
  void testTimeCannotBeNull() throws VoteException {
    MyValidator myValidator = new MyValidator();

    Throwable exception = assertThrows(VoteException.class, () -> myValidator.validateTime(null));

    assertThat(exception.getMessage()).isEqualTo("TIME:NULL");
  }
}
