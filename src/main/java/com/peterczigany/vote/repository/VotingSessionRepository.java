package com.peterczigany.vote.repository;

import com.peterczigany.vote.model.VotingSession;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
  boolean existsByTime(ZonedDateTime time);

  Optional<VotingSession> findById(String id);
}