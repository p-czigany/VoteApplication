package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VoteValidationTest {

  private static final String VOTE_VALUE_NULL =
      "A szavazat értéke nem lehet null. Megadása kötelező.";
  private static final String VOTE_VALUE_EMPTY =
      "A szavazat értéke nem lehet üres. Megadása kötelező.";

  private static final String VOTE_VALUE_NOT_VALID =
      "A szavazat értéke nem lehet %s. Csak 'i', 'n' és 't' adható meg.";

  private static final List<String> VOTE_TYPES = List.of("i", "n", "t");

  static class MyValidator {
    void validateVoteValue(String typeString) throws VoteException {
      if (typeString == null) {
        throw new VoteException(VOTE_VALUE_NULL);
      }
      if (typeString.isEmpty()) {
        throw new VoteException(VOTE_VALUE_EMPTY);
      }
      if (!VOTE_TYPES.contains(typeString)) {
        throw new VoteException(String.format(VOTE_VALUE_NOT_VALID, typeString));
      }
    }
  }

  private MyValidator myValidator;

  @BeforeEach
  void init() {
    myValidator = new MyValidator();
  }

  @Test
  void testVoteValueCannotBeNull() {
    Throwable exception =
        assertThrows(VoteException.class, () -> myValidator.validateVoteValue(null));

    assertThat(exception.getMessage()).isEqualTo(VOTE_VALUE_NULL);
  }

  @Test
  void testVoteValueCannotBeEmpty() {
    Throwable exception =
        assertThrows(VoteException.class, () -> myValidator.validateVoteValue(""));

    assertThat(exception.getMessage()).isEqualTo(VOTE_VALUE_EMPTY);
  }

  @ParameterizedTest
  @ValueSource(strings = {"I", "N", "T", "igen", "nem", "tartózkodás", "123"})
  void testVoteValueNotValid(String typeString) {
    Throwable exception =
        assertThrows(VoteException.class, () -> myValidator.validateVoteValue(typeString));

    assertThat(exception.getMessage()).isEqualTo(String.format(VOTE_VALUE_NOT_VALID, typeString));
  }
}
