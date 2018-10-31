package com.servizz.core.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class SRCwithoutServerWithH2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    private Long serviceRequestId;

    @Before
    public void setUp() {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setDescription("Test Request");
        serviceRequest = serviceRequestRepository.saveAndFlush(serviceRequest);
        serviceRequestId = serviceRequest.getId();

    }

    @Test
    public void load() throws Exception {

        RequestBuilder requestBuilder = get("/services/{serviceId}", serviceRequestId);

        MockHttpServletResponse response =
                mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println("+++++++++++ "+response.getContentAsString());
    }
}
