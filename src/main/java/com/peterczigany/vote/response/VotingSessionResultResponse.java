package com.peterczigany.vote.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record VotingSessionResultResponse(
    @JsonProperty("eredmeny") ResultValue result,
    @JsonProperty("kepviselokSzama") int numberOfRepresentatives,
    @JsonProperty("igenekSzama") int voteCountFor,
    @JsonProperty("nemekSzama") int voteCountAgainst,
    @JsonProperty("tartozkodasokSzama") int voteCountAbstaining) {

  public enum ResultValue {
    ACCEPTED("F"),
    REJECTED("U");

    @JsonValue public final String label;

    ResultValue(String label) {
      this.label = label;
    }
  }

  //  private static ResultValue mapToResult(String resultString) {
  //    return switch (resultString) {
  //      case "F" -> ResultValue.ACCEPTED;
  //      case "U" -> ResultValue.REJECTED;
  //      default -> throw new IllegalArgumentException();
  //    };
  //  }
}
