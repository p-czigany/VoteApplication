package com.peterczigany.vote.model;

public enum VotingSessionType {
  PRESENCE("j"),
  MAJORITY("e"),
  SUPERMAJORITY("m");

  public final String label;

  VotingSessionType(String label) {
    this.label = label;
  }
}
