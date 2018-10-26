package com.servizz.core.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.servizz.core.service.ServiceRequestTestProvider.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ServiceRequestController.class)
public class ServiceRequestControllerTest {

    @MockBean
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<ServiceRequest> jsonServiceRequest;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Ignore
    public void printAsJsonString() throws IOException {
        ServiceRequest serviceRequest = getDefaultServiceRequest();
        String jsonRequest = jsonServiceRequest.write(serviceRequest).getJson();
        System.out.println("" + jsonRequest);
    }

    @Test
    public void serviceRequestFound() throws Exception {
        ServiceRequest expected = new ServiceRequest("mocked description");
        //given
        given(serviceRequestRepository.findById(anyLong())).willReturn(Optional.of(expected));
        //when
        MockHttpServletRequestBuilder requestBuilder = get("/services/{serviceId}", Long.valueOf(1));
        ResultActions resultActions = mvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON));
        //then
        //assertion with hamcrest
        resultActions.andExpect(status().isOk()).andExpect(content().string(equalTo(jsonServiceRequest.write(expected).getJson())));
        // assertion with assertJ
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonServiceRequest.write(expected).getJson());

    }

    @Test
    public void serviceRequestNotFound() throws Exception {
        String errorMsg = String.format(ServiceRequestController.ENTITY_NOT_FOUND_FOR, 1, ServiceRequestController.SERVICE_QUALIFIER_GET);
        //given
        given(serviceRequestRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        MockHttpServletRequestBuilder requestBuilder = get("/services/{serviceId}", 1l);

        ResultActions resultActions = mvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(content().string(containsString(errorMsg)));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void createServiceRequest() throws Exception {
        ServiceRequest serviceRequestNew = new ServiceRequest("newServerRequest");
        given(serviceRequestRepository.save(any(ServiceRequest.class))).willReturn(serviceRequestNew);
        MockHttpServletRequestBuilder requestBuilder = post("/services/").
                contentType(MediaType.APPLICATION_JSON).
                content(jsonServiceRequest.write(getDefaultServiceRequest()).getJson());
        mvc.perform(requestBuilder).
                andExpect(status().isCreated()).
                andExpect(content().string(containsString("newServerRequest")));
    }

    @Test
    public void updateServiceRequest() throws Exception {
        ServiceRequest serviceRequestNew = new ServiceRequest("newServerRequest");
        given(serviceRequestRepository.existsById(anyLong())).willReturn(true);

        MockHttpServletRequestBuilder requestBuilder = put("/services/42").contentType(MediaType.APPLICATION_JSON).
                content(jsonServiceRequest.write(getDefaultServiceRequest()).getJson());

        mvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void updateServiceRequestFails() throws Exception {
        String errorMsg = String.format(ServiceRequestController.ENTITY_NOT_FOUND_FOR, 42, ServiceRequestController.SERVICE_QUALIFIER_UPDATE);
        ServiceRequest serviceRequestNew = new ServiceRequest("newServerRequest");
        given(serviceRequestRepository.existsById(anyLong())).willReturn(false);

        MockHttpServletRequestBuilder requestBuilder = put("/services/{serviceId}", 42).contentType(MediaType.APPLICATION_JSON).
                content(jsonServiceRequest.write(getDefaultServiceRequest()).getJson());

        mvc.perform(requestBuilder).andExpect(status().isNotFound()).andExpect(content().string(containsString(errorMsg)));
    }

    //getByQuery tests

    public void getServiceRequestsInRange(){

    }

    public void getCurrentServiceRequests(){

    }

    public void getWithoutDateFrom(){

    }

    public void getWithoutDateTo(){

    }

    public void getReturnsEmptyList(){

    }

}
