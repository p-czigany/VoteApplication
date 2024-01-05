package com.peterczigany.vote.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class VotingSession {
  @Id @GeneratedValue private UUID id;
  private ZonedDateTime time;
  private String subject;
  private VotingSessionType votingSessionType;
  private String chair;

  @OneToMany(mappedBy = "votingSession", cascade = CascadeType.ALL)
  private final List<Vote> votes;

  public VotingSession(
      UUID id,
      ZonedDateTime time,
      String subject,
      VotingSessionType votingSessionType,
      String chair,
      List<Vote> votes) {
    this.id = id;
    this.time = time;
    this.subject = subject;
    this.votingSessionType = votingSessionType;
    this.chair = chair;
    this.votes = votes;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ZonedDateTime getTime() {
    return time;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public VotingSessionType getVotingSessionType() {
    return votingSessionType;
  }

  public void setVotingSessionType(VotingSessionType votingSessionType) {
    this.votingSessionType = votingSessionType;
  }

  public String getChair() {
    return chair;
  }

  public void setChair(String chair) {
    this.chair = chair;
  }

  public List<Vote> getVotes() {
    return votes;
  }
}
