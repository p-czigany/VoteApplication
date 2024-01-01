package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepresentativeCodeValidationTest {

  private static final String REP_CODE_NULL =
      "A képviselő kódja nem lehet null. Megadása kötelező.";
  private static final String REP_CODE_EMPTY =
      "A képviselő kódja nem lehet üres. Megadása kötelező.";

  static class MyValidator {
    void validateRepresentativeCode(String typeString) throws VoteException {
      if (typeString == null) {
        throw new VoteException(REP_CODE_NULL);
      }
      if (typeString.isEmpty()) {
        throw new VoteException(REP_CODE_EMPTY);
      }
    }
  }

  private MyValidator myValidator;

  @BeforeEach
  void init() {
    myValidator = new MyValidator();
  }

  @Test
  void testRepresentativeCodeCannotBeNull() {
    Throwable exception =
        assertThrows(VoteException.class, () -> myValidator.validateRepresentativeCode(null));

    assertThat(exception.getMessage()).isEqualTo(REP_CODE_NULL);
  }

  @Test
  void testRepresentativeCodeCannotBeEmpty() {
    Throwable exception =
        assertThrows(VoteException.class, () -> myValidator.validateRepresentativeCode(""));

    assertThat(exception.getMessage()).isEqualTo(REP_CODE_EMPTY);
  }
}
