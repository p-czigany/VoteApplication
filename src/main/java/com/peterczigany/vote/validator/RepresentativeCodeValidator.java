package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;

public class RepresentativeCodeValidator {

  static final String REP_CODE_NULL = "A képviselő kódja nem lehet null. Megadása kötelező.";
  static final String REP_CODE_EMPTY =
      "A képviselő kódja nem lehet üres. Megadása kötelező.";

  public void validateRepresentativeCode(String typeString) throws VoteException {
    if (typeString == null) {
      throw new VoteException(REP_CODE_NULL);
    }
    if (typeString.isEmpty()) {
      throw new VoteException(REP_CODE_EMPTY);
    }
  }
}
