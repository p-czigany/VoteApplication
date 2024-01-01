package com.peterczigany.vote.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VoteValidatorTest {

  @ParameterizedTest
  @ValueSource(strings = {"{ \"kepviselo\": \"Kepviselo1\", \"szavazat\": \"i\" }"})
  void testValidVote(String voteString) {
    VoteValidator voteValidator = new VoteValidator();

    assertDoesNotThrow(() -> voteValidator.validateVote(voteString));
  }

  @ParameterizedTest
  @ValueSource(strings = {"{ \"kepviselo\": \"Kepviselo1\", \"szavazat\": \"j\" }", "{ \"szavazat\": \"i\" }", "{ \"kepviselo\": \"Kepviselo1\" }", "{ \"kepviselo\": \"\", \"szavazat\": \"i\" }", "{ \"kepviselo\": \"Kepviselo1\", \"szavazat\": \"\" }"})
  void testInvalidVote(String voteString) {
    VoteValidator voteValidator = new VoteValidator();

    Exception exception =
        assertThrows(VoteException.class, () -> voteValidator.validateVote(voteString));

    assertThat(exception.getMessage()).isEqualTo(VOTE_NOT_VALID);
  }
}
