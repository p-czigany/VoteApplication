package com.peterczigany.vote.service;

import com.peterczigany.vote.exception.*;
import com.peterczigany.vote.model.Vote;
import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.model.VotingSessionType;
import com.peterczigany.vote.repository.VotingSessionRepository;
import com.peterczigany.vote.response.CreationResponse;
import com.peterczigany.vote.response.VoteResponse;
import com.peterczigany.vote.response.VotingSessionResultResponse;
import com.peterczigany.vote.response.VotingSessionResultResponse.ResultValue;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotingSessionService {

  private static final long TOTAL_NUMBER_OF_MEMBERS_OF_PARLIAMENT = 200;
  private final VotingSessionRepository repository;
  private final VotingSessionMapper mapper;

  @Autowired
  public VotingSessionService(VotingSessionRepository repository, VotingSessionMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public CreationResponse createVotingSession(VotingSessionDTO dto) throws VoteException {
    validateTime(dto.time());
    VotingSession votingSession = mapper.mapToEntity(dto);
    VotingSession saved = repository.save(votingSession);
    return new CreationResponse(saved.getId());
  }

  private void validateTime(ZonedDateTime time) throws VoteException {
    if (repository.existsByTime(time)) {
      throw new TimeDuplicationException("Erre az időpontra már van elmentett szavazat.");
    }
  }

  public VoteResponse getIndividualVote(String votingSessionId, String representative)
      throws VoteNotFoundException {
    Optional<VotingSession> votingSession = repository.findById(votingSessionId);
    if (votingSession.isPresent()) {
      Optional<Vote> individualVote =
          votingSession.get().getVotes().stream()
              .filter(vote -> representative.equals(vote.getRepresentative()))
              .findFirst();
      if (individualVote.isPresent()) {
        return new VoteResponse(individualVote.get().getVoteValue().label);
      }
    }
    throw new VoteNotFoundException("Nem található ilyen szavazat.");
  }

  public VotingSessionResultResponse getVotingSessionResult(String votingSessionId)
      throws VotingSessionNotFoundException {
    VotingSession votingSession =
        repository
            .findById(votingSessionId)
            .orElseThrow(() -> new VotingSessionNotFoundException("Nem található ilyen szavazás"));

    if (votingSession.getVotingSessionType().equals(VotingSessionType.PRESENCE)) {
      return createResultResponse(ResultValue.ACCEPTED, votingSession);
    }
    if (votingSession.getVotingSessionType().equals(VotingSessionType.SUPERMAJORITY)) {
      return evaluateNumberOfVotes(votingSession, TOTAL_NUMBER_OF_MEMBERS_OF_PARLIAMENT);
    }
    if (votingSession.getVotingSessionType().equals(VotingSessionType.MAJORITY)) {
      return majorityResult(votingSession);
    }
    return null;
  }

  private VotingSessionResultResponse evaluateNumberOfVotes(
      VotingSession votingSession, long totalRepresentatives) {
    if ((votingSession.countVotes(VoteValue.FOR) * 2) > totalRepresentatives) {
      return createResultResponse(ResultValue.ACCEPTED, votingSession);
    }
    return createResultResponse(ResultValue.REJECTED, votingSession);
  }

  private VotingSessionResultResponse majorityResult(VotingSession votingSession)
      throws PriorPresenceVotingNotFoundException {
    VotingSession priorPresenceVoting =
        repository
            .findLatestPresenceVotingSessionBefore(VotingSessionType.PRESENCE, votingSession.getTime())
            .orElseThrow(
                () ->
                    new PriorPresenceVotingNotFoundException(
                        "Nem található korábbi jelenléti szavazás"));
    return evaluateNumberOfVotes(votingSession, priorPresenceVoting.countTotalVotes());
  }

  private VotingSessionResultResponse createResultResponse(
      ResultValue resultValue, VotingSession votingSession) {
    return new VotingSessionResultResponse(
        resultValue,
        votingSession.countTotalVotes(),
        votingSession.countVotes(VoteValue.FOR),
        votingSession.countVotes(VoteValue.AGAINST),
        votingSession.countVotes(VoteValue.ABSTAIN));
  }
}
