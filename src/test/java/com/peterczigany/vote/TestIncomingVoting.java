package com.peterczigany.vote;

//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.http.MediaType;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = VoteApplication.class)
//@AutoConfigureMockMvc
//class TestIncomingVoting {
//
//  @Autowired private MockMvc mockMvc;
//
//  private static final String validVotingExampleJson =
//      """
//    {
//        "idopont": "2023-09-28T11:06:25Z",
//        "targy": "Szavazás tárgya",
//        "tipus": "j",
//        "elnok": "Kepviselo1",
//        "szavazatok": [
//            {
//                "kepviselo": "Kepviselo1",
//                "szavazat": "i"
//            },
//            {
//                "kepviselo": "Kepviselo2",
//                "szavazat": "n"
//            },
//            {
//                "kepviselo": "Kepviselo3",
//                "szavazat": "t"
//            }
//        ]
//    }
//      """;
//
//  @Test
//  void testPostValidVotingSuccessfully() {
//    mockMvc.perform(MockMvcRequestBuilders.post("/api/yourEndpoint")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(validVotingExampleJson))
//            .andExpect(status().isOk())
//            .andExpect(content().string("Success"));
//  }
//}
