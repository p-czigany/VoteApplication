package com.peterczigany.vote.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VoteValue {
  FOR("i"),
  AGAINST("n"),
  ABSTAIN("t");

  @JsonValue
  public final String label;

  VoteValue(String label) {
    this.label = label;
  }
}
