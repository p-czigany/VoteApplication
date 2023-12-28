package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProjectInitTest {

  static class MyObject {
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
    MyObject myObject = new MyObject();
    assertThat(myObject.getOk()).isExactlyInstanceOf(String.class);
  }
}
