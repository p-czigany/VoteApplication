package com.peterczigany.vote;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peterczigany.vote.controller.VotingSessionController;
import com.peterczigany.vote.exception.TimeDuplicationException;
import com.peterczigany.vote.model.VotingSessionDTO;
import com.peterczigany.vote.response.CreationResponse;
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
}
