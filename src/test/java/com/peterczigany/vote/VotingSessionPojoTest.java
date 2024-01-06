package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;

import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class VotingSessionPojoTest {

  @Test
  void testCreation() {
    VotingSession votingSession = TestUtils.validVotingSession();

    assertThat(votingSession.getTime())
        .isEqualTo(ZonedDateTime.of(2023, 12, 23, 14, 30, 45, 0, ZoneId.of("UTC")));
    assertThat(votingSession.getSubject()).isEqualTo("Subject of Voting");
    assertThat(votingSession.getVotingSessionType()).isEqualTo(VotingSessionType.PRESENCE);
    assertThat(votingSession.getChair()).isEqualTo("Kepviselo1");
    assertThat(votingSession.getVotes().get(1).getRepresentative()).isEqualTo("Kepviselo2");
    assertThat(votingSession.getVotes().get(1).getVoteValue()).isEqualTo(VoteValue.AGAINST);
  }
}
