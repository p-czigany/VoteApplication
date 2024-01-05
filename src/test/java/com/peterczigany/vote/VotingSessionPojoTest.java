package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;

import com.peterczigany.vote.model.Vote;
import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class VotingSessionPojoTest {

  @Test
  void testCreation() {
    Vote vote1 = new Vote(null, "Kepviselo1", VoteValue.FOR, null);
    List<Vote> votes =
        List.of(
            vote1,
            new Vote(null, "Kepviselo2", VoteValue.AGAINST, null),
            new Vote(null, "Kepviselo3", VoteValue.ABSTAIN, null));
    VotingSession votingSession =
        new VotingSession(
            null,
            ZonedDateTime.parse("2023-12-23T14:30:45Z"),
            "Subject of Voting",
            VotingSessionType.PRESENCE,
            "Kepviselo1",
            votes);

    assertThat(votingSession.getTime())
        .isEqualTo(ZonedDateTime.of(2023, 12, 23, 14, 30, 45, 0, ZoneId.of("UTC")));
    assertThat(votingSession.getSubject()).isEqualTo("Subject of Voting");
    assertThat(votingSession.getVotingSessionType()).isEqualTo(VotingSessionType.PRESENCE);
    assertThat(votingSession.getChair()).isEqualTo("Kepviselo1");
    assertThat(votingSession.getVotes().get(0)).isEqualTo(vote1);
    assertThat(votingSession.getVotes().get(1).getRepresentative()).isEqualTo("Kepviselo2");
    assertThat(votingSession.getVotes().get(1).getVoteValue()).isEqualTo(VoteValue.AGAINST);
  }
}
