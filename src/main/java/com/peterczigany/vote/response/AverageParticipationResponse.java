package com.peterczigany.vote.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AverageParticipationResponse(@JsonProperty("atlag") double averageParticipation) {}
