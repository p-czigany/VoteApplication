package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;
import java.util.List;

public class VoteValueValidator {

  static final String VOTE_VALUE_NULL = "A szavazat értéke nem lehet null. Megadása kötelező.";
  static final String VOTE_VALUE_EMPTY = "A szavazat értéke nem lehet üres. Megadása kötelező.";
  static final String VOTE_VALUE_NOT_VALID =
      "A szavazat értéke nem lehet %s. Csak 'i', 'n' és 't' adható meg.";

  private static final List<String> VOTE_TYPES = List.of("i", "n", "t");

  public void validateVoteValue(String typeString) throws VoteException {
    if (typeString == null) {
      throw new VoteException(VOTE_VALUE_NULL);
    }
    if (typeString.isEmpty()) {
      throw new VoteException(VOTE_VALUE_EMPTY);
    }
    if (!VOTE_TYPES.contains(typeString)) {
      throw new VoteException(String.format(VOTE_VALUE_NOT_VALID, typeString));
    }
  }
}
