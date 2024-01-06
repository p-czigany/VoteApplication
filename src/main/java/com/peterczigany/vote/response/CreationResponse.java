package com.peterczigany.vote.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreationResponse(@JsonProperty("szavazasId") String votingSessionId) {}
