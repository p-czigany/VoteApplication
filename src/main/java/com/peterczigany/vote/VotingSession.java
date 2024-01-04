package com.peterczigany.vote;

import java.time.ZonedDateTime;
import java.util.List;

public record VotingSession(
    ZonedDateTime time, String subject, String type, String chair, List<Vote> votes) {

  public record Vote(String representative, String voteValue) {}

  public VotingSession(
      String timeString, String subject, String type, String chair, List<Vote> votes) {
    this(ZonedDateTime.parse(timeString.replace(" ", "T")), subject, type, chair, votes);
  }
}
