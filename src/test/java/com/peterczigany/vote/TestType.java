package com.peterczigany.vote;

import com.peterczigany.vote.exception.VoteException;

class TestType {

  String getOk() {
    return "ok";
  }

  boolean getTrue() {
    return true;
  }

  void throwException() throws VoteException {
    throw new VoteException("test exception");
  }
}
