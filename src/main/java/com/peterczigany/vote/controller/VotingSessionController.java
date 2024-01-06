package com.peterczigany.vote.controller;

import com.peterczigany.vote.exception.VoteException;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.response.CreationResponse;
import com.peterczigany.vote.response.VoteResponse;
import com.peterczigany.vote.service.VotingSessionService;
import java.time.format.DateTimeParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class VotingSessionController {
  private final VotingSessionService service;

  @Autowired
  public VotingSessionController(VotingSessionService service) {
    this.service = service;
  }

  @PostMapping(value = "/szavazasok/szavazas", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public CreationResponse createVotingSession(@RequestBody final VotingSessionDTO votingSession)
      throws VoteException {
    return service.createVotingSession(votingSession);
  }

  @GetMapping(value = "")
  @ResponseStatus(HttpStatus.OK)
  public String hello() {
    return "hello";
  }

  @ExceptionHandler({
    IllegalArgumentException.class,
    NullPointerException.class,
    DateTimeParseException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handleException() {}

  @GetMapping(value = "/szavazasok/szavazas")
  @ResponseStatus(HttpStatus.OK)
  public VoteResponse getIndividualVote(
      @RequestParam("szavazas") String votingSessionId,
      @RequestParam("kepviselo") String representative) {
    return service.getIndividualVote(votingSessionId, representative);
  }
}
