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

  @Test
  void testFindLastestPresenceVotingBeforeTime() {
    VotingSession presence1 = TestUtils.sessionWithXPresent(10); // "2023-09-28T11:01:25Z"
    VotingSession supermajority = TestUtils.supermajorityVotingSession();
    supermajority.setTime(supermajority.getTime().minusMinutes(10)); // "2023-09-28T10:56:25Z"
    VotingSession presence2 = TestUtils.sessionWithXPresent(20);
    presence2.setTime(presence2.getTime().plusMinutes(10)); // "2023-09-28T11:11:25Z"
    VotingSession majority = TestUtils.majorityVotingSession(5, 3, 1); // "2023-09-28T11:06:25Z"
    VotingSession presence3 = TestUtils.sessionWithXPresent(30);
    presence3.setTime(presence3.getTime().minusMinutes(10)); // "2023-09-28T10:51:25Z"
    repository.save(presence1);
    repository.save(supermajority);
    repository.save(presence2);
    repository.save(majority);
    repository.save(presence3);

    VotingSession voting1 =
        repository
            .findLatestPresenceVotingSessionBefore(
                VotingSessionType.PRESENCE, ZonedDateTime.parse("2023-09-28T11:00:00Z"))
            .orElseThrow();
    VotingSession voting2 =
        repository
            .findLatestPresenceVotingSessionBefore(
                VotingSessionType.PRESENCE, ZonedDateTime.parse("2023-09-28T11:10:00Z"))
            .orElseThrow();
    VotingSession voting3 =
        repository
            .findLatestPresenceVotingSessionBefore(
                VotingSessionType.PRESENCE, ZonedDateTime.parse("2023-09-28T11:12:25Z"))
            .orElseThrow();
    VotingSession voting4 =
        repository
            .findLatestPresenceVotingSessionBefore(
                VotingSessionType.PRESENCE, ZonedDateTime.parse("2023-09-28T11:01:25Z"))
            .orElseThrow();

    assertThat(voting1.countTotalVotes()).isEqualTo(30);
    assertThat(voting2.countTotalVotes()).isEqualTo(10);
    assertThat(voting3.countTotalVotes()).isEqualTo(20);
    assertThat(voting4.countTotalVotes()).isEqualTo(30);
  }
}
