package com.peterczigany.vote.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class Vote {
  @Id @GeneratedValue private UUID id;
  private String representative;
  private VoteValue voteValue;

  public Vote(String representative, String voteValueString) {
    this(representative, VoteValue.valueOf(voteValueString));
  }

  public Vote(String representative, VoteValue voteValue) {
    this.representative = representative;
    this.voteValue = voteValue;
  }

  public Vote(UUID id, String representative, VoteValue voteValue) {
    this.id = id;
    this.representative = representative;
    this.voteValue = voteValue;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getRepresentative() {
    return representative;
  }

  public void setRepresentative(String representative) {
    this.representative = representative;
  }

  public VoteValue getVoteValue() {
    return voteValue;
  }

  public void setVoteValue(VoteValue voteValue) {
    this.voteValue = voteValue;
  }
}
