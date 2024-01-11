package com.peterczigany.vote.repository;

import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionType;
import java.time.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
  boolean existsByTime(ZonedDateTime time);

  Optional<VotingSession> findById(String id);

  @Query(
      "SELECT vs FROM VotingSession vs WHERE vs.votingSessionType = :votingSessionType "
          + "AND vs.time < :time ORDER BY vs.time DESC LIMIT 1")
  Optional<VotingSession> findLatestPresenceVotingSessionBefore(
      @Param("votingSessionType") VotingSessionType votingSessionType,
      @Param("time") ZonedDateTime time);

  @Query(
      "SELECT vs FROM VotingSession vs WHERE vs.time >= :beginningOfDay "
          + "AND vs.time <= :endOfDay ORDER BY vs.time")
  List<VotingSession> findVotingSessionsBetweenTimes(
      @Param("beginningOfDay") ZonedDateTime beginningOfDay,
      @Param("endOfDay") ZonedDateTime endOfDay);

  default List<VotingSession> findDailyVotingSessions(LocalDate localDate) {
    ZonedDateTime beginningOfDay = localDate.atStartOfDay(ZoneId.systemDefault());
    ZonedDateTime endOfDay =
        LocalDateTime.of(localDate, LocalTime.MAX).atZone(ZoneId.systemDefault());

    return findVotingSessionsBetweenTimes(beginningOfDay, endOfDay);
  }
}
