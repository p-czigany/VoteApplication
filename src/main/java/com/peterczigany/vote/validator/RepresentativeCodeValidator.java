package com.peterczigany.vote.validator;

import com.peterczigany.vote.exception.VoteException;

public class RepresentativeCodeValidator implements Validator {

  static final String REP_CODE_NULL = "A képviselő kódja nem lehet null. Megadása kötelező.";
  static final String REP_CODE_EMPTY = "A képviselő kódja nem lehet üres. Megadása kötelező.";

  public void validate(String typeString) throws VoteException {
    if (typeString == null) {
      throw new VoteException(REP_CODE_NULL);
    }
    if (typeString.isEmpty()) {
      throw new VoteException(REP_CODE_EMPTY);
    }
  }
}
