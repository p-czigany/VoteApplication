package com.peterczigany.vote.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record VotingSessionResultResponse(
    @JsonProperty("eredmeny") ResultValue result,
    @JsonProperty("kepviselokSzama") long numberOfRepresentatives,
    @JsonProperty("igenekSzama") long voteCountFor,
    @JsonProperty("nemekSzama") long voteCountAgainst,
    @JsonProperty("tartozkodasokSzama") long voteCountAbstaining) {
}
