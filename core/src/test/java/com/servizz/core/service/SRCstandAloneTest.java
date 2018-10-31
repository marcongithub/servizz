package com.servizz.core.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class SRCstandAloneTest {

    private MockMvc mvc;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private ServiceRequestController serviceRequestController;

    @Before
    public void setup() {
        // We would need this line if we would not use MockitoJUnitRunner
        // MockitoAnnotations.initMocks(this);
        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(serviceRequestController)
//                .setControllerAdvice(new SuperHeroExceptionHandler())
//                .addFilters(new SuperHeroFilter())
                .build();
    }

    @Test
    public void load() throws Exception {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setDescription("mocked description");
        Optional<ServiceRequest> optional = Optional.of(serviceRequest);
        given(serviceRequestRepository.findById(anyLong())).willReturn(optional);
        RequestBuilder requestBuilder = get("/services/{serviceId}",1L);

        mvc.perform(requestBuilder);
    }
}
