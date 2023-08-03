package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.StandingDTO;
import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Standing;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.CompetitionRepository;
import co.com.perficient.project3.repository.StandingRepository;
import co.com.perficient.project3.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.Constants.uuidA;
import static co.com.perficient.project3.utils.constant.Constants.uuidB;
import static co.com.perficient.project3.utils.constant.StandingConstants.STANDING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StandingControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StandingRepository standingRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private CompetitionRepository competitionRepository;

    private final String TEAM_NAME = "teamName";
    private final String COMPETITION_NAME = "competitionName";

    private final String MATCHES_PLAYED_JSONPATH = "$.matchesPlayed";
    private final String WINS_JSONPATH = "$.wins";
    private final String DRAWS_JSONPATH = "$.draws";
    private final String LOSSES_JSONPATH = "$.losses";
    private final String POINTS_JSONPATH = "$.points";
    private final String COMPETITION_JSONPATH = "$.competition";
    private final String TEAM_JSONPATH = "$.team";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        Team teamA = Team.builder().name(TEAM_NAME + "A").build();
        Team teamB = Team.builder().name(TEAM_NAME + "B").build();
        Team teamC = Team.builder().name(TEAM_NAME + "C").build();
        teamRepository.saveAll(Arrays.asList(teamA, teamB, teamC));
        Competition competitionA = Competition.builder().name(COMPETITION_NAME + "A").build();
        Competition competitionB = Competition.builder().name(COMPETITION_NAME + "B").build();
        Competition competitionC = Competition.builder().name(COMPETITION_NAME + "C").build();
        competitionRepository.saveAll(Arrays.asList(competitionA, competitionB, competitionC));

        Standing standingA = Standing.builder().id(uuidA).competition(competitionA).team(teamA).wins(1).draws(1)
                .losses(1).build();
        Standing standingB = Standing.builder().id(uuidB).competition(competitionB).team(teamB).wins(2).draws(2)
                .losses(2).build();
        standingRepository.saveAll(Arrays.asList(standingA, standingB));
    }

    @Test
    void createStanding() throws Exception {
        final Integer WINS = 4;
        final Integer DRAWS = 5;
        final Integer LOSSES = 6;

        StandingDTO standingDTO = new StandingDTO(null, WINS, DRAWS, LOSSES, null, COMPETITION_NAME + "C", TEAM_NAME + "C", null);
        String body = new ObjectMapper().writeValueAsString(standingDTO);

        MvcResult mvcResult = mockMvc.perform(post(STANDING).content(body).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MATCHES_PLAYED_JSONPATH).value(15)).andExpect(jsonPath(WINS_JSONPATH).value(WINS))
                .andExpect(jsonPath(DRAWS_JSONPATH).value(DRAWS)).andExpect(jsonPath(LOSSES_JSONPATH).value(LOSSES))
                .andExpect(jsonPath(POINTS_JSONPATH).value(17))
                .andExpect(jsonPath(COMPETITION_JSONPATH).value(COMPETITION_NAME + "C"))
                .andExpect(jsonPath(TEAM_JSONPATH).value(TEAM_NAME + "C")).andReturn();
    }

    @Test
    void findAllStandings() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(STANDING)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2)).andReturn();
    }

    @Test
    void findStandingById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(STANDING + "/{id}", uuidB)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MATCHES_PLAYED_JSONPATH).value(6)).andExpect(jsonPath(WINS_JSONPATH).value(2))
                .andExpect(jsonPath(DRAWS_JSONPATH).value(2)).andExpect(jsonPath(LOSSES_JSONPATH).value(2))
                .andExpect(jsonPath(POINTS_JSONPATH).value(8))
                .andExpect(jsonPath(COMPETITION_JSONPATH).value(COMPETITION_NAME + "B"))
                .andExpect(jsonPath(TEAM_JSONPATH).value(TEAM_NAME + "B")).andReturn();
    }

    @Test
    void findStandingByIdNotFound() throws Exception {
        mockMvc.perform(get(STANDING + "/{id}", UUID.randomUUID())).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void updateStanding() throws Exception {
        final Integer WINS = 6;
        final Integer DRAWS = 4;
        final Integer LOSSES = 2;

        StandingDTO standingDTO = new StandingDTO(null, WINS, DRAWS, LOSSES, null, COMPETITION_NAME + "C", TEAM_NAME + "C", null);
        String body = new ObjectMapper().writeValueAsString(standingDTO);

        MvcResult mvcResult = mockMvc.perform(put(STANDING + "/{id}", uuidA).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(MATCHES_PLAYED_JSONPATH).value(12)).andExpect(jsonPath(WINS_JSONPATH).value(6))
                .andExpect(jsonPath(DRAWS_JSONPATH).value(DRAWS)).andExpect(jsonPath(LOSSES_JSONPATH).value(LOSSES))
                .andExpect(jsonPath(POINTS_JSONPATH).value(22))
                .andExpect(jsonPath(COMPETITION_JSONPATH).value(COMPETITION_NAME + "C"))
                .andExpect(jsonPath(TEAM_JSONPATH).value(TEAM_NAME + "C")).andReturn();
    }

    @Test
    void updateStandingNotFound() throws Exception {
        StandingDTO standingDTO = new StandingDTO(null, 3, 3, 3, null, null, null, null);
        String body = new ObjectMapper().writeValueAsString(standingDTO);

        mockMvc.perform(put(STANDING + "/{id}", UUID.randomUUID()).content(body)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }


    @Test
    void deleteStanding() throws Exception {
        mockMvc.perform(delete(STANDING + "/{id}", uuidA)).andDo(print()).andExpect(status().isOk()).andReturn();
    }
}
