package com.peterczigany.vote.repository;

import com.peterczigany.vote.model.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {}
