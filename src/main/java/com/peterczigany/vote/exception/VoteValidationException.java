package com.peterczigany.vote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class VoteValidationException extends VoteException {
  public VoteValidationException(String message) {
    super(message);
  }
}
