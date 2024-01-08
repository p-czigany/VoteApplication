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
        .isEqualTo(ZonedDateTime.of(2023, 9, 28, 11, 6, 25, 0, ZoneId.of("UTC")));
    assertThat(votingSession.getSubject()).isEqualTo("Szavazás tárgya");
    assertThat(votingSession.getVotingSessionType()).isEqualTo(VotingSessionType.PRESENCE);
    assertThat(votingSession.getChair()).isEqualTo("Kepviselo1");
    assertThat(votingSession.getVotes().get(1).getRepresentative()).isEqualTo("Kepviselo2");
    assertThat(votingSession.getVotes().get(1).getVoteValue()).isEqualTo(VoteValue.AGAINST);
    assertThat(votingSession.countTotalVotes()).isEqualTo(3);
    assertThat(votingSession.countVotes(VoteValue.FOR)).isEqualTo(1);
    assertThat(votingSession.countVotes(VoteValue.AGAINST)).isEqualTo(1);
    assertThat(votingSession.countVotes(VoteValue.ABSTAIN)).isEqualTo(1);
  }
}
