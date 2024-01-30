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
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import util.HouseTestData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import static util.initdata.TestDataForHouse.UPDATE_HOUSE_AREA;
import static util.initdata.TestDataForHouse.UPDATE_HOUSE_CITY;
import static util.initdata.TestDataForPerson.PERSON_UUID;

@WebMvcTest(HouseController.class)
class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HouseServiceImpl houseService;

    private static final String URL = "/houses";

    @Nested
    class FindById {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID houseUuid = HOUSE_UUID;

            HouseResponse response = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            when(houseService.findById(houseUuid))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(get(URL + "/" + houseUuid))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid").value(houseUuid.toString()));
        }

        @SneakyThrows
        @Test
        void shouldReturnExceptionThrownAndStatus404() {
            // given
            UUID houseUuid = INCORRECT_UUID;

            when(houseService.findById(any()))
                    .thenThrow(new NotFoundException("House not found!"));

            // when, then
            mockMvc.perform(get(URL + "/" + houseUuid))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
        }
    }

    @Nested
    class FindAll {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 2);

            HouseResponse house = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            List<HouseResponse> houseList = List.of(house);

            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findAll(pageRequest))
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

            List<HouseResponse> houseList = Collections.emptyList();

            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findAll(pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindHousesWhichSomeTimeLivesPerson {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID personUuid = PERSON_UUID;

            PageRequest pageRequest = PageRequest.of(0, 3);

            HouseResponse house = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            List<HouseResponse> houseList = List.of(house);
            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesWhichSomeTimeLivesPerson(personUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/sometimelives/" + personUuid + "?page=0&size=3"))
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
            UUID personUuid = PERSON_UUID;

            PageRequest pageRequest = PageRequest.of(0, 3);

            List<HouseResponse> houseList = Collections.emptyList();

            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesWhichSomeTimeLivesPerson(personUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "/sometimelives/" + personUuid + "?page=0&size=3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindHousesWhichOwnPerson {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID personUuid = PERSON_UUID;

            PageRequest pageRequest = PageRequest.of(0, 4);

            HouseResponse house = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            List<HouseResponse> houseList = List.of(house);
            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesWhichOwnPerson(personUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/owns/" + personUuid + "?page=0&size=4"))
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
            UUID personUuid = PERSON_UUID;

            PageRequest pageRequest = PageRequest.of(0, 4);

            List<HouseResponse> houseList = Collections.emptyList();

            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesWhichOwnPerson(personUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "/owns/" + personUuid + "?page=0&size=4"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindHousesWhichSomeTimeOwnPerson {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID personUuid = PERSON_UUID;

            PageRequest pageRequest = PageRequest.of(0, 3);

            HouseResponse house = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            List<HouseResponse> houseList = List.of(house);

            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesWhichSomeTimeOwnPerson(personUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/sometimeowns/" + personUuid + "?page=0&size=3"))
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
            UUID personUuid = PERSON_UUID;

            PageRequest pageRequest = PageRequest.of(0, 3);

            List<HouseResponse> houseList = Collections.emptyList();
            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesWhichSomeTimeOwnPerson(personUuid, pageRequest))
                    .thenReturn(page);

            // when, then
            mockMvc.perform(get(URL + "/sometimeowns/" + personUuid + "?page=0&size=3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class FindHousesFullTextSearch {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            String searchTerm = "лог";

            PageRequest pageRequest = PageRequest.of(0, 3);

            HouseResponse house = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            List<HouseResponse> houseList = List.of(house);

            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesFullTextSearch(searchTerm, pageRequest))
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
            String searchTerm = "лог";

            PageRequest pageRequest = PageRequest.of(0, 3);

            List<HouseResponse> houseList = Collections.emptyList();

            Page<HouseResponse> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.findHousesFullTextSearch(searchTerm, pageRequest))
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
            HouseRequest houseRequest = HouseTestData.builder()
                    .build().
                    getRequestDto();
            HouseResponse houseResponse = HouseTestData.builder()
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(houseResponse);

            when(houseService.save(houseRequest))
                    .thenReturn(houseResponse);

            // when, then
            mockMvc.perform(post(URL)
                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpectAll(jsonPath("$.city").value(houseResponse.getCity()),
                            jsonPath("$.area").value(houseResponse.getArea()),
                            jsonPath("$.street").value(houseResponse.getStreet()),
                            jsonPath("$.number").value(houseResponse.getNumber()));
        }
    }

    @Nested
    class Update {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID houseUuid = HOUSE_UUID;

            HouseRequest houseRequest = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build().
                    getRequestDto();
            HouseResponse houseResponse = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(houseResponse);

            when(houseService.update(houseUuid, houseRequest))
                    .thenReturn(houseResponse);

            // when, then
            mockMvc.perform(put(URL + "/" + houseUuid)
                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.city").value(houseResponse.getCity()),
                            jsonPath("$.area").value(houseResponse.getArea()),
                            jsonPath("$.street").value(houseResponse.getStreet()),
                            jsonPath("$.number").value(houseResponse.getNumber()));
        }
    }

    @Nested
    class PatchUpdate {

        @SneakyThrows
        @Test
        void shouldReturnExpectedJsonAndStatus200() {
            // given
            UUID houseUuid = HOUSE_UUID;

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("area", UPDATE_HOUSE_AREA);
            fieldsToUpdate.put("city", UPDATE_HOUSE_CITY);

            HouseResponse houseResponse = HouseTestData.builder()
                    .withArea(UPDATE_HOUSE_AREA)
                    .withCity(UPDATE_HOUSE_CITY)
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(houseResponse);

            when(houseService.patchUpdate(houseUuid, fieldsToUpdate))
                    .thenReturn(houseResponse);

            // when, then
            mockMvc.perform(patch(URL + "/" + houseUuid)
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
            UUID houseUuid = HOUSE_UUID;

            doNothing().when(houseService)
                    .delete(houseUuid);

            // when, then
            mockMvc.perform(delete(URL + "/" + houseUuid))
                    .andExpect(status().isNoContent());
        }
    }
}