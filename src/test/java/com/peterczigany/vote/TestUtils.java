package com.peterczigany.vote;

import com.peterczigany.vote.model.*;
import java.time.ZonedDateTime;
import java.util.List;

public class TestUtils {

  public static VotingSession validVotingSession() {
    Vote vote1 = new Vote("Kepviselo1", VoteValue.FOR);
    List<Vote> votes =
        List.of(
            vote1,
            new Vote("Kepviselo2", VoteValue.AGAINST),
            new Vote("Kepviselo3", VoteValue.ABSTAIN));
    return new VotingSession(
        ZonedDateTime.parse("2023-09-28T11:06:25Z"),
        "Szavazás tárgya",
        VotingSessionType.PRESENCE,
        "Kepviselo1",
        votes);
  }

  public static VotingSessionDTO validVotingSessionDTO() {
    VoteDTO voteDTO1 = new VoteDTO("Kepviselo1", "i");
    List<VoteDTO> voteDTOs =
        List.of(voteDTO1, new VoteDTO("Kepviselo2", "n"), new VoteDTO("Kepviselo3", "t"));
    return new VotingSessionDTO(
        "2023-09-28T11:06:25Z", "Szavazás tárgya", "j", "Kepviselo1", voteDTOs);
  }
}
