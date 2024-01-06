package com.peterczigany.vote.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.peterczigany.vote.TestUtils;
import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class VotingSessionEntityTest {

  @Autowired private VotingSessionRepository repository;

  @Test
  void testPersisting() {
    VotingSession votingSession1 = TestUtils.validVotingSession();
    VotingSession votingSession2 = TestUtils.validVotingSession();
    votingSession2.setTime(ZonedDateTime.of(2024, 1, 1, 1, 1, 1, 0, ZoneId.of("UTC")));
    votingSession2.setVotingSessionType(VotingSessionType.MAJORITY);

    VotingSession saved1 = repository.save(votingSession1);
    VotingSession saved2 = repository.save(votingSession2);
    Optional<VotingSession> retrieved1 = repository.findById(saved1.getId());

    assertThat(saved1.getChair()).isEqualTo("Kepviselo1");
    assertThat(saved1.getVotes()).hasSize(3);
    assertThat(saved1.getVotingSessionType()).isEqualTo(VotingSessionType.PRESENCE);
    assertThat(retrieved1.isPresent()).isTrue();
    assertThat(retrieved1.get().getVotes()).hasSize(3);

    assertThat(repository.findById(saved2.getId()).isPresent()).isTrue();
    assertThat(repository.findById(saved2.getId()).get().getVotes()).hasSize(3);
    assertThat(repository.findById(saved2.getId()).get().getVotes().get(0).getVoteValue())
        .isEqualTo(VoteValue.FOR);
  }
}
