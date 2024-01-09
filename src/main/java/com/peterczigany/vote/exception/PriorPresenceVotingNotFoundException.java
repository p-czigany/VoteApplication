package com.peterczigany.vote.exception;

public class PriorPresenceVotingNotFoundException extends VotingSessionNotFoundException {
  public PriorPresenceVotingNotFoundException(String message) {
    super(message);
  }
}
