package com.peterczigany.vote.model;

import java.time.ZonedDateTime;
import java.util.List;

public record VotingSessionDTO(
    ZonedDateTime time,
    String subject,
    VotingSessionType votingSessionType,
    String chair,
    List<VoteDTO> voteDTOS) {

  private static VotingSessionType mapToType(String typeString) {
    return switch (typeString) {
      case "j" -> VotingSessionType.PRESENCE;
      case "e" -> VotingSessionType.MAJORITY;
      case "m" -> VotingSessionType.SUPERMAJORITY;
      default -> throw new IllegalArgumentException(String.format("Invalid type: %s", typeString));
    };
  }

  public VotingSessionDTO {
    if (subject == null) {
      throw new IllegalArgumentException("Subject cannot be null.");
    }
    if (subject.isBlank()) {
      throw new IllegalArgumentException("Subject cannot be blank.");
    }
    if (!voteDTOS.stream().map(VoteDTO::representative).toList().contains(chair)) {
      throw new IllegalArgumentException("Chair must vote.");
    }
  }

  public VotingSessionDTO(
      String timeString, String subject, String typeString, String chair, List<VoteDTO> voteDTOS) {
    this(
        ZonedDateTime.parse(timeString.replace(" ", "T")),
        subject,
        mapToType(typeString),
        chair,
        voteDTOS);
  }
}
