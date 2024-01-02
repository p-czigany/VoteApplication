package com.peterczigany.vote.validator;

import static com.peterczigany.vote.validator.VoteValidator.REP_CODE_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.peterczigany.vote.VoteException;
import org.springframework.beans.factory.annotation.Autowired;
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
  private static final String VOTING_SESSION_CHAIR_DID_NOT_VOTE = "Az elnök (%s) nem szavazott.";

  private final TimeValidator timeValidator;
  private final SubjectValidator subjectValidator;
  private final TypeValidator typeValidator;
  private final RepresentativeCodeValidator repCodeValidator;
  private final VoteValidator voteValidator;
  private final ObjectMapper mapper;

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
    this.mapper = new JsonMapper();
  }

  public void validateVotingSession(String votingSessionJson) throws VoteException {
    try {
      JsonNode votingSessionNode = mapper.readTree(votingSessionJson);
      timeValidator.validateTime(votingSessionNode.get(TIME_KEY).textValue());
      subjectValidator.validateSubject(votingSessionNode.get(SUBJECT_KEY).textValue());
      typeValidator.validateType(votingSessionNode.get(TYPE_KEY).textValue());
      String chair = votingSessionNode.get(CHAIR_KEY).textValue();
      repCodeValidator.validateRepresentativeCode(chair);
      JsonNode votesNode = votingSessionNode.get(VOTES_KEY);
      boolean didChairVote = false;
      for (JsonNode voteNode : votesNode) {
        if (chair.equals(voteNode.get(REP_CODE_KEY).textValue())) {
          didChairVote = true;
        }
        voteValidator.validateVote(voteNode.toString());
      }
      if (!didChairVote) {
        throw new VoteException(String.format(VOTING_SESSION_CHAIR_DID_NOT_VOTE, chair));
      }
    } catch (JsonProcessingException | NullPointerException e) {
      throw new VoteException(String.format(VOTING_SESSION_FORMAT_INVALID, votingSessionJson));
    }
  }
}
