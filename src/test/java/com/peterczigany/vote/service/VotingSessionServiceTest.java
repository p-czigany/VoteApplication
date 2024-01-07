package com.peterczigany.vote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.peterczigany.vote.TestUtils;
import com.peterczigany.vote.exception.VoteException;
import com.peterczigany.vote.exception.VoteNotFoundException;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.repository.VotingSessionRepository;
import com.peterczigany.vote.response.CreationResponse;
import com.peterczigany.vote.response.VoteResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VotingSessionServiceTest {
  @Mock private VotingSessionRepository repository;
  @Mock private VotingSessionMapper mapper;
  @InjectMocks private VotingSessionService service;

  @Test
  void testSuccessfullyCreateVotingSession() throws VoteException {
    VotingSessionDTO dto = TestUtils.validVotingSessionDTO();
    VotingSession votingSession = TestUtils.validVotingSession();
    Mockito.when(mapper.mapToEntity(dto)).thenReturn(votingSession);

    votingSession.setId("ABC123");
    CreationResponse expectation = new CreationResponse(votingSession.getId());
    Mockito.when(repository.existsByTime(votingSession.getTime())).thenReturn(false);
    Mockito.when(repository.save(votingSession)).thenReturn(votingSession);

    assertThat(service.createVotingSession(dto)).isEqualTo(expectation);
  }

  @Test
  void testFailToCreateVotingSessionBecauseTimeIsDuplicated() {
    VotingSessionDTO dto = TestUtils.validVotingSessionDTO();
    VotingSession votingSession = TestUtils.validVotingSession();

    Mockito.when(repository.existsByTime(votingSession.getTime())).thenReturn(true);

    assertThatThrownBy(() -> service.createVotingSession(dto)).isInstanceOf(Exception.class);
  }

  @Test
  void testGetIndividualVoteSuccessfully() throws VoteNotFoundException {
    Mockito.when(repository.findById(any(String.class)))
        .thenReturn(Optional.of(TestUtils.validVotingSession()));
    VoteResponse response = new VoteResponse("i");

    assertThat(service.getIndividualVote("ABC123", "Kepviselo1")).isEqualTo(response);
  }

  @Test
  void testVotingSessionNotFound() {
    Mockito.when(repository.findById(any(String.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getIndividualVote("ABC123", "Kepviselo1"))
        .isInstanceOf(VoteNotFoundException.class);
  }

  @Test
  void testRepresentativeDidNotVoteThatSession() {
    Mockito.when(repository.findById(any(String.class)))
        .thenReturn(Optional.of(TestUtils.validVotingSession()));

    assertThatThrownBy(() -> service.getIndividualVote("ABC123", "Kepviselo10"))
        .isInstanceOf(VoteNotFoundException.class);
  }
}