package com.wooyoung85.shoppingmallrestapi.events;

import com.wooyoung85.shoppingmallrestapi.accounts.repository.AccountRepository;
import com.wooyoung85.shoppingmallrestapi.accounts.service.AccountService;
import com.wooyoung85.shoppingmallrestapi.common.BaseControllerTest;
import com.wooyoung85.shoppingmallrestapi.common.TestDescription;
import com.wooyoung85.shoppingmallrestapi.configs.AppProperties;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;


    @Test
    @TestDescription("정상으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
            .name("Spring")
            .description("Test")
            .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .beginEventDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .endEventDateTime(LocalDateTime.of(2019, 12, 1, 11, 20))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("Test")
            .build();

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
            .andExpect(jsonPath("free").value(false))
            .andExpect(jsonPath("offline").value(true))
            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.query-events").exists())
            .andExpect(jsonPath("_links.update-event").exists())
            .andDo(document("create-event",
                links(
                    linkWithRel("self").description("link to self"),
                    linkWithRel("query-events").description("link to query events"),
                    linkWithRel("update-event").description("link to update an existing"),
                    linkWithRel("profile").description("link to update an existing")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                requestFields(
                    fieldWithPath("name").description("Name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollmentDateTime"),
                    fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                    fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                    fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("base price of new event"),
                    fieldWithPath("maxPrice").description("max price of new event"),
                    fieldWithPath("limitOfEnrollment").description("limit of new event")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.LOCATION).description("Location header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                ),
                responseFields(
                    fieldWithPath("id").description("identifier of new event"),
                    fieldWithPath("name").description("Name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollmentDateTime"),
                    fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                    fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                    fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("base price of new event"),
                    fieldWithPath("maxPrice").description("max price of new event"),
                    fieldWithPath("limitOfEnrollment").description("limit of new event"),
                    fieldWithPath("free").description("it tells if this event is free or not"),
                    fieldWithPath("offline").description("it tells if this event is offline or not"),
                    fieldWithPath("eventStatus").description("event status"),
                    fieldWithPath("manager.id").description("manager of event"),
                    fieldWithPath("manager.email").description("manager of event"),
                    fieldWithPath("manager.password").description("manager of event"),
                    fieldWithPath("manager.name").description("manager of event"),
                    fieldWithPath("manager.roles").description("manager of event"),
                    fieldWithPath("_links.self.href").description("link to self"),
                    fieldWithPath("_links.query-events.href").description("link to query events"),
                    fieldWithPath("_links.update-event.href").description("link to update event"),
                    fieldWithPath("_links.profile.href").description("link to profile")
                )
            ))
        ;
    }

    private String getBearerToken() throws Exception {
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
            .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
            .param("username", appProperties.getUserEmail())
            .param("password", appProperties.getUserPassword())
            .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return "Bearer " + parser.parseMap(responseBody).get("access_token").toString();
    }


    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
            .id(100)
            .name("Spring")
            .description("Test")
            .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .beginEventDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .endEventDateTime(LocalDateTime.of(2019, 12, 1, 11, 20))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("Test")
            .build();

        mockMvc.perform(post("/api/events/")
            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
            .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
            .name("Spring")
            .description("Test")
            .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 28, 11, 20))
            .beginEventDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .endEventDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .basePrice(100)
            .maxPrice(10)
            .limitOfEnrollment(100)
            .location("Test")
            .build();

        mockMvc.perform(post("/api/events/")
            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(eventDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("content[0].objectName").exists())
            .andExpect(jsonPath("content[0].defaultMessage").exists())
            .andExpect(jsonPath("content[0].code").exists())
            .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0,30).forEach(i -> this.generateEvent(i));

        //When
        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("page").exists())
            .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.profile").exists())
            .andDo(document("query-events"))
        ;
    }

    @Test
    @TestDescription("인증정보를 가지고 30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEventsWithAuthentication() throws Exception {
        //Given
        IntStream.range(0,30).forEach(i -> this.generateEvent(i));

        //When
        this.mockMvc.perform(get("/api/events")
            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
            .param("page", "1")
            .param("size", "10")
            .param("sort", "name,DESC")
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("page").exists())
            .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.profile").exists())
            .andExpect(jsonPath("_links.create-event").exists())
            .andDo(document("query-events"))
        ;
    }

    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception {
        //Given
        Event event = this.generateEvent(100);

        //When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").exists())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.profile").exists())
            .andDo(document("get-an-event"))
        ;
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        //When & Then
        this.mockMvc.perform(get("/api/events/9999"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        //Given
        Event event = this.generateEvent(200);
        String eventName = "Update Event";
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setName(eventName);

        //When
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
            )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패")
    public void updateEvent400Empty() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        //When
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent400Wrong() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        //When
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent404() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = modelMapper.map(event, EventDto.class);

        //When
        this.mockMvc.perform(put("/api/events/99999", event.getId())
            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
            .andDo(print())
            .andExpect(status().isNotFound())
        ;
    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
            .name("event" + i)
            .description("test event")
            .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 29, 11, 20))
            .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .beginEventDateTime(LocalDateTime.of(2019, 11, 30, 11, 20))
            .endEventDateTime(LocalDateTime.of(2019, 12, 1, 11, 20))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("Test")
            .free(false)
            .offline(true)
            .eventStatus(EventStatus.DRAFT)
            .build();

        return this.eventRepository.save(event);
    }
}