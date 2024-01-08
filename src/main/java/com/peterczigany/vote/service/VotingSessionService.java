package com.peterczigany.vote.service;

import com.peterczigany.vote.exception.TimeDuplicationException;
import com.peterczigany.vote.exception.VoteException;
import com.peterczigany.vote.exception.VoteNotFoundException;
import com.peterczigany.vote.exception.VotingSessionNotFoundException;
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

  private static final int TOTAL_NUMBER_OF_MEMBERS_OF_PARLIAMENT = 200;
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
      return superMajorityResult(votingSession);
    }
    return null;
  }

  private VotingSessionResultResponse superMajorityResult(VotingSession votingSession) {
    if ((votingSession.countVotes(VoteValue.FOR) * 2) > TOTAL_NUMBER_OF_MEMBERS_OF_PARLIAMENT) {
      return createResultResponse(ResultValue.ACCEPTED, votingSession);
    }
    return createResultResponse(ResultValue.REJECTED, votingSession);
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
