package com.peterczigany.vote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.peterczigany.vote.exception.VoteException;
import com.peterczigany.vote.model.Vote;
import com.peterczigany.vote.model.VoteDTO;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.repository.VotingSessionRepository;
import com.peterczigany.vote.response.CreationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

class VotingSessionServiceTest {
  @MockBean private VotingSessionRepository repository;

  @MockBean private VotingSessionMapper mapper;

  @Autowired private VotingSessionService service;

  @Test
  void testSuccessfullyCreateVotingSession() throws VoteException {
      // votingSessionDTO
      VoteDTO voteDTO1 = new VoteDTO("Kepviselo1", "i");
      List<VoteDTO> voteDTOs = List.of(
              voteDTO1, new VoteDTO("Kepviselo2", "n"), new VoteDTO("Kepviselo3", "t"));
      VotingSessionDTO dto =
              new VotingSessionDTO("2023-12-23 14:30:45Z", "Subject of Voting", "j", "Kepviselo1",
                      voteDTOs);
      Vote vote1 = new Vote("Kepviselo1", "i");
      // votingSessionEntity
      List<Vote> votes = List.of(
              vote1, new Vote("Kepviselo2", "n"), new Vote("Kepviselo3", "t"));
      VotingSession votingSession = new VotingSession(dto.time(), dto.subject(), dto.votingSessionType(), dto.chair(), votes);

      votingSession.setId("ABC123");
      VotingSession expectation = votingSession;
      Mockito.when(repository.save(votingSession)).thenReturn(expectation);

      assertThat(service.createVotingSession(dto)).isEqualTo(expectation);
  }

  @Test
  void testFailToCreateVotingSessionBecauseTimeIsDuplicated() {}
}
