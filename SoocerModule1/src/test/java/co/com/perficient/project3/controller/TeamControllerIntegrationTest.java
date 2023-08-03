package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.TeamDTO;
import co.com.perficient.project3.model.entity.Coach;
import co.com.perficient.project3.model.entity.President;
import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.CoachRepository;
import co.com.perficient.project3.repository.PresidentRepository;
import co.com.perficient.project3.repository.StadiumRepository;
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

import static co.com.perficient.project3.utils.constant.Constants.COUNTRY;
import static co.com.perficient.project3.utils.constant.Constants.COUNTRY_JSONPATH;
import static co.com.perficient.project3.utils.constant.Constants.NAME_JSONPATH;
import static co.com.perficient.project3.utils.constant.Constants.uuidA;
import static co.com.perficient.project3.utils.constant.Constants.uuidB;
import static co.com.perficient.project3.utils.constant.TeamConstants.TEAM;
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
class TeamControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private StadiumRepository stadiumRepository;
    @Autowired
    private PresidentRepository presidentRepository;
    @Autowired
    private CoachRepository coachRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        Stadium stadiumA = Stadium.builder().name("Stadium A").country("Country A").build();
        Stadium stadiumB = Stadium.builder().name("Stadium B").country("Country B").build();
        stadiumRepository.saveAll(Arrays.asList(stadiumA, stadiumB));

        Team teamA = Team.builder().id(uuidA).name("Team A").country("Country A").stadium(stadiumA).build();
        Team teamB = Team.builder().id(uuidB).name("Team B").country("Country B").stadium(stadiumB).build();
        teamRepository.saveAll(Arrays.asList(teamA, teamB));

        President presidentA = President.builder().name("President A").team(teamA).build();
        President presidentB = President.builder().name("President B").team(teamB).build();
        presidentRepository.saveAll(Arrays.asList(presidentA, presidentB));

        coachRepository.save(Coach.builder().name("Coach B").team(teamB).build());
    }

    @Test
    void createTeam() throws Exception {
        final String STADIUM_NAME = "Stadium C";
        final String COUNTRY = "Country C";
        stadiumRepository.save(Stadium.builder().name(STADIUM_NAME).country(COUNTRY).build());
        final String NAME = "Team C";

        TeamDTO teamDTO = new TeamDTO(NAME, null, STADIUM_NAME, "", "", null);
        String body = new ObjectMapper().writeValueAsString(teamDTO);

        MvcResult mvcResult = mockMvc.perform(post(TEAM).content(body).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME)).andExpect(jsonPath(COUNTRY_JSONPATH).value(COUNTRY))
                .andExpect(jsonPath("$.stadium").value(STADIUM_NAME)).andReturn();
    }

    @Test
    void findAllTeams() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(TEAM)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2)).andReturn();
    }

    @Test
    void findTeamById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(TEAM + "/{id}", uuidB)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value("Team B")).andExpect(jsonPath("$.links.size()").value(5))
                .andReturn();
    }

    @Test
    void findTeamByIdNotFound() throws Exception {
        mockMvc.perform(get(TEAM + "/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    void updateTeam() throws Exception {
        final String STADIUM_NAME = "Stadium D";
        final String COUNTRY = "Country D";
        stadiumRepository.save(Stadium.builder().name(STADIUM_NAME).country(COUNTRY).build());

        final String NAME = "Team D";
        TeamDTO teamDTO = new TeamDTO(NAME, null, STADIUM_NAME, "", "", null);
        String body = new ObjectMapper().writeValueAsString(teamDTO);

        MvcResult mvcResult = mockMvc.perform(put(TEAM + "/{id}", uuidA).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME))
                .andExpect(jsonPath(COUNTRY_JSONPATH).value(COUNTRY))
                .andExpect(jsonPath("$.stadium").value(STADIUM_NAME))
                .andReturn();
    }

    @Test
    void updateTeamNotFound() throws Exception {
        TeamDTO teamDTO = new TeamDTO("", null, "", "", "", null);
        String body = new ObjectMapper().writeValueAsString(teamDTO);

        mockMvc.perform(put(TEAM + "/{id}", UUID.randomUUID()).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTeam() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(TEAM + "/{id}", uuidA)).andDo(print()).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void findTeamsByCountry() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(TEAM + COUNTRY + "/Country A")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.size()").value(1)).andReturn();
    }
}
