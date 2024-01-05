package com.peterczigany.vote;

import java.time.ZonedDateTime;
import java.util.List;

public record VotingSession(
    ZonedDateTime time, String subject, Type type, String chair, List<Vote> votes) {

  public enum Type {
    PRESENCE("j"),
    MAJORITY("e"),
    SUPERMAJORITY("m");

    public final String label;

    Type(String label) {
      this.label = label;
    }
  }

  private static Type mapToType(String typeString) {
    return switch (typeString) {
      case "j" -> Type.PRESENCE;
      case "e" -> Type.MAJORITY;
      case "m" -> Type.SUPERMAJORITY;
      default -> throw new IllegalArgumentException(String.format("Invalid type: %s", typeString));
    };
  }

  public record Vote(String representative, VoteValue voteValue) {

    public enum VoteValue {
      FOR("i"),
      AGAINST("n"),
      ABSTAIN("t");

      public final String label;

      VoteValue(String label) {
        this.label = label;
      }
    }

    public Vote {
      if (representative == null) {
        throw new IllegalArgumentException("Representative cannot be null.");
      }
      if (representative.isBlank()) {
        throw new IllegalArgumentException("Representative cannot be blank.");
      }
    }

    public Vote(String representative, String voteValue) {
      this(representative, mapToVoteValue(voteValue));
    }

    private static VoteValue mapToVoteValue(String voteValue) {
      return switch (voteValue) {
        case "i" -> VoteValue.FOR;
        case "n" -> VoteValue.AGAINST;
        case "t" -> VoteValue.ABSTAIN;
        default -> throw new IllegalArgumentException(
            String.format("Invalid vote value: %s", voteValue));
      };
    }
  }

  public VotingSession {
    if (subject == null) {
      throw new IllegalArgumentException("Subject cannot be null.");
    }
    if (subject.isBlank()) {
      throw new IllegalArgumentException("Subject cannot be blank.");
    }
    if (!votes.stream().map(vote -> vote.representative).toList().contains(chair)) {
      throw new IllegalArgumentException("Chair must vote.");
    }
  }

  public VotingSession(
      String timeString, String subject, String typeString, String chair, List<Vote> votes) {
    this(
        ZonedDateTime.parse(timeString.replace(" ", "T")),
        subject,
        mapToType(typeString),
        chair,
        votes);
  }
}
