package com.peterczigany.vote.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class VoteValidatorTest {

  private static VoteValidator voteValidator;

  @BeforeAll
  public static void setup() {
    voteValidator = new VoteValidator(new RepresentativeCodeValidator(), new VoteValueValidator());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/validVotes.csv")
  void testValidVote(String voteString) {
    assertDoesNotThrow(() -> voteValidator.validateVote(voteString));
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/invalidVotes.csv")
  void testInvalidVote(String voteString) {
    assertThrows(VoteException.class, () -> voteValidator.validateVote(voteString));
  }
}
