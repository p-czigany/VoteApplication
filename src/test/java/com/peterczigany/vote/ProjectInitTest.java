package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProjectInitTest {

  static class TestType {
    String getOk() {
      return "ok";
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
    assertThat(testObject.getOk()).isExactlyInstanceOf(String.class);
  }
}
