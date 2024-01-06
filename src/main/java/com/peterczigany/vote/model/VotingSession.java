package com.peterczigany.vote.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.List;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class VotingSession {
  @Id
  @GeneratedValue(generator = "shortUrlIdGenerator")
  @GenericGenerator(name = "shortUrlIdGenerator", type = ShortUrlIdGenerator.class)
  private String id;

  private ZonedDateTime time;
  private String subject;
  private VotingSessionType votingSessionType;
  private String chair;

  @OneToMany
  @JoinColumn(name = "voting_session_id")
  private List<Vote> votes;

  public VotingSession() {}

  public VotingSession(
      ZonedDateTime time,
      String subject,
      VotingSessionType votingSessionType,
      String chair,
      List<Vote> votes) {
    this(null, time, subject, votingSessionType, chair, votes);
  }

  public VotingSession(
      String id,
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
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
