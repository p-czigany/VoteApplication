package com.peterczigany.vote;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peterczigany.vote.model.VotingSession;
import com.peterczigany.vote.repository.VotingSessionRepository;
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
    System.out.println("ID: " + votingSession.getId());
    votingSession
        .getVotes()
        .forEach(v -> System.out.println(v.getRepresentative() + "\t" + v.getVoteValue().label));

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
}
