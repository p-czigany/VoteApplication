package com.peterczigany.vote.validator;

import static com.peterczigany.vote.validator.VoteValueValidator.VOTE_VALUE_EMPTY;
import static com.peterczigany.vote.validator.VoteValueValidator.VOTE_VALUE_NOT_VALID;
import static com.peterczigany.vote.validator.VoteValueValidator.VOTE_VALUE_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.exception.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VoteValueValidatorTest {

  private static VoteValueValidator voteValueValidator;

  @BeforeAll
  static void setup() {
    voteValueValidator = new VoteValueValidator();
  }

  @Test
  void testVoteValueCannotBeNull() {
    Exception exception =
        assertThrows(VoteException.class, () -> voteValueValidator.validate(null));

    assertThat(exception.getMessage()).isEqualTo(VOTE_VALUE_NULL);
  }

  @Test
  void testVoteValueCannotBeEmpty() {
    Exception exception =
        assertThrows(VoteException.class, () -> voteValueValidator.validate(""));

    assertThat(exception.getMessage()).isEqualTo(VOTE_VALUE_EMPTY);
  }

  @ParameterizedTest
  @ValueSource(strings = {"I", "N", "T", "igen", "nem", "tartózkodás", "123"})
  void testVoteValueNotValid(String typeString) {
    Exception exception =
        assertThrows(VoteException.class, () -> voteValueValidator.validate(typeString));

    assertThat(exception.getMessage()).isEqualTo(String.format(VOTE_VALUE_NOT_VALID, typeString));
  }
}
