package com.peterczigany.vote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.peterczigany.vote.TestUtils;
import com.peterczigany.vote.exception.PriorPresenceVotingNotFoundException;
import com.peterczigany.vote.exception.VoteException;
import com.peterczigany.vote.exception.VoteNotFoundException;
import com.peterczigany.vote.exception.VotingSessionNotFoundException;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.model.VotingSessionType;
import com.peterczigany.vote.repository.VotingSessionRepository;
import com.peterczigany.vote.response.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    when(mapper.mapToEntity(dto)).thenReturn(votingSession);

    votingSession.setId("ABC123");
    CreationResponse expectation = new CreationResponse(votingSession.getId());
    when(repository.existsByTime(votingSession.getTime())).thenReturn(false);
    when(repository.save(votingSession)).thenReturn(votingSession);

    assertThat(service.createVotingSession(dto)).isEqualTo(expectation);
  }

  @Test
  void testFailToCreateVotingSessionBecauseTimeIsDuplicated() {
    VotingSessionDTO dto = TestUtils.validVotingSessionDTO();
    VotingSession votingSession = TestUtils.validVotingSession();

    when(repository.existsByTime(votingSession.getTime())).thenReturn(true);

    assertThatThrownBy(() -> service.createVotingSession(dto)).isInstanceOf(Exception.class);
  }

  @Test
  void testGetIndividualVoteSuccessfully() throws VoteNotFoundException {
    when(repository.findById(any(String.class)))
        .thenReturn(Optional.of(TestUtils.validVotingSession()));
    VoteResponse response = new VoteResponse("i");

    assertThat(service.getIndividualVote("ABC123", "Kepviselo1")).isEqualTo(response);
  }

  @Test
  void testVotingSessionNotFound() {
    when(repository.findById(any(String.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getIndividualVote("ABC123", "Kepviselo1"))
        .isInstanceOf(VoteNotFoundException.class);
  }

  @Test
  void testRepresentativeDidNotVoteThatSession() {
    when(repository.findById(anyString())).thenReturn(Optional.of(TestUtils.validVotingSession()));

    assertThatThrownBy(() -> service.getIndividualVote("ABC123", "Kepviselo10"))
        .isInstanceOf(VoteNotFoundException.class);
  }

  @Test
  void testGetVotingSessionResult_whenVotingSessionIsNotFound() {
    when(repository.findById(anyString())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getVotingSessionResult("ABC123"))
        .isInstanceOf(VotingSessionNotFoundException.class);
  }

  @Test
  void testGetVotingSessionResult_whenTypeIsPresence() throws VotingSessionNotFoundException {
    String votingSessionId = "ABC123";
    VotingSessionResultResponse expectedResponse =
        new VotingSessionResultResponse(ResultValue.ACCEPTED, 3, 1, 1, 1);
    when(repository.findById(anyString())).thenReturn(Optional.of(TestUtils.validVotingSession()));

    VotingSessionResultResponse actualResponse = service.getVotingSessionResult(votingSessionId);

    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  void testGetVotingSessionResultReject_whenTypeIsSupermajority()
      throws VotingSessionNotFoundException {
    String votingSessionId = "ABC123";
    VotingSessionResultResponse expectedResponse =
        new VotingSessionResultResponse(ResultValue.REJECTED, 3, 1, 1, 1);
    VotingSession votingSession = TestUtils.validVotingSession();
    votingSession.setVotingSessionType(VotingSessionType.SUPERMAJORITY);
    when(repository.findById(anyString())).thenReturn(Optional.of(votingSession));

    VotingSessionResultResponse actualResponse = service.getVotingSessionResult(votingSessionId);

    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  void testGetVotingSessionResultAccept_whenTypeIsSupermajority()
      throws VotingSessionNotFoundException {
    String votingSessionId = "ABC123";
    VotingSessionResultResponse expectedResponse =
        new VotingSessionResultResponse(ResultValue.ACCEPTED, 101, 101, 0, 0);
    when(repository.findById(anyString()))
        .thenReturn(Optional.of(TestUtils.supermajorityVotingSession()));

    assertThat(service.getVotingSessionResult(votingSessionId)).isEqualTo(expectedResponse);
  }

  @Test
  void testGetVotingSessionResultAccept_whenTypeIsMajority() throws VotingSessionNotFoundException {
    String votingSessionId = "ABC123";
    VotingSessionResultResponse expectedResponse =
        new VotingSessionResultResponse(ResultValue.ACCEPTED, 10, 6, 2, 2);

    when(repository.findById(anyString()))
        .thenReturn(Optional.of(TestUtils.majorityVotingSession(6, 2, 2)));
    when(repository.findLatestPresenceVotingSessionBefore(
            any(VotingSessionType.class), any(ZonedDateTime.class)))
        .thenReturn(Optional.of(TestUtils.sessionWithXPresent(10)));

    assertThat(service.getVotingSessionResult(votingSessionId)).isEqualTo(expectedResponse);
  }

  @Test
  void testGetVotingSessionResultReject_whenTypeIsMajority() throws VotingSessionNotFoundException {
    String votingSessionId = "ABC123";
    VotingSessionResultResponse expectedResponse =
        new VotingSessionResultResponse(ResultValue.REJECTED, 10, 6, 2, 2);

    when(repository.findById(anyString()))
        .thenReturn(Optional.of(TestUtils.majorityVotingSession(6, 2, 2)));
    when(repository.findLatestPresenceVotingSessionBefore(
            any(VotingSessionType.class), any(ZonedDateTime.class)))
        .thenReturn(Optional.of(TestUtils.sessionWithXPresent(12)));

    assertThat(service.getVotingSessionResult(votingSessionId)).isEqualTo(expectedResponse);
  }

  @Test
  void testGetVotingSessionResultFail_whenTypeIsMajorityAndNoPreviousPresence() {
    when(repository.findById(anyString()))
        .thenReturn(Optional.of(TestUtils.majorityVotingSession(6, 2, 2)));
    when(repository.findLatestPresenceVotingSessionBefore(
            any(VotingSessionType.class), any(ZonedDateTime.class)))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getVotingSessionResult("ABC123"))
        .isInstanceOf(PriorPresenceVotingNotFoundException.class);
  }

  @Test
  void testGetDailyVotingSessions() {
    VotingSession voting1 = TestUtils.validVotingSession();
    VotingSession voting2 = TestUtils.validVotingSession();
    voting2.setTime(voting2.getTime().plusHours(1));
    when(repository.findDailyVotingSessions(any())).thenReturn(List.of(voting1, voting2));
    VotingSessionDTO votingDTO = TestUtils.validVotingSessionDTO();
    when(mapper.mapVotesToDTOs(voting1.getVotes())).thenReturn(votingDTO.voteDTOs());
    when(mapper.mapVotesToDTOs(voting2.getVotes())).thenReturn(votingDTO.voteDTOs());

    assertThat(service.getDailyVotingSessions("2023-09-28"))
        .isEqualTo(
            new DailyVotingSessionsResponse(
                List.of(
                    new DailyVotingSessionsResponse.DailyVotingSession(
                        votingDTO.time(),
                        votingDTO.subject(),
                        votingDTO.votingSessionType(),
                        votingDTO.chair(),
                        ResultValue.ACCEPTED,
                        3L,
                        votingDTO.voteDTOs()),
                    new DailyVotingSessionsResponse.DailyVotingSession(
                        votingDTO.time().plusHours(1),
                        votingDTO.subject(),
                        votingDTO.votingSessionType(),
                        votingDTO.chair(),
                        ResultValue.ACCEPTED,
                        3L,
                        votingDTO.voteDTOs()))));
  }

  @Test
  void testRepresentativeAverageParticipation() {
    String representative = "Kepviselo2";
    String startDayAsString = "2023-09-27";
    String endDayAsString = "2023-09-29";

    LocalDate startDay = LocalDate.parse(startDayAsString);
    LocalDate endDay = LocalDate.parse(endDayAsString);

    when(repository.countVotingSessionsByRepresentativeBetweenDays(
            representative, startDay, endDay))
        .thenReturn(2L);

    assertThat(service.getAverageParticipation(representative, startDayAsString, endDayAsString))
        .isEqualTo(new AverageParticipationResponse(0.67d));
  }
}
