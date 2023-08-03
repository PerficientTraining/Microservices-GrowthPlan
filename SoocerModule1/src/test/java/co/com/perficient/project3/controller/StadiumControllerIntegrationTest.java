package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.StadiumDTO;
import co.com.perficient.project3.model.entity.Stadium;
import co.com.perficient.project3.repository.StadiumRepository;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static co.com.perficient.project3.utils.constant.Constants.COUNTRY;
import static co.com.perficient.project3.utils.constant.Constants.COUNTRY_JSONPATH;
import static co.com.perficient.project3.utils.constant.Constants.NAME_JSONPATH;
import static co.com.perficient.project3.utils.constant.Constants.uuidA;
import static co.com.perficient.project3.utils.constant.Constants.uuidB;
import static co.com.perficient.project3.utils.constant.StadiumConstants.STADIUM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StadiumControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StadiumRepository stadiumRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        Stadium stadiumA = Stadium.builder().id(uuidA).name("Stadium A").country("Country A").city("City A")
                .capacity("1").build();
        Stadium stadiumB = Stadium.builder().id(uuidB).name("Stadium B").country("Country B").city("City B")
                .capacity("2").build();
        stadiumRepository.saveAll(Arrays.asList(stadiumA, stadiumB));
    }

    @Test
    void createStadium() throws Exception {
        final String NAME = "Stadium C";
        final String COUNTRY = "Country C";
        final String CITY = "City C";
        final String CAPACITY = "3";

        StadiumDTO stadiumDTO = new StadiumDTO(NAME, COUNTRY, CITY, CAPACITY, null);
        String body = new ObjectMapper().writeValueAsString(stadiumDTO);

        MvcResult mvcResult = mockMvc.perform(post(STADIUM).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME)).andExpect(jsonPath(COUNTRY_JSONPATH).value(COUNTRY))
                .andExpect(jsonPath("$.city").value(CITY)).andExpect(jsonPath("$.capacity").value(CAPACITY))
                .andReturn();
    }

    @Test
    void findAllStadiums() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(STADIUM)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2)).andReturn();
    }

    @Test
    void findStadiumById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(STADIUM + "/{id}", uuidB)).andDo(print())
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city").value("City B")).andReturn();
    }

    @Test
    void findStadiumByIdNotFound() throws Exception {
        mockMvc.perform(get(STADIUM + "/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    void updateStadium() throws Exception {
        final String NAME = "Stadium D";
        final String COUNTRY = "Country D";
        final String CITY = "City D";
        final String CAPACITY = "4";

        StadiumDTO stadiumDTO = new StadiumDTO(NAME, COUNTRY, CITY, CAPACITY, null);
        String body = new ObjectMapper().writeValueAsString(stadiumDTO);

        MvcResult mvcResult = mockMvc.perform(put(STADIUM + "/{id}", uuidA).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME)).andExpect(jsonPath(COUNTRY_JSONPATH).value(COUNTRY))
                .andExpect(jsonPath("$.city").value(CITY)).andExpect(jsonPath("$.capacity").value(CAPACITY))
                .andReturn();
    }

    @Test
    void updateStadiumNotFound() throws Exception {
        StadiumDTO stadiumDTO = new StadiumDTO("", COUNTRY, "", "", null);
        String body = new ObjectMapper().writeValueAsString(stadiumDTO);

        mockMvc.perform(put(STADIUM + "/{id}", UUID.randomUUID()).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStadium() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(STADIUM + "/{id}", uuidA)).andDo(print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void patchStadium() throws Exception {
        final String NAME = "Stadium E";
        final String CAPACITY = "5";

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", NAME);
        fields.put("capacity", CAPACITY);
        String body = new ObjectMapper().writeValueAsString(fields);

        MvcResult mvcResult = mockMvc.perform(patch(STADIUM + "/{id}", uuidB).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME)).andExpect(jsonPath(COUNTRY_JSONPATH).value("Country B"))
                .andExpect(jsonPath("$.city").value("City B")).andExpect(jsonPath("$.capacity").value(CAPACITY))
                .andReturn();
    }

    @Test
    void patchStadiumNotFound() throws Exception {
        String body = new ObjectMapper().writeValueAsString(new HashMap<>());

        mockMvc.perform(patch(STADIUM + "/{id}", UUID.randomUUID()).content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void findStadiumsByCountry() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(STADIUM + COUNTRY + "/Country A")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.size()").value(1)).andReturn();
    }
}
