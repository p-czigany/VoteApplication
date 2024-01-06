package com.peterczigany.vote.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VoteDTO(String representative, VoteValue voteValue) {

  public VoteDTO {
    if (representative == null) {
      throw new IllegalArgumentException("Representative cannot be null.");
    }
    if (representative.isBlank()) {
      throw new IllegalArgumentException("Representative cannot be blank.");
    }
  }

  public VoteDTO(
      @JsonProperty("kepviselo") String representative,
      @JsonProperty("szavazat") String voteValue) {
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
