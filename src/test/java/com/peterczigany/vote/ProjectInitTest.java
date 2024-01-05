package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.peterczigany.vote.exception.VoteException;
import org.junit.jupiter.api.Test;

class ProjectInitTest {

  @SuppressWarnings("java:S2699") // this is an empty test method by definition
  @Test
  void emptyTest() {}

  @SuppressWarnings("all") // testing the testing framework - triviality is of the essence here
  @Test
  void testStringEquality() {
    assertEquals("ok", "ok");
  }

  @Test
  void testStringReturnValue() {
    TestType testObject = new TestType();

    var actual = testObject.getOk();

    assertThat(actual).isExactlyInstanceOf(String.class).isEqualTo("ok");
  }

  @Test
  void testBooleanEquality() {
    assertTrue(true);
  }

  @Test
  void testBooleanReturnValue() {
    TestType testObject = new TestType();

    var actual = testObject.getTrue();

    assertThat(actual).isTrue();
  }

  @Test
  void testException() {
    assertThrows(
        Exception.class,
        () -> {
          throw new Exception();
        });
  }

  @Test
  void testExceptionThrownByMethod() {
    TestType testObject = new TestType();

    assertThrows(VoteException.class, testObject::throwException);
  }
}
