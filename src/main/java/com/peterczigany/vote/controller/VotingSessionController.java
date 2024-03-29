package com.peterczigany.vote.controller;

import com.peterczigany.vote.exception.VoteException;
import com.peterczigany.vote.exception.VoteNotFoundException;
import com.peterczigany.vote.exception.VotingSessionNotFoundException;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.response.*;
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
      @RequestParam("kepviselo") String representative)
      throws VoteNotFoundException {
    return service.getIndividualVote(votingSessionId, representative);
  }

  @ExceptionHandler({VoteNotFoundException.class, VotingSessionNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleNotFoundException() {}

  @GetMapping(value = "/szavazasok/eredmeny")
  @ResponseStatus(HttpStatus.OK)
  public VotingSessionResultResponse getVotingSessionResult(
      @RequestParam("szavazas") String votingSessionId) throws VotingSessionNotFoundException {
    return service.getVotingSessionResult(votingSessionId);
  }

  @GetMapping(value = "/szavazasok/napi-szavazasok")
  @ResponseStatus(HttpStatus.OK)
  public DailyVotingSessionsResponse getDailyVotingSessions(@RequestParam("nap") String date) {
    return service.getDailyVotingSessions(date);
  }

  @GetMapping(value = "/szavazasok/kepviselo-reszvetel-atlag", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public AverageParticipationResponse getAverageParticipation(
      @RequestParam("kepviselo") String representative,
      @RequestParam("idoszakKezdete") String startDay,
      @RequestParam("idoszakVege") String endDay) {
    return service.getAverageParticipation(representative, startDay, endDay);
  }
}
