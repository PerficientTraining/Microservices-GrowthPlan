package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.MatchDTO;
import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.model.entity.Match;
import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.model.entity.Team;
import co.com.perficient.project3.repository.CompetitionRepository;
import co.com.perficient.project3.repository.MatchRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.Constants.LAST;
import static co.com.perficient.project3.utils.constant.Constants.uuidA;
import static co.com.perficient.project3.utils.constant.Constants.uuidB;
import static co.com.perficient.project3.utils.constant.MatchConstants.MATCH;
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
class MatchControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private StadiumRepository stadiumRepository;
    @Autowired
    private CompetitionRepository competitionRepository;

    private final String STADIUM_NAME = "stadiumName";
    private final String HOME_TEAM_NAME = "homeTeam";
    private final String AWAY_TEAM_NAME = "awayTeam";
    private final String COMPETITION_NAME = "competitionName";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        Stadium stadiumA = Stadium.builder().name(STADIUM_NAME + "A").build();
        Stadium stadiumB = Stadium.builder().name(STADIUM_NAME + "B").build();
        Stadium stadiumC = Stadium.builder().name(STADIUM_NAME + "C").build();
        stadiumRepository.saveAll(Arrays.asList(stadiumA, stadiumB, stadiumC));

        Team homeTeam = Team.builder().name(HOME_TEAM_NAME).stadium(stadiumA).build();
        Team awayTeam = Team.builder().name(AWAY_TEAM_NAME).stadium(stadiumB).build();
        teamRepository.saveAllAndFlush(Arrays.asList(homeTeam, awayTeam));

        Competition competitionA = Competition.builder().name(COMPETITION_NAME + "A").build();
        Competition competitionB = Competition.builder().name(COMPETITION_NAME + "B").build();
        competitionRepository.saveAll(Arrays.asList(competitionA, competitionB));

        Match matchA = Match.builder().id(uuidA).date(LocalDate.now().minusYears(1)).round("Last 8").score("2-0")
                .homeTeam(homeTeam).awayTeam(awayTeam).build();
        Match matchB = Match.builder().id(uuidB).date(LocalDate.now().minusYears(2)).stadium(stadiumB).round("Final")
                .score("0-0").homeTeam(homeTeam).awayTeam(awayTeam).build();
        matchRepository.saveAll(Arrays.asList(matchA, matchB));
    }

    @Test
    void createMatch() throws Exception {
        final String ROUND = "Last 16";
        final String SCORE = "2-0";

        MatchDTO matchDTO = new MatchDTO(LocalDate.now().minusMonths(1)
                .toString(), null, ROUND, SCORE, HOME_TEAM_NAME, AWAY_TEAM_NAME);
        String body = new ObjectMapper().writeValueAsString(matchDTO);

        MvcResult mvcResult = mockMvc.perform(post(MATCH).queryParam("competitionName", COMPETITION_NAME + "A")
                        .content(body).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.round").value(ROUND)).andExpect(jsonPath("$.score").value(SCORE)).andReturn();
    }

    @Test
    void findAllMatches() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(MATCH)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2)).andReturn();
    }

    @Test
    void findMatchById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(MATCH + "/{id}", uuidB)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.round").value("Final")).andExpect(jsonPath("$.score").value("0-0")).andReturn();
    }

    @Test
    void updateMatch() throws Exception {
        final String ROUND = "Semifinal";
        final String SCORE = "2-1";

        MatchDTO matchDTO = new MatchDTO(LocalDate.now().minusMonths(1)
                .toString(), STADIUM_NAME + "C", ROUND, SCORE, HOME_TEAM_NAME, AWAY_TEAM_NAME);
        String body = new ObjectMapper().writeValueAsString(matchDTO);

        MvcResult mvcResult = mockMvc.perform(put(MATCH + "/{id}", uuidA).queryParam("competitionName", COMPETITION_NAME + "A")
                        .content(body).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.round").value(ROUND)).andExpect(jsonPath("$.score").value(SCORE)).andReturn();
    }

    @Test
    void updateMatchNotFound() throws Exception {
        MatchDTO matchDTO = new MatchDTO(LocalDate.now().minusMonths(1).toString(), "", "", "", "", "");
        String body = new ObjectMapper().writeValueAsString(matchDTO);

        mockMvc.perform(put(MATCH + "/{id}", UUID.randomUUID()).queryParam("competitionName", COMPETITION_NAME + "B")
                .content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void deleteMatch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(MATCH + "/{id}", uuidA)).andDo(print()).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void findLast3Matches() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(MATCH + LAST)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2)).andReturn();
    }
}
