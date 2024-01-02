package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;
import java.util.List;

public class TypeValidator implements Validator {

  static final String TYPE_NULL = "A típus nem lehet null. Megadása kötelező.";
  static final String TYPE_EMPTY = "A típus nem lehet üres. Megadása kötelező.";

  static final String TYPE_NOT_VALID = "A típus nem lehet %s. Csak 'j', 'e' és 'm' adható meg.";

  private static final List<String> VOTE_TYPES = List.of("j", "e", "m");

  public void validate(String typeString) throws VoteException {
    if (typeString == null) {
      throw new VoteException(TYPE_NULL);
    }
    if (typeString.isEmpty()) {
      throw new VoteException(TYPE_EMPTY);
    }
    if (!VOTE_TYPES.contains(typeString)) {
      throw new VoteException(String.format(TYPE_NOT_VALID, typeString));
    }
  }
}
