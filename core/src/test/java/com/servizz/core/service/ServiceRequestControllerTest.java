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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.servizz.core.service.ServiceRequestTestProvider.getDefaultServiceRequest;
import static com.servizz.core.service.ServiceRequestTestProvider.serviceRequestsOfOneYear;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ServiceRequestController.class)
public class ServiceRequestControllerTest {

    @MockBean
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<ServiceRequest> jsonServiceRequest;

    private JacksonTester<List<ServiceRequest>> jsonServiceRequestList;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Ignore
    public void printAsJsonString() throws IOException {
        ServiceRequest serviceRequest = getDefaultServiceRequest();
        String jsonRequest = jsonServiceRequest.write(serviceRequest).getJson();

        List<ServiceRequest> serviceRequestsOfOneYear = serviceRequestsOfOneYear();
        String jsonList = jsonServiceRequestList.write(serviceRequestsOfOneYear).getJson();
    }

    @Test
    public void serviceRequestFound() throws Exception {
        ServiceRequest serviceRequestExpected = new ServiceRequest("mocked description");
        String serviceRequestExpectedJson = jsonServiceRequest.write(serviceRequestExpected).getJson();
        //given
        given(serviceRequestRepository.findById(anyLong())).willReturn(Optional.of(serviceRequestExpected));
        //when
        MockHttpServletRequestBuilder requestBuilder = get("/services/{serviceId}", 1L);
        ResultActions resultActions = mvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk()).andExpect(content().json(serviceRequestExpectedJson));
    }

    @Test
    public void serviceRequestNotFound() throws Exception {
        String errorMsg = String.format(ServiceRequestController.ENTITY_NOT_FOUND_FOR, 1, ServiceRequestController.SERVICE_QUALIFIER_GET);
        //given
        given(serviceRequestRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        MockHttpServletRequestBuilder requestBuilder = get("/services/{serviceId}", 1L);

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

    /*********** getByQuery tests ***************/

    @Test
    public void getServiceRequestsInRange() throws Exception {
        List<ServiceRequest> serviceRequestsOfOneYear = serviceRequestsOfOneYear();
        String serviceRequestsOfOneYearExpectedJson = jsonServiceRequestList.write(serviceRequestsOfOneYear).getJson();

        given(serviceRequestRepository.findByDateFromGreaterThanEqualAndDateToLessThanEqual(any(Date.class), any(Date.class))).
                willReturn(serviceRequestsOfOneYear);
        ResultActions resultActions = mvc.perform(get("/services").
                param("date-from", "12.10.2018").param("date-to", "13.10.2018"));
        resultActions.andExpect(status().isOk()).andExpect(content().json(serviceRequestsOfOneYearExpectedJson));
    }

    @Test
    public void getCurrentServiceRequests() throws Exception {
        List<ServiceRequest> currentServiceRequests = Collections.singletonList(getDefaultServiceRequest());
        String currentServiceRequestsJson = jsonServiceRequestList.write(currentServiceRequests).getJson();

        given(serviceRequestRepository.findCurrent()).willReturn(currentServiceRequests);
        ResultActions resultActions = mvc.perform(get("/services"));
        resultActions.andExpect(status().isOk()).andExpect(content().json(currentServiceRequestsJson));
    }

    @Test
    public void getServiceRequestsBySpec() throws Exception {
        ServiceRequest paintingRequest = getDefaultServiceRequest();
        paintingRequest.setServiceType(ServiceRequest.ServiceSpecification.PAINTING);
        List<ServiceRequest> paintingRequests = Collections.singletonList(paintingRequest);
        String paintingRequestsJson = jsonServiceRequestList.write(paintingRequests).getJson();

        given(serviceRequestRepository.findAllByServiceType(any(ServiceRequest.ServiceSpecification.class))).willReturn(paintingRequests);

        ResultActions resultActions = mvc.perform(get("/services").param("service-spec", ServiceRequest.ServiceSpecification.PAINTING.name()));
        resultActions.andExpect(status().isOk()).andExpect(content().json(paintingRequestsJson));

    }

    @Test
    public void getWithoutDateFrom() throws Exception {
        ResultActions resultActions = mvc.perform(get("/services").
                param("date-to", "01.01.2011"));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void getWithoutDateTo() throws Exception {
        ResultActions resultActions = mvc.perform(get("/services").
                param("date-from", "01.02.2013"));
        resultActions.andExpect(status().isBadRequest());
    }

    //TODO find out how to test empty json list result
    @Test
    public void getReturnsEmptyList() {

    }

}
