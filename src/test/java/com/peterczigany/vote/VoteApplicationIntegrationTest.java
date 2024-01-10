package com.peterczigany.vote;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peterczigany.vote.model.Vote;
import com.peterczigany.vote.model.VoteValue;
import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.model.VotingSessionType;
import com.peterczigany.vote.repository.VotingSessionRepository;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class VoteApplicationIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private VotingSessionRepository repository;

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void testHello() throws Exception {
    mockMvc
        .perform(get("http://localhost:8080"))
        .andExpect(status().isOk())
        .andExpect(content().string("hello"));
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/validVotingSession.csv")
  void testSuccessfulVoteSessionCreation(String votingSessionJson) throws Exception {
    mockMvc
        .perform(
            post("http://localhost:8080/szavazasok/szavazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(votingSessionJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("szavazasId").isNotEmpty());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/validVotingSession.csv")
  void testFailingVoteSessionCreationBecauseTimeDuplication(String votingSessionJson)
      throws Exception {
    mockMvc
        .perform(
            post("http://localhost:8080/szavazasok/szavazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(votingSessionJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("szavazasId").isNotEmpty());
    mockMvc
        .perform(
            post("http://localhost:8080/szavazasok/szavazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(votingSessionJson))
        .andExpect(status().isConflict());
    // TODO: display error message in response body
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = {
        "/invalidVotingSessions/badJson.csv",
        "/invalidVotingSessions/badTimeFormat.csv",
        "/invalidVotingSessions/blankField.csv",
        "/invalidVotingSessions/invalidType.csv",
        "/invalidVotingSessions/invalidVoteValue.csv",
        "/invalidVotingSessions/missingField.csv"
      })
  void testFailingVoteSessionCreationBecauseInvalidRequest(String votingSessionJson)
      throws Exception {
    mockMvc
        .perform(
            post("http://localhost:8080/szavazasok/szavazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(votingSessionJson))
        .andExpect(status().isBadRequest());
    // TODO: display error message in response body
  }

  @Test
  void testGetVoteBySessionIdAndRepresentative() throws Exception {
    VotingSession votingSession = repository.save(TestUtils.validVotingSession());

    mockMvc
        .perform(
            get("http://localhost:8080/szavazasok/szavazas")
                .param("szavazas", votingSession.getId())
                .param("kepviselo", "Kepviselo1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("szavazat").value("i"));
  }

  @Test
  void testFailToGetVoteBySessionIdAndRepresentative() throws Exception {
    mockMvc
        .perform(
            get("http://localhost:8080/szavazasok/szavazas")
                .param("szavazas", "ABC123")
                .param("kepviselo", "Kepviselo1"))
        .andExpect(status().isNotFound());
    VotingSession votingSession = repository.save(TestUtils.validVotingSession());
    mockMvc
        .perform(
            get("http://localhost:8080/szavazasok/szavazas")
                .param("szavazas", votingSession.getId())
                .param("kepviselo", "Kepviselo9"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetVotingSessionResultById() throws Exception {
    mockMvc
        .perform(get("http://localhost:8080/szavazasok/eredmeny").param("szavazas", "ABC123"))
        .andExpect(status().isNotFound());

    VotingSession votingSession = TestUtils.validVotingSession();
    votingSession.setTime(votingSession.getTime().plusMinutes(2));
    votingSession.setVotingSessionType(VotingSessionType.MAJORITY);
    votingSession.getVotes().get(2).setVoteValue(VoteValue.FOR);
    String id = repository.save(votingSession).getId();
    mockMvc
        .perform(get("http://localhost:8080/szavazasok/eredmeny").param("szavazas", id))
        .andExpect(status().isNotFound());

    repository.save(TestUtils.validVotingSession());

    mockMvc
        .perform(get("http://localhost:8080/szavazasok/eredmeny").param("szavazas", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("eredmeny").value("F"));

    VotingSession presenceVotingSession = TestUtils.validVotingSession();
    presenceVotingSession.setTime(presenceVotingSession.getTime().plusMinutes(1));
    presenceVotingSession.setVotes(
        Stream.concat(
                presenceVotingSession.getVotes().stream(),
                Stream.of(new Vote("Kepviselo4", VoteValue.FOR)))
            .collect(Collectors.toList()));
    repository.save(presenceVotingSession);

    mockMvc
        .perform(get("http://localhost:8080/szavazasok/eredmeny").param("szavazas", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("eredmeny").value("U"));
  }

  @Test
  void testDailyVotingSessions() throws Exception {
    VotingSession votingSession = TestUtils.validVotingSession();
    votingSession.setTime(votingSession.getTime().plusMinutes(2));
    repository.save(votingSession);
    repository.save(TestUtils.validVotingSession());

    String day = "2023-09-28";

    mockMvc
        .perform(get("http://localhost:8080/szavazasok/napi-szavazasok").param("nap", day))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.szavazasok").isArray())
        .andExpect(jsonPath("$.szavazasok.length()").value(2))
        .andExpect(jsonPath("$.szavazasok[1].kepviselokSzama").value(3))
        .andExpect(jsonPath("$.szavazasok[1].szavazatok.length()").value(3));
  }
}
