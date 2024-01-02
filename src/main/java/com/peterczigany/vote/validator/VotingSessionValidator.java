package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

@Service
public class VotingSessionValidator {

  private static final String TIME_KEY = "idopont";
  private static final String SUBJECT_KEY = "targy";
  private static final String TYPE_KEY = "tipus";
  private static final String CHAIR_KEY = "elnok";
  private static final String VOTES_KEY = "szavazatok";

  private static final String VOTING_SESSION_FORMAT_INVALID =
      "A szavazás formája nem megfelelő: %s";
  private final TimeValidator timeValidator;
  private final SubjectValidator subjectValidator;
  private final TypeValidator typeValidator;
  private final RepresentativeCodeValidator repCodeValidator;
  private final VoteValidator voteValidator;
  private final JsonParser jsonParser;

  @Autowired
  public VotingSessionValidator(
      TimeValidator timeValidator,
      SubjectValidator subjectValidator,
      TypeValidator typeValidator,
      RepresentativeCodeValidator repCodeValidator,
      VoteValidator voteValidator) {
    this.timeValidator = timeValidator;
    this.subjectValidator = subjectValidator;
    this.typeValidator = typeValidator;
    this.repCodeValidator = repCodeValidator;
    this.voteValidator = voteValidator;
    this.jsonParser = new BasicJsonParser();
  }

  public void validateVotingSession(String votingSessionJson) throws VoteException {
    try {
      Map<String, Object> votingSessionMap = jsonParser.parseMap(votingSessionJson);
      timeValidator.validateTime((String) votingSessionMap.get(TIME_KEY));
      subjectValidator.validateSubject((String) votingSessionMap.get(SUBJECT_KEY));
      typeValidator.validateType((String) votingSessionMap.get(TYPE_KEY));
      repCodeValidator.validateRepresentativeCode((String) votingSessionMap.get(CHAIR_KEY));
      Object voteListJson = votingSessionMap.get(VOTES_KEY);
      System.out.println(voteListJson.toString());

      //      List<Object> voteList = jsonParser.parseList(voteListJson);
      //      for (Object vote : voteList) {
      //        voteValidator.validateVote((String) vote);
      //      }
    } catch (JsonParseException | ClassCastException | NullPointerException e) {
      throw new VoteException(String.format(VOTING_SESSION_FORMAT_INVALID, votingSessionJson));
    }
  }
}
