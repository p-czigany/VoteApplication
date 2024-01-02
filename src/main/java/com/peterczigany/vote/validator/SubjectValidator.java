package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;

public class SubjectValidator implements Validator {

  static final String SUBJECT_NULL = "A szavazás tárgya nem lehet null. Megadása kötelező.";
  static final String SUBJECT_EMPTY = "A szavazás tárgya nem lehet üres. Megadása kötelező.";

  public void validate(String typeString) throws VoteException {
    if (typeString == null) {
      throw new VoteException(SUBJECT_NULL);
    }
    if (typeString.isEmpty()) {
      throw new VoteException(SUBJECT_EMPTY);
    }
  }
}
