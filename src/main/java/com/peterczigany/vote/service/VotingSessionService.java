package com.peterczigany.vote.service;

import com.peterczigany.vote.exception.*;
import com.peterczigany.vote.model.Vote;
import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.model.VotingSessionType;
import com.peterczigany.vote.repository.VotingSessionRepository;
import com.peterczigany.vote.response.*;
import com.peterczigany.vote.response.DailyVotingSessionsResponse.DailyVotingSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
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

    return evaluateVotingSession(votingSession);
  }

  private VotingSessionResultResponse evaluateVotingSession(VotingSession votingSession)
      throws PriorPresenceVotingNotFoundException {
    VotingSessionType sessionType = votingSession.getVotingSessionType();

    return switch (sessionType) {
      case PRESENCE -> createResultResponse(ResultValue.ACCEPTED, votingSession);
      case MAJORITY -> majorityResult(votingSession);
      case SUPERMAJORITY -> evaluateNumberOfVotes(
          votingSession, TOTAL_NUMBER_OF_MEMBERS_OF_PARLIAMENT);
    };
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
            .findLatestPresenceVotingSessionBefore(
                VotingSessionType.PRESENCE, votingSession.getTime())
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

  public DailyVotingSessionsResponse getDailyVotingSessions(String date) {
    List<VotingSession> dailyVotingSessions =
        repository.findDailyVotingSessions(LocalDate.parse(date));

    if (dailyVotingSessions.isEmpty())
      return new DailyVotingSessionsResponse(Collections.emptyList());

    return new DailyVotingSessionsResponse(
        dailyVotingSessions.stream()
            .map(
                voting -> {
                  try {
                    return new DailyVotingSession(
                        voting.getTime(),
                        voting.getSubject(),
                        voting.getVotingSessionType(),
                        voting.getChair(),
                        evaluateVotingSession(voting).result(),
                        voting.countTotalVotes(),
                        mapper.mapVotesToDTOs(voting.getVotes()));
                  } catch (VotingSessionNotFoundException e) {
                    throw new VotingRuntimeException(e);
                  }
                })
            .toList());
  }

  public AverageParticipationResponse getAverageParticipation(
      String representative, String startDayString, String endDayString) {
    LocalDate startDay = LocalDate.parse(startDayString);
    LocalDate endDay = LocalDate.parse(endDayString);

    double average =
        (double)
                repository.countVotingSessionsByRepresentativeBetweenDays(
                    representative, startDay, endDay)
            / startDay.datesUntil(endDay.plusDays(1)).count();

    BigDecimal bd = new BigDecimal(Double.toString(average));
    bd = bd.setScale(2, RoundingMode.HALF_UP);

    return new AverageParticipationResponse(bd.doubleValue());
  }
}
