package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.PlayerDTO;
import co.com.perficient.project3.model.entity.Player;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.PlayerRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.Constants.BIRTHDATE_JSONPATH;
import static co.com.perficient.project3.utils.constant.Constants.NAME_JSONPATH;
import static co.com.perficient.project3.utils.constant.Constants.NATIONALITY_JSONPATH;
import static co.com.perficient.project3.utils.constant.Constants.uuidA;
import static co.com.perficient.project3.utils.constant.Constants.uuidB;
import static co.com.perficient.project3.utils.constant.PlayerConstants.PLAYER;
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
class PlayerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        Team teamB = Team.builder().name("Team B").build();
        teamRepository.save(teamB);

        Player playerA = Player.builder().id(uuidA).birthDate(LocalDate.now().minusYears(1)).name("Player A")
                .nationality("Nationality A").number("1").position("Position A").build();
        Player playerB = Player.builder().id(uuidB).birthDate(LocalDate.now().minusYears(2)).name("Player B")
                .nationality("Nationality B").number("2").position("Position B").team(teamB).build();
        playerRepository.saveAll(Arrays.asList(playerA, playerB));
    }

    @Test
    void createPlayer() throws Exception {
        final String NAME = "Coach C";
        final String NATIONALITY = "Nationality C";
        final String NUMBER = "10";
        final String POSITION = "Striker";

        PlayerDTO playerDTO = new PlayerDTO(NAME, NATIONALITY, LocalDate.now().minusYears(5)
                .toString(), NUMBER, POSITION, null, null);
        String body = new ObjectMapper().writeValueAsString(playerDTO);

        MvcResult mvcResult = mockMvc.perform(post(PLAYER).content(body).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME))
                .andExpect(jsonPath(NATIONALITY_JSONPATH).value(NATIONALITY))
                .andExpect(jsonPath("$.number").value(NUMBER)).andExpect(jsonPath("$.position").value(POSITION))
                .andExpect(jsonPath(BIRTHDATE_JSONPATH).isNotEmpty()).andReturn();
    }

    @Test
    void findAllPlayers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(PLAYER)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2)).andReturn();
    }

    @Test
    void findAllPlayersByTeam() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(PLAYER + "?team={t}", "Team B")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$..name").value("Player B")).andExpect(jsonPath("$..number").value("2"))
                .andExpect(jsonPath("$..position").value("Position B")).andReturn();
    }

    @Test
    void findPlayerById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(PLAYER + "/{id}", uuidB)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value("Player B"))
                .andExpect(jsonPath(NATIONALITY_JSONPATH).value("Nationality B"))
                .andExpect(jsonPath(BIRTHDATE_JSONPATH).isNotEmpty()).andExpect(jsonPath("$.number").value("2"))
                .andExpect(jsonPath("$.position").value("Position B")).andReturn();
    }

    @Test
    void findPlayerByIdNotFound() throws Exception {
        mockMvc.perform(get(PLAYER + "/{id}", UUID.randomUUID())).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void updatePlayer() throws Exception {
        final String TEAM_NAME = "teamName";
        Team team = Team.builder().name(TEAM_NAME).build();
        teamRepository.save(team);

        final String NAME = "Coach D";
        final String NATIONALITY = "Nationality D";
        final String NUMBER = "1";
        final String POSITION = "Goalkeeper";

        PlayerDTO playerDTO = new PlayerDTO(NAME, NATIONALITY, LocalDate.now().minusYears(4)
                .toString(), NUMBER, POSITION, TEAM_NAME, null);
        String body = new ObjectMapper().writeValueAsString(playerDTO);

        MvcResult mvcResult = mockMvc.perform(put(PLAYER + "/{id}", uuidA).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME))
                .andExpect(jsonPath(NATIONALITY_JSONPATH).value(NATIONALITY))
                .andExpect(jsonPath(BIRTHDATE_JSONPATH).isNotEmpty()).andExpect(jsonPath("$.number").value(NUMBER))
                .andExpect(jsonPath("$.position").value(POSITION)).andReturn();
    }

    @Test
    void updatePlayerNotFound() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO("", "", LocalDate.now().minusYears(4)
                .toString(), "", "", "", null);
        String body = new ObjectMapper().writeValueAsString(playerDTO);

        mockMvc.perform(put(PLAYER + "/{id}", UUID.randomUUID()).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePlayerById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(PLAYER + "/{id}", uuidA)).andDo(print()).andExpect(status().isOk())
                .andReturn();
    }
}
