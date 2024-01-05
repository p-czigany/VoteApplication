package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peterczigany.vote.VotingSession.Vote;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class VotingSessionPojoTest {

  @Test
  void testVotingSessionCreationIsSuccessful() {
    Vote vote1 = new Vote("Kepviselo1", "i");
    List<Vote> votes = List.of(vote1, new Vote("Kepviselo2", "n"), new Vote("Kepviselo3", "t"));

    VotingSession votingSession =
        new VotingSession("2023-12-23 14:30:45Z", "Subject of Voting", "j", "Kepviselo1", votes);

    assertThat(votingSession.time())
        .isEqualTo(ZonedDateTime.of(2023, 12, 23, 14, 30, 45, 0, ZoneId.of("UTC")));
    assertThat(votingSession.subject()).isEqualTo("Subject of Voting");
    assertThat(votingSession.type()).isEqualTo("j");
    assertThat(votingSession.chair()).isEqualTo("Kepviselo1");
    assertThat(votingSession.votes().get(0)).isEqualTo(vote1);
    assertThat(votingSession.votes().get(1).representative()).isEqualTo("Kepviselo2");
    assertThat(votingSession.votes().get(1).voteValue()).isEqualTo("n");
  }

  @Test
  void testVotingSessionCreationWithInvalidTimeString() {
    assertThatThrownBy(
            () ->
                new VotingSession(
                    "2023-12-23 14:30:45",
                    "Subject of Voting",
                    "j",
                    "Kepviselo1",
                    List.of(
                        new Vote("Kepviselo1", "i"),
                        new Vote("Kepviselo2", "n"),
                        new Vote("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

//  @Test
//  void testVotingSessionCreationWithoutChairVote() {
//    assertThatThrownBy(
//            () ->
//                new VotingSession(
//                    "2023-12-23 14:30:45Z",
//                    "Subject of Voting",
//                    "j",
//                    "Kepviselo0",
//                    List.of(
//                        new Vote("Kepviselo1", "i"),
//                        new Vote("Kepviselo2", "n"),
//                        new Vote("Kepviselo3", "t"))))
//        .isInstanceOf(Exception.class);
//  }
//
//  @Test
//  void testVotingSessionCreationWithInvalidType() {
//    assertThatThrownBy(
//            () ->
//                new VotingSession(
//                    "2023-12-23 14:30:45Z",
//                    "Subject of Voting",
//                    "jelenléti (NOT VALID)",
//                    "Kepviselo1",
//                    List.of(
//                        new Vote("Kepviselo1", "i"),
//                        new Vote("Kepviselo2", "n"),
//                        new Vote("Kepviselo3", "t"))))
//        .isInstanceOf(Exception.class);
//  }
//
//  @Test
//  void testVotingSessionCreationWithInvalidVoteValue() {
//    assertThatThrownBy(
//            () ->
//                new VotingSession(
//                    "2023-12-23 14:30:45Z",
//                    "Subject of Voting",
//                    "j",
//                    "Kepviselo1",
//                    List.of(
//                        new Vote("Kepviselo1", "igen (NOT VALID)"),
//                        new Vote("Kepviselo2", "n"),
//                        new Vote("Kepviselo3", "t"))))
//        .isInstanceOf(Exception.class);
//  }
//
//  @Test
//  void testVotingSessionCreationWithEmptyField() {
//    assertThatThrownBy(
//            () ->
//                new VotingSession(
//                    "2023-12-23 14:30:45Z",
//                    "",
//                    "j",
//                    "Kepviselo1",
//                    List.of(
//                        new Vote("Kepviselo1", "i"),
//                        new Vote("Kepviselo2", "n"),
//                        new Vote("Kepviselo3", "t"))))
//        .isInstanceOf(Exception.class);
//  }
//
//  @Test
//  void testVotingSessionCreationWithNullField() {
//    assertThatThrownBy(
//            () ->
//                new VotingSession(
//                    "2023-12-23 14:30:45Z",
//                    null,
//                    "j",
//                    "Kepviselo1",
//                    List.of(
//                        new Vote("Kepviselo1", "i"),
//                        new Vote("Kepviselo2", "n"),
//                        new Vote("Kepviselo3", "t"))))
//        .isInstanceOf(Exception.class);
//  }
}
