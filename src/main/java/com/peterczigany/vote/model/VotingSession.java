package com.peterczigany.vote.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "voting_sessions")
public class VotingSession {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private final UUID id;

  @Column(name = "time")
  private final ZonedDateTime time;

  @Column(name = "subject")
  private final String subject;

  @Column(name = "voting_session_type")
  private final VotingSessionType votingSessionType;

  @Column(name = "chair")
  private final String chair;

  //  @OneToMany(mappedBy = "voting_session", cascade = CascadeType.ALL)
  @OneToMany private final List<Vote> votes;

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

  public ZonedDateTime getTime() {
    return time;
  }

  public String getSubject() {
    return subject;
  }

  public VotingSessionType getVotingSessionType() {
    return votingSessionType;
  }

  public String getChair() {
    return chair;
  }

  public List<Vote> getVotes() {
    return votes;
  }
}
