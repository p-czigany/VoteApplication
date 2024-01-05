package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peterczigany.vote.VotingSession.Type;
import com.peterczigany.vote.VotingSession.Vote;
import com.peterczigany.vote.VotingSession.Vote.VoteValue;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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
    assertThat(votingSession.type()).isEqualTo(Type.PRESENCE);
    assertThat(votingSession.chair()).isEqualTo("Kepviselo1");
    assertThat(votingSession.votes().get(0)).isEqualTo(vote1);
    assertThat(votingSession.votes().get(1).representative()).isEqualTo("Kepviselo2");
    assertThat(votingSession.votes().get(1).voteValue()).isEqualTo(VoteValue.AGAINST);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(
      strings = {
        "",
        " ",
        "2023-12-23 14:30:45",
        "2023-12-23T14:30:45+0130",
        "20240101T173952Z",
        "2024-01-01T10:39:52−08:00",
        "2024-01-01T10:39:52−08",
        "2024-01-01T10:39:52−0630"
      })
  void testVotingSessionCreationWithMissingOrInvalidTimeString(String timeString) {
    assertThatThrownBy(
            () ->
                new VotingSession(
                    timeString,
                    "Subject of Voting",
                    "j",
                    "Kepviselo1",
                    List.of(
                        new Vote("Kepviselo1", "i"),
                        new Vote("Kepviselo2", "n"),
                        new Vote("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @Test
  void testVotingSessionCreationWithoutChairVote() {
    assertThatThrownBy(
            () ->
                new VotingSession(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    "j",
                    "Kepviselo0",
                    List.of(
                        new Vote("Kepviselo1", "i"),
                        new Vote("Kepviselo2", "n"),
                        new Vote("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "jelenléti (NOT VALID)"})
  void testVotingSessionCreationWithMissingOrInvalidType(String type) {
    assertThatThrownBy(
            () ->
                new VotingSession(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    type,
                    "Kepviselo1",
                    List.of(
                        new Vote("Kepviselo1", "i"),
                        new Vote("Kepviselo2", "n"),
                        new Vote("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "igen (NOT VALID)"})
  void testVotingSessionCreationWithMissingOrInvalidVoteValue(String voteValue) {
    assertThatThrownBy(
            () ->
                new VotingSession(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    "j",
                    "Kepviselo1",
                    List.of(
                        new Vote("Kepviselo1", voteValue),
                        new Vote("Kepviselo2", "n"),
                        new Vote("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void testVotingSessionCreationWithMissingOrEmptySubject(String subject) {
    assertThatThrownBy(
            () ->
                new VotingSession(
                    "2023-12-23 14:30:45Z",
                    subject,
                    "j",
                    "Kepviselo1",
                    List.of(
                        new Vote("Kepviselo1", "i"),
                        new Vote("Kepviselo2", "n"),
                        new Vote("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void testVotingSessionCreationWithMissingOrEmptyRepresentative(String representative) {
    assertThatThrownBy(
            () ->
                new VotingSession(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    "j",
                    "Kepviselo1",
                    List.of(
                        new Vote("Kepviselo1", "i"),
                        new Vote(representative, "n"),
                        new Vote("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }
}
