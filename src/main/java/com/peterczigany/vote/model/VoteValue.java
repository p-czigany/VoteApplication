package com.peterczigany.vote.model;

public enum VoteValue {
  FOR("i"),
  AGAINST("n"),
  ABSTAIN("t");

  public final String label;

  VoteValue(String label) {
    this.label = label;
  }
}
