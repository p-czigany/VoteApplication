package com.peterczigany.vote.exception;

public class VotingSessionNotFoundException extends VoteException {
  public VotingSessionNotFoundException(String message) {
    super(message);
  }
}
