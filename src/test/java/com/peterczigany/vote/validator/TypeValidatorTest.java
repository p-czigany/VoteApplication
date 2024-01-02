package com.peterczigany.vote.validator;

import static com.peterczigany.vote.validator.TypeValidator.TYPE_EMPTY;
import static com.peterczigany.vote.validator.TypeValidator.TYPE_NOT_VALID;
import static com.peterczigany.vote.validator.TypeValidator.TYPE_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TypeValidatorTest {

  private static TypeValidator typeValidator;

  @BeforeAll
  static void setup() {
    typeValidator = new TypeValidator();
  }

  @Test
  void testTypeCannotBeNull() {
    Throwable exception = assertThrows(VoteException.class, () -> typeValidator.validateType(null));

    assertThat(exception.getMessage()).isEqualTo(TYPE_NULL);
  }

  @Test
  void testTypeCannotBeEmpty() {
    Throwable exception = assertThrows(VoteException.class, () -> typeValidator.validateType(""));

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
    Throwable exception =
        assertThrows(VoteException.class, () -> typeValidator.validateType(typeString));

    assertThat(exception.getMessage()).isEqualTo(String.format(TYPE_NOT_VALID, typeString));
  }
}
