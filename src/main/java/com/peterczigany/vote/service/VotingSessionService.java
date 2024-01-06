package com.peterczigany.vote.service;

import com.peterczigany.vote.exception.TimeDuplicationException;
import com.peterczigany.vote.exception.VoteException;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.repository.VotingSessionRepository;
import com.peterczigany.vote.response.CreationResponse;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotingSessionService {
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
}
