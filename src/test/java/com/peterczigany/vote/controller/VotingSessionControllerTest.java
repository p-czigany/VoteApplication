package com.peterczigany.vote.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peterczigany.vote.TestUtils;
import com.peterczigany.vote.exception.TimeDuplicationException;
import com.peterczigany.vote.exception.VoteNotFoundException;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.response.CreationResponse;
import com.peterczigany.vote.response.VoteResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VotingSessionController.class)
class VotingSessionControllerTest {

  @MockBean private VotingSessionController controller;

  @Autowired private MockMvc mockMvc;

  @ParameterizedTest
  @CsvFileSource(resources = "/validVotingSession.csv")
  void testAddNewVotingSessionSuccessfully(String votingSessionJson) throws Exception {
    VotingSessionDTO dto = TestUtils.validVotingSessionDTO();
    Mockito.when(controller.createVotingSession(dto)).thenReturn(new CreationResponse("ABC123"));

    mockMvc
        .perform(
            post("/szavazasok/szavazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(votingSessionJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("szavazasId").value("ABC123"));
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/validVotingSession.csv")
  void testUnsuccessfulVotingSessionAdditionBecauseTimeIsDuplicated(String votingSessionJson)
      throws Exception {
    VotingSessionDTO dto = TestUtils.validVotingSessionDTO();
    Mockito.when(controller.createVotingSession(dto)).thenThrow(TimeDuplicationException.class);

    mockMvc
        .perform(
            post("/szavazasok/szavazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(votingSessionJson))
        .andExpect(status().isConflict());
    // TODO: display error message in response body
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/validVotingSession.csv")
  void testUnsuccessfulVotingSessionAdditionBecauseRequestIsNotValid(String votingSessionJson)
      throws Exception {
    VotingSessionDTO dto = TestUtils.validVotingSessionDTO();
    Mockito.when(controller.createVotingSession(dto)).thenThrow(IllegalArgumentException.class);

    mockMvc
        .perform(
            post("/szavazasok/szavazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(votingSessionJson))
        .andExpect(status().isBadRequest());
    // TODO: display error message in response body
  }

  @Test
  void testGetVoteBySessionIdAndRepresentative() throws Exception {
    Mockito.when(controller.getIndividualVote("ABC123", "Kepviselo1"))
        .thenReturn(new VoteResponse("i"));

    mockMvc
        .perform(
            get("http://localhost:8080/szavazasok/szavazas")
                .param("szavazas", "ABC123")
                .param("kepviselo", "Kepviselo1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("szavazat").value("i"));
  }

  @Test
  void testFailToGetVoteBySessionIdAndRepresentative() throws Exception {
    Mockito.when(controller.getIndividualVote("ABC123", "Kepviselo1"))
        .thenThrow(VoteNotFoundException.class);

    mockMvc
        .perform(
            get("http://localhost:8080/szavazasok/szavazas")
                .param("szavazas", "ABC123")
                .param("kepviselo", "Kepviselo1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testFailToGetVotingSessionResult() throws Exception {
    Mockito.when(controller.getVotingSessionResult("ABC123"))
        .thenThrow(VotingSessionNotFoundException.class);

    mockMvc
        .perform(get("http://localhost:8080/szavazasok/eredmeny").param("szavazas", "ABC123"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testSuccessfulGetVotingSessionResult() throws Exception {

    Mockito.when(controller.getVotingSessionResult("ABC123"))
        .thenReturn(new VotingSessionResultResponse(ResultValue.ACCEPTED, 150, 120, 30, 0));

    mockMvc
        .perform(get("http://localhost:8080/szavazasok/eredmeny").param("szavazas", "ABC123"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("eredmeny").value("F"))
        .andExpect(jsonPath("kepviselokSzama").value(150))
        .andExpect(jsonPath("igenekSzama").value(120))
        .andExpect(jsonPath("nemekSzama").value(30))
        .andExpect(jsonPath("tartozkodasokSzama").value(0));
  }
}