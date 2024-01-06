package com.peterczigany.vote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

public record VotingSessionDTO(
    ZonedDateTime time,
    String subject,
    VotingSessionType votingSessionType,
    String chair,
    List<VoteDTO> voteDTOs) {

  private static VotingSessionType mapToType(String typeString) {
    return switch (typeString) {
      case "j" -> VotingSessionType.PRESENCE;
      case "e" -> VotingSessionType.MAJORITY;
      case "m" -> VotingSessionType.SUPERMAJORITY;
      default -> throw new IllegalArgumentException(
          String.format("Érvénytelen típus: %s", typeString));
    };
  }

  public VotingSessionDTO {
    if (subject == null) {
      throw new IllegalArgumentException("A tárgy nem lehet null.");
    }
    if (subject.isBlank()) {
      throw new IllegalArgumentException("A tárgy nem lehet üres.");
    }
    if (!voteDTOs.stream().map(VoteDTO::representative).toList().contains(chair)) {
      throw new IllegalArgumentException("Az elnöknek szavaznia kell.");
    }
  }

  public VotingSessionDTO(
      @JsonProperty("idopont") String timeString,
      @JsonProperty("targy") String subject,
      @JsonProperty("tipus") String typeString,
      @JsonProperty("elnok") String chair,
      @JsonProperty("szavazatok") List<VoteDTO> voteDTOs) {
    this(
        ZonedDateTime.parse(timeString.replace(" ", "T")),
        subject,
        mapToType(typeString),
        chair,
        voteDTOs);
  }
}
