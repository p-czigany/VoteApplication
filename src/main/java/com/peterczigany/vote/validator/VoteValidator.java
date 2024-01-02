package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;

public class VoteValidator {

  private static final String REP_CODE_KEY = "kepviselo";
  private static final String VOTE_VALUE_KEY = "szavazat";
  private static final String VOTE_FORMAT_INVALID = "Az alábbi szavazat formája nem megfelelő: %s";

  private final RepresentativeCodeValidator repCodeValidator;
  private final VoteValueValidator voteValueValidator;

  @Autowired
  public VoteValidator(
      RepresentativeCodeValidator repCodeValidator, VoteValueValidator voteValueValidator) {
    this.repCodeValidator = repCodeValidator;
    this.voteValueValidator = voteValueValidator;
  }

  public void validateVote(String voteJson) throws VoteException {
    JsonParser parser = new BasicJsonParser();
    Map<String, Object> voteMap = parser.parseMap(voteJson);
    if (!(voteMap.get(REP_CODE_KEY) instanceof String)
        || !(voteMap.get(VOTE_VALUE_KEY) instanceof String)) {
      throw new VoteException(String.format(VOTE_FORMAT_INVALID, voteJson));
    }
    repCodeValidator.validateRepresentativeCode((String) voteMap.get(REP_CODE_KEY));
    voteValueValidator.validateVoteValue((String) voteMap.get(VOTE_VALUE_KEY));
  }
}
