package com.peterczigany.vote.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class Vote {
  @Id @GeneratedValue private UUID id;
  private String representative;
  private VoteValue voteValue;
  @ManyToOne(fetch = FetchType.LAZY) private VotingSession votingSession;

  public Vote(UUID id, String representative, VoteValue voteValue, VotingSession votingSession) {
    this.id = id;
    this.representative = representative;
    this.voteValue = voteValue;
    this.votingSession = votingSession;
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

  public VotingSession getVotingSession() {
    return votingSession;
  }

  public void setVotingSession(VotingSession votingSession) {
    this.votingSession = votingSession;
  }
}
