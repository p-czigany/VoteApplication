package com.peterczigany.vote.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.peterczigany.vote.model.VoteDTO;
import com.peterczigany.vote.model.VotingSessionType;
import java.time.ZonedDateTime;
import java.util.List;

public record DailyVotingSessionsResponse(
    @JsonProperty("szavazasok") List<DailyVotingSession> dailyVotingSessions) {

  public record DailyVotingSession(
      @JsonProperty("idopont") ZonedDateTime time,
      @JsonProperty("targy") String subject,
      @JsonProperty("tipus") VotingSessionType type,
      @JsonProperty("elnok") String chair,
      @JsonProperty("eredmeny") ResultValue result,
      @JsonProperty("kepviselokSzama") long numberOfRepresentatives,
      @JsonProperty("szavazatok") List<VoteDTO> voteDTOs) {}
}
