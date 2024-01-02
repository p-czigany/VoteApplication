package com.peterczigany.vote.validator;

import static com.peterczigany.vote.validator.RepresentativeCodeValidator.REP_CODE_EMPTY;
import static com.peterczigany.vote.validator.RepresentativeCodeValidator.REP_CODE_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RepresentativeCodeValidatorTest {

  private static RepresentativeCodeValidator representativeCodeValidator;

  @BeforeAll
  static void setup() {
    representativeCodeValidator = new RepresentativeCodeValidator();
  }

  @Test
  void testRepresentativeCodeCannotBeNull() {
    Exception exception =
        assertThrows(
            VoteException.class,
            () -> representativeCodeValidator.validateRepresentativeCode(null));

    assertThat(exception.getMessage()).isEqualTo(REP_CODE_NULL);
  }

  @Test
  void testRepresentativeCodeCannotBeEmpty() {
    Exception exception =
        assertThrows(
            VoteException.class, () -> representativeCodeValidator.validateRepresentativeCode(""));

    assertThat(exception.getMessage()).isEqualTo(REP_CODE_EMPTY);
  }
}
