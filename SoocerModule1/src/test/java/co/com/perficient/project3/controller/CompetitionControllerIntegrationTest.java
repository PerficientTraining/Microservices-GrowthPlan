package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.CompetitionDTO;
import co.com.perficient.project3.model.entity.Competition;
import co.com.perficient.project3.repository.CompetitionRepository;
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

import static co.com.perficient.project3.utils.constant.CompetitionConstants.COMPETITION;
import static co.com.perficient.project3.utils.constant.Constants.uuidA;
import static co.com.perficient.project3.utils.constant.Constants.uuidB;
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
class CompetitionControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompetitionRepository competitionRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        Competition competitionA = Competition.builder().id(uuidA).name("Competition A").build();
        Competition competitionB = Competition.builder().id(uuidB).name("Competition B").build();
        competitionRepository.saveAll(Arrays.asList(competitionA, competitionB));
    }

    @Test
    void createCompetition() throws Exception {
        final String COMPETITION_NAME = "Competition C";
        CompetitionDTO competitionDTO = new CompetitionDTO(COMPETITION_NAME);
        String body = new ObjectMapper().writeValueAsString(competitionDTO);

        MvcResult mvcResult = mockMvc.perform(post(COMPETITION).content(body).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(COMPETITION_NAME)).andReturn();
    }

    @Test
    void findAllCompetitions() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(COMPETITION)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2)).andReturn();
    }

    @Test
    void findCompetitionById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(COMPETITION + "/{id}", uuidB)).andDo(print())
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Competition B")).andReturn();
    }

    @Test
    void findCompetitionByIdNotFound() throws Exception {
        mockMvc.perform(get(COMPETITION + "/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    void updateCompetition() throws Exception {
        final String COMPETITION_NAME = "Competition D";
        CompetitionDTO competitionDTO = new CompetitionDTO(COMPETITION_NAME);
        String body = new ObjectMapper().writeValueAsString(competitionDTO);

        MvcResult mvcResult = mockMvc.perform(put(COMPETITION + "/{id}", uuidA).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(COMPETITION_NAME)).andReturn();
    }

    @Test
    void updateCompetitionNotFound() throws Exception {
        CompetitionDTO competitionDTO = new CompetitionDTO("");
        String body = new ObjectMapper().writeValueAsString(competitionDTO);

        mockMvc.perform(put(COMPETITION + "/{id}", UUID.randomUUID()).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void deleteCompetition() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(COMPETITION + "/{id}", uuidA)).andDo(print())
                .andExpect(status().isOk()).andReturn();
    }
}