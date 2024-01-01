package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TypeValidationTest {

  private static final String TYPE_NULL = "A típus nem lehet null. Megadása kötelező.";
  private static final String TYPE_EMPTY = "A típus nem lehet üres. Megadása kötelező.";

  private static final String TYPE_NOT_VALID =
      "A típus nem lehet %s. Csak 'j', 'e' és 'm' adható meg.";

  private static final List<String> VOTE_TYPES = List.of("j", "e", "m");

  static class MyValidator {
    void validateType(String typeString) throws VoteException {
      if (typeString == null) {
        throw new VoteException(TYPE_NULL);
      }
      if (typeString.isEmpty()) {
        throw new VoteException(TYPE_EMPTY);
      }
      if (!VOTE_TYPES.contains(typeString)) {
        throw new VoteException(String.format(TYPE_NOT_VALID, typeString));
      }
    }
  }

  @Test
  void testTypeCannotBeNull() {
    MyValidator myValidator = new MyValidator();

    Throwable exception = assertThrows(VoteException.class, () -> myValidator.validateType(null));

    assertThat(exception.getMessage()).isEqualTo(TYPE_NULL);
  }

  @Test
  void testTypeCannotBeEmpty() {
    MyValidator myValidator = new MyValidator();

    Throwable exception = assertThrows(VoteException.class, () -> myValidator.validateType(""));

    assertThat(exception.getMessage()).isEqualTo(TYPE_EMPTY);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "J",
        "E",
        "M",
        "jelenlét",
        "egyszerű többségi szavazás",
        "minősített többségi szavazás",
        "123"
      })
  void testTypeNotValid(String typeString) {
    MyValidator myValidator = new MyValidator();

    Throwable exception =
        assertThrows(VoteException.class, () -> myValidator.validateType(typeString));

    assertThat(exception.getMessage()).isEqualTo(String.format(TYPE_NOT_VALID, typeString));
  }
}
