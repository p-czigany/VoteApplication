package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProjectInitTest {

  static class TestType {
    String getOk() {
      return "ok";
    }

    boolean getTrue() {
      return true;
    }
  }

  @SuppressWarnings("java:S2699") // this is an empty test method by definition
  @Test
  void emptyTest() {}

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
  void testBooleanReturnValue() {
    TestType testObject = new TestType();

    var actual = testObject.getTrue();

    assertThat(actual).isTrue();
  }
}
