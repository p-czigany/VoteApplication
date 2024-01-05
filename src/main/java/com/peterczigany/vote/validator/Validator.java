package com.peterczigany.vote.validator;

import com.peterczigany.vote.exception.VoteException;

public interface Validator {
  void validate(String valueString) throws VoteException;
}
