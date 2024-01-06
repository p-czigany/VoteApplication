package com.peterczigany.vote.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VotingSessionType {
  PRESENCE("j"),
  MAJORITY("e"),
  SUPERMAJORITY("m");

  @JsonValue
  public final String label;

  VotingSessionType(String label) {
    this.label = label;
  }
}
