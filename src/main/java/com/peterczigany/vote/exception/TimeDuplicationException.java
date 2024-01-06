package com.peterczigany.vote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TimeDuplicationException extends VoteException {
  public TimeDuplicationException(String message) {
    super(message);
  }
}
