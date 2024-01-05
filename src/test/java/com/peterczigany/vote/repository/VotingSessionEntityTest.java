package com.peterczigany.vote.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.peterczigany.vote.model.Vote;
import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionType;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class VotingSessionEntityTest {

  @Autowired private VotingSessionRepository repository;

  @Test
  void testPersisting() {
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

    VotingSession savedVotingSession = repository.save(votingSession);

    assertThat(savedVotingSession.getChair()).isEqualTo("Kepviselo1");
    assertThat(savedVotingSession.getVotes()).hasSize(3);
  }
}
