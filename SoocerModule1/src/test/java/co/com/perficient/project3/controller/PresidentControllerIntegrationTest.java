package co.com.perficient.project3.controller;

import co.com.perficient.project3.model.dto.PresidentDTO;
import co.com.perficient.project3.model.entity.President;
import co.com.perficient.project3.repository.PresidentRepository;
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
import static co.com.perficient.project3.utils.constant.PresidentConstants.PRESIDENT;
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
class PresidentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PresidentRepository presidentRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        President presidentA = President.builder().id(uuidA).name("President A").nationality("Nationality A")
                .birthDate(LocalDate.now().minusYears(1)).build();
        President presidentB = President.builder().id(uuidB).name("President B").nationality("Nationality B")
                .birthDate(LocalDate.now().minusYears(1)).build();
        presidentRepository.saveAll(Arrays.asList(presidentA, presidentB));
    }

    @Test
    void createPresident() throws Exception {
        final String NAME = "President C";
        final String NATIONALITY = "Nationality C";

        PresidentDTO presidentDTO = new PresidentDTO(NAME, NATIONALITY, LocalDate.now().minusYears(1)
                .toString(), null, null);
        String body = new ObjectMapper().writeValueAsString(presidentDTO);

        MvcResult mvcResult = mockMvc.perform(post(PRESIDENT).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME))
                .andExpect(jsonPath(NATIONALITY_JSONPATH).value(NATIONALITY))
                .andExpect(jsonPath(BIRTHDATE_JSONPATH).isNotEmpty())
                .andReturn();
    }

    @Test
    void findAllPresidents() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(PRESIDENT)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2)).andReturn();
    }

    @Test
    void findPresidentById() throws Exception {
        mockMvc.perform(get(PRESIDENT + "/{id}", uuidB)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value("President B"))
                .andExpect(jsonPath(NATIONALITY_JSONPATH).value("Nationality B"))
                .andExpect(jsonPath(BIRTHDATE_JSONPATH).isNotEmpty()).andReturn();
    }

    @Test
    void findPresidentByIdNotFound() throws Exception {
        mockMvc.perform(get(PRESIDENT + "/{id}", UUID.randomUUID())).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void updatePresident() throws Exception {
        final String NAME = "President D";
        final String NATIONALITY = "Nationality D";

        PresidentDTO presidentDTO = new PresidentDTO(NAME, NATIONALITY, LocalDate.now().minusYears(1)
                .toString(), null, null);
        String body = new ObjectMapper().writeValueAsString(presidentDTO);

        MvcResult mvcResult = mockMvc.perform(put(PRESIDENT + "/{id}", uuidA).content(body)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(NAME_JSONPATH).value(NAME))
                .andExpect(jsonPath(NATIONALITY_JSONPATH).value(NATIONALITY)).andReturn();
    }

    @Test
    void updatePresidentNotFound() throws Exception {
        PresidentDTO presidentDTO = new PresidentDTO("", "", LocalDate.now().minusYears(1).toString(), "", null);
        String body = new ObjectMapper().writeValueAsString(presidentDTO);

        mockMvc.perform(put(PRESIDENT + "/{id}", UUID.randomUUID()).content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void deletePresident() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(PRESIDENT + "/{id}", uuidA)).andDo(print())
                .andExpect(status().isOk()).andReturn();
    }
}
