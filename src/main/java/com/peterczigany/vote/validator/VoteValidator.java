package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;

public class VoteValidator {

  static final String REP_CODE_KEY = "kepviselo";
  private static final String VOTE_VALUE_KEY = "szavazat";
  private static final String VOTE_FORMAT_INVALID = "Az alábbi szavazat formája nem megfelelő: %s";

  private final RepresentativeCodeValidator repCodeValidator;
  private final VoteValueValidator voteValueValidator;
  private final JsonParser jsonParser;

  @Autowired
  public VoteValidator(
      RepresentativeCodeValidator repCodeValidator, VoteValueValidator voteValueValidator) {
    this.repCodeValidator = repCodeValidator;
    this.voteValueValidator = voteValueValidator;
    this.jsonParser = new BasicJsonParser();
  }

  public void validateVote(String voteJson) throws VoteException {
    try {
      Map<String, Object> voteMap = jsonParser.parseMap(voteJson);
      repCodeValidator.validate((String) voteMap.get(REP_CODE_KEY));
      voteValueValidator.validate((String) voteMap.get(VOTE_VALUE_KEY));
    } catch (JsonParseException | ClassCastException e) {
      throw new VoteException(String.format(VOTE_FORMAT_INVALID, voteJson));
    }
  }
}
