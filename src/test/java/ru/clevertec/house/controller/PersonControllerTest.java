package ru.clevertec.house.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.service.impl.PersonServiceImpl;
import util.PersonTestData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.initdata.TestDataForHouse.HOUSE_UUID;
import static util.initdata.TestDataForHouse.INCORRECT_UUID;
import static util.initdata.TestDataForPerson.PERSON_UUID;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_NAME;
import static util.initdata.TestDataForPerson.UPDATE_PERSON_SURNAME;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonServiceImpl personService;

    private static final String URL = "/persons";

    @Nested
    class FindById {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID personUuid = PERSON_UUID;

            PersonResponse response = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            when(personService.findById(personUuid))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(get(URL + "/" + personUuid))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid").value(personUuid.toString()));
        }
    }

    @Nested
    class FindAll {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 2);

            PersonResponse person = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponse> personList = List.of(person);

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findAll(pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(2);
            assertThat(jsonObject.get("content")).isNotNull();
        }

        @SneakyThrows
        @Test
        void shouldReturnEmptyJsonAndStatus200() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 2);

            List<PersonResponse> personList = Collections.emptyList();

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findAll(pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindPersonsWhichLiveInHouse {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID houseUuid = HOUSE_UUID;

            PageRequest pageRequest = PageRequest.of(0, 2);
            PersonResponse person = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponse> personList = List.of(person);

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsWhichLiveInHouse(houseUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/lives/" + houseUuid + "?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(2);
            assertThat(jsonObject.get("content")).isNotNull();
        }

        @SneakyThrows
        @Test
        void shouldReturnEmptyJsonAndStatus200() {
            // given
            UUID houseUuid = INCORRECT_UUID;

            PageRequest pageRequest = PageRequest.of(0, 2);

            List<PersonResponse> personList = Collections.emptyList();

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsWhichLiveInHouse(houseUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "/lives/" + houseUuid + "?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindPersonsWhichSomeTimeLiveInHouse {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID houseUuid = HOUSE_UUID;

            PageRequest pageRequest = PageRequest.of(0, 3);

            PersonResponse person = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponse> personList = List.of(person);

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsWhichSomeTimeLiveInHouse(houseUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/sometimelives/" + houseUuid + "?page=0&size=3"))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(3);
            assertThat(jsonObject.get("content")).isNotNull();
        }

        @SneakyThrows
        @Test
        void shouldReturnEmptyJsonAndStatus200() {
            // given
            UUID houseUuid = INCORRECT_UUID;

            PageRequest pageRequest = PageRequest.of(0, 3);

            List<PersonResponse> personList = Collections.emptyList();

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsWhichSomeTimeLiveInHouse(houseUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "/sometimelives/" + houseUuid + "?page=0&size=3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindPersonsWhichSomeTimeOwnHouse {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID houseUuid = HOUSE_UUID;

            PageRequest pageRequest = PageRequest.of(0, 4);

            PersonResponse person = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponse> personList = List.of(person);

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsWhichSomeTimeOwnHouse(houseUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/sometimeownes/" + houseUuid + "?page=0&size=4"))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(4);
            assertThat(jsonObject.get("content")).isNotNull();
        }

        @SneakyThrows
        @Test
        void shouldReturnEmptyJsonAndStatus200() {
            // given
            UUID houseUuid = INCORRECT_UUID;

            PageRequest pageRequest = PageRequest.of(0, 4);

            List<PersonResponse> personList = Collections.emptyList();

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsWhichSomeTimeOwnHouse(houseUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "/sometimeownes/" + houseUuid + "?page=0&size=4"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindPersonsFullTextSearch {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            String searchTerm = "ро";

            PageRequest pageRequest = PageRequest.of(0, 3);

            PersonResponse person = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponse> personList = List.of(person);

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsFullTextSearch(searchTerm, pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/fullsearch/" + searchTerm + "?page=0&size=3"))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(3);
            assertThat(jsonObject.get("content")).isNotNull();
        }

        @SneakyThrows
        @Test
        void shouldReturnEmptyJsonAndStatus200() {
            // given
            String searchTerm = "ра";

            PageRequest pageRequest = PageRequest.of(0, 3);

            List<PersonResponse> personList = Collections.emptyList();

            Page<PersonResponse> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.findPersonsFullTextSearch(searchTerm, pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "/fullsearch/" + searchTerm + "?page=0&size=3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class Save {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            PersonRequest personRequest = PersonTestData.builder()
                    .build().
                    getRequestDto();
            PersonResponse personResponse = PersonTestData.builder()
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(personResponse);

            when(personService.save(personRequest))
                    .thenReturn(personResponse);

            // when, then
            mockMvc.perform(post(URL)
                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpectAll(jsonPath("$.name").value(personResponse.getName()),
                            jsonPath("$.surname").value(personResponse.getSurname()),
                            jsonPath("$.sex").value(personResponse.getSex().toString()),
                            jsonPath("$.passport.series").value(personResponse.getPassport().getSeries()),
                            jsonPath("$.passport.number").value(personResponse.getPassport().getNumber()));
        }
    }

    @Nested
    class Update {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID personUuid = PERSON_UUID;

            PersonRequest personRequest = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build().
                    getRequestDto();
            PersonResponse personResponse = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(personResponse);

            when(personService.update(personUuid, personRequest))
                    .thenReturn(personResponse);

            // when, then
            mockMvc.perform(put(URL + "/" + personUuid)
                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.name").value(personResponse.getName()),
                            jsonPath("$.surname").value(personResponse.getSurname()),
                            jsonPath("$.sex").value(personResponse.getSex().toString()),
                            jsonPath("$.passport.series").value(personResponse.getPassport().getSeries()),
                            jsonPath("$.passport.number").value(personResponse.getPassport().getNumber()));
        }
    }

    @Nested
    class PatchUpdate {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID personUuid = PERSON_UUID;

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("name", UPDATE_PERSON_NAME);
            fieldsToUpdate.put("surname", UPDATE_PERSON_SURNAME);

            PersonResponse personResponse = PersonTestData.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(personResponse);

            when(personService.patchUpdate(personUuid, fieldsToUpdate))
                    .thenReturn(personResponse);

            // when, then
            mockMvc.perform(patch(URL + "/" + personUuid)
                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class Delete {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID personUuid = PERSON_UUID;

            doNothing().when(personService)
                    .delete(personUuid);

            // when, then
            mockMvc.perform(delete(URL + "/" + personUuid))
                    .andExpect(status().isNoContent());
        }
    }
}