package com.peterczigany.vote.validator;

import com.peterczigany.vote.VoteException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class TimeValidator {

  static final String TIME_NULL = "Az időpont nem lehet null. Megadása kötelező.";
  static final String TIME_EMPTY = "Az időpont nem lehet üres. Megadása kötelező.";
  static final String TIME_BAD_FORMAT =
      "Az alábbi időpont formátuma nem megfelelő:\n%s\n(MSZ ISO 8601:2003 formátumban szükséges megadni.)";

  @SuppressWarnings("ResultOfMethodCallIgnored") // parsing time string to see if it's possible
  public void validateTime(String timeString) throws VoteException {
    if (timeString == null) {
      throw new VoteException(TIME_NULL);
    }
    if (timeString.isEmpty()) {
      throw new VoteException(TIME_EMPTY);
    }
    try {
      ZonedDateTime.parse(timeString.replace(" ", "T"));
    } catch (DateTimeParseException e) {
      throw new VoteException(String.format(TIME_BAD_FORMAT, timeString));
    }
  }
}
