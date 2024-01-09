package com.peterczigany.vote.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResultValue {
  ACCEPTED("F"),
  REJECTED("U");

  @JsonValue public final String label;

  ResultValue(String label) {
    this.label = label;
  }
}
