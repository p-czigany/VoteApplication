package com.peterczigany.vote;

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
