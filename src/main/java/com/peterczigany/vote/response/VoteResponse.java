package com.peterczigany.vote.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VoteResponse(@JsonProperty("szavazat") String voteValue) {}
