package com.peterczigany.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peterczigany.vote.model.VoteDTO;
import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.model.VotingSessionType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class VotingSessionDTOTest {

  @Test
  void testVotingSessionCreationIsSuccessful() {
    VoteDTO voteDTO1 = new VoteDTO("Kepviselo1", "i");
    List<VoteDTO> voteDTOS = List.of(
        voteDTO1, new VoteDTO("Kepviselo2", "n"), new VoteDTO("Kepviselo3", "t"));

    VotingSessionDTO votingSessionDTO =
        new VotingSessionDTO("2023-12-23 14:30:45Z", "Subject of Voting", "j", "Kepviselo1",
            voteDTOS);

    assertThat(votingSessionDTO.time())
        .isEqualTo(ZonedDateTime.of(2023, 12, 23, 14, 30, 45, 0, ZoneId.of("UTC")));
    assertThat(votingSessionDTO.subject()).isEqualTo("Subject of Voting");
    assertThat(votingSessionDTO.votingSessionType()).isEqualTo(VotingSessionType.PRESENCE);
    assertThat(votingSessionDTO.chair()).isEqualTo("Kepviselo1");
    assertThat(votingSessionDTO.voteDTOS().get(0)).isEqualTo(voteDTO1);
    assertThat(votingSessionDTO.voteDTOS().get(1).representative()).isEqualTo("Kepviselo2");
    assertThat(votingSessionDTO.voteDTOS().get(1).voteValue()).isEqualTo(VoteValue.AGAINST);
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
                new VotingSessionDTO(
                    timeString,
                    "Subject of Voting",
                    "j",
                    "Kepviselo1",
                    List.of(
                        new VoteDTO("Kepviselo1", "i"),
                        new VoteDTO("Kepviselo2", "n"),
                        new VoteDTO("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @Test
  void testVotingSessionCreationWithoutChairVote() {
    assertThatThrownBy(
            () ->
                new VotingSessionDTO(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    "j",
                    "Kepviselo0",
                    List.of(
                        new VoteDTO("Kepviselo1", "i"),
                        new VoteDTO("Kepviselo2", "n"),
                        new VoteDTO("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "jelenléti (NOT VALID)"})
  void testVotingSessionCreationWithMissingOrInvalidType(String type) {
    assertThatThrownBy(
            () ->
                new VotingSessionDTO(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    type,
                    "Kepviselo1",
                    List.of(
                        new VoteDTO("Kepviselo1", "i"),
                        new VoteDTO("Kepviselo2", "n"),
                        new VoteDTO("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "igen (NOT VALID)"})
  void testVotingSessionCreationWithMissingOrInvalidVoteValue(String voteValue) {
    assertThatThrownBy(
            () ->
                new VotingSessionDTO(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    "j",
                    "Kepviselo1",
                    List.of(
                        new VoteDTO("Kepviselo1", voteValue),
                        new VoteDTO("Kepviselo2", "n"),
                        new VoteDTO("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void testVotingSessionCreationWithMissingOrEmptySubject(String subject) {
    assertThatThrownBy(
            () ->
                new VotingSessionDTO(
                    "2023-12-23 14:30:45Z",
                    subject,
                    "j",
                    "Kepviselo1",
                    List.of(
                        new VoteDTO("Kepviselo1", "i"),
                        new VoteDTO("Kepviselo2", "n"),
                        new VoteDTO("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void testVotingSessionCreationWithMissingOrEmptyRepresentative(String representative) {
    assertThatThrownBy(
            () ->
                new VotingSessionDTO(
                    "2023-12-23 14:30:45Z",
                    "Subject of Voting",
                    "j",
                    "Kepviselo1",
                    List.of(
                        new VoteDTO("Kepviselo1", "i"),
                        new VoteDTO(representative, "n"),
                        new VoteDTO("Kepviselo3", "t"))))
        .isInstanceOf(Exception.class);
  }
}
