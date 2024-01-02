package com.peterczigany.vote.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class VotingSessionValidatorTest {

  private static VotingSessionValidator votingSessionValidator;

  @BeforeAll
  static void setup() {
    votingSessionValidator =
        new VotingSessionValidator(
            new TimeValidator(),
            new SubjectValidator(),
            new TypeValidator(),
            new RepresentativeCodeValidator(),
            new VoteValidator(new RepresentativeCodeValidator(), new VoteValueValidator()));
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/validVotingSessions.csv")
  void testValidVotingSession(String votingSessionJson) {
    assertDoesNotThrow(() -> votingSessionValidator.validateVotingSession(votingSessionJson));
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = {
        "/invalidVotingSessions/badJson.csv",
        "/invalidVotingSessions/badTimeFormat.csv",
        "/invalidVotingSessions/blankField.csv",
        "/invalidVotingSessions/invalidType.csv",
        "/invalidVotingSessions/invalidVoteValue.csv",
        "/invalidVotingSessions/missingField.csv"
      })
  void testInvalidVotingSession(String votingSessionJson) {
    assertThrows(
        VoteException.class, () -> votingSessionValidator.validateVotingSession(votingSessionJson));
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/invalidVotingSessions/chairDidNotVote.csv")
  void testChairDidNotVote(String votingSessionJson) {
    assertThrows(
        VoteException.class, () -> votingSessionValidator.validateVotingSession(votingSessionJson));
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/invalidVotingSessions/repHasMoreThanOneVotes.csv")
  void testRepHasMoreThanOneVotes(String votingSessionJson) {
    assertThrows(
        VoteException.class, () -> votingSessionValidator.validateVotingSession(votingSessionJson));
  }
}
