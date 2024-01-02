package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;

public interface Validator {
  void validate(String valueString) throws VoteException;
}
