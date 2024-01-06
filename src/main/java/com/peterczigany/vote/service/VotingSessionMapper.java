package com.peterczigany.vote.service;

import com.peterczigany.vote.model.Vote;
import com.peterczigany.vote.model.VoteDTO;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class VotingSessionMapper {

  public VotingSession mapToEntity(VotingSessionDTO dto) {
    return new VotingSession(
        dto.time(),
        dto.subject(),
        dto.votingSessionType(),
        dto.chair(),
        mapVoteDTOsToEntities(dto.voteDTOs()));
  }

  private List<Vote> mapVoteDTOsToEntities(List<VoteDTO> dtos) {
    return dtos.stream()
        .map(voteDTO -> new Vote(voteDTO.representative(), voteDTO.voteValue()))
        .collect(Collectors.toList());
  }
}
