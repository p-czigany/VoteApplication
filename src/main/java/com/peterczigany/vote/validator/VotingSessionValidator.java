package com.peterczigany.vote.validator;

import static com.peterczigany.vote.validator.VoteValidator.REP_CODE_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.peterczigany.vote.exception.VoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class VotingSessionValidator {

  private static final String TIME_KEY = "idopont";
  private static final String SUBJECT_KEY = "targy";
  private static final String TYPE_KEY = "tipus";
  private static final String CHAIR_KEY = "elnok";
  private static final String VOTES_KEY = "szavazatok";

  private static final String VOTING_SESSION_FORMAT_INVALID =
      "A szavazás formája nem megfelelő: %s";
  private static final String VOTING_SESSION_CHAIR_DID_NOT_VOTE = "Az elnök (%s) nem szavazott.";
  private static final String VOTING_SESSION_REP_WITH_MULTIPLE_VOTES =
      "Ennek a képviselőnek több szavazata van: %s";

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

      validateJsonStringField(votingSessionNode, TIME_KEY, timeValidator);
      validateJsonStringField(votingSessionNode, SUBJECT_KEY, subjectValidator);
      validateJsonStringField(votingSessionNode, TYPE_KEY, typeValidator);
      validateJsonStringField(votingSessionNode, CHAIR_KEY, repCodeValidator);
      validateChairVote(votingSessionNode);
      validateVotes(votingSessionNode.get(VOTES_KEY));

    } catch (JsonProcessingException | NullPointerException e) {
      throw new VoteException(String.format(VOTING_SESSION_FORMAT_INVALID, votingSessionJson));
    }
  }

  private void validateJsonStringField(JsonNode node, String key, Validator validator)
      throws VoteException {
    String value = node.get(key).textValue();
    validator.validate(value);
  }

  private void validateChairVote(JsonNode votingSessionNode) throws VoteException {
    String chair = votingSessionNode.get(CHAIR_KEY).textValue();
    Iterator<JsonNode> voteNodes = votingSessionNode.get(VOTES_KEY).elements();
    while (voteNodes.hasNext()) {
      if (chair.equals(voteNodes.next().get(REP_CODE_KEY).textValue())) {
        return;
      }
    }
    throw new VoteException(String.format(VOTING_SESSION_CHAIR_DID_NOT_VOTE, chair));
  }

  private void validateVotes(JsonNode votesNode) throws VoteException {
    Set<String> representatives = new HashSet<>();
    for (JsonNode voteNode : votesNode) {
      voteValidator.validateVote(voteNode.toString());
      String rep = voteNode.get(REP_CODE_KEY).textValue();
      boolean duplication = !representatives.add(rep);
      if (duplication) {
        throw new VoteException(String.format(VOTING_SESSION_REP_WITH_MULTIPLE_VOTES, rep));
      }
    }
  }
}
