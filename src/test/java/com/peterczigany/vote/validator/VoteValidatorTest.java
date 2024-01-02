package com.peterczigany.vote.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VoteValidatorTest {

  private static VoteValidator voteValidator;

  @BeforeAll
  public static void setup() {
    voteValidator = new VoteValidator(new RepresentativeCodeValidator(), new VoteValueValidator());
  }

  @ParameterizedTest
  @ValueSource(strings = {"{ \"kepviselo\": \"Kepviselo1\", \"szavazat\": \"i\" }"})
  void testValidVote(String voteString) {
    assertDoesNotThrow(() -> voteValidator.validateVote(voteString));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "{ \"kepviselo\": \"Kepviselo1\", \"szavazat\": \"j\" }", // invalid vote value
        "{ \"szavazat\": \"i\" }", // rep is missing
        "{ \"kepviselo\": \"Kepviselo1\" }", // vote value is missing
        "{ \"kepviselo\": \"\", \"szavazat\": \"i\" }", // rep is empty
        "{ \"kepviselo\": \"Kepviselo1\", \"szavazat\": \"\" }", // vote value is empty
        """
              {
                  "kepviselo":"Kepviselo3",
                  "szavazat":{
                      "kepviselo":"Kepviselo2",
                      "szavazat":"n"
                  }
              }
        """
      })
  void testInvalidVote(String voteString) {
    assertThrows(VoteException.class, () -> voteValidator.validateVote(voteString));
  }
}
