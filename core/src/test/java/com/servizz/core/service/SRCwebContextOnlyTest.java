package com.servizz.core.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.*;
/**
 * Loads web layer, i.e. Filter, Controller Advice, etc. are loaded as well (making it a kind of integration test).
 **/
@RunWith(SpringRunner.class)
@WebMvcTest(ServiceRequestController.class)
public class SRCwebContextOnlyTest {

    @MockBean
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private MockMvc mvc;


    public void updateServiceRequest() throws Exception {

    }

    @Test
    public void load() throws Exception {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setDescription("mocked description");
        Optional<ServiceRequest> optional = Optional.of(serviceRequest);
        given(serviceRequestRepository.findById(anyLong())).willReturn(optional);
        RequestBuilder requestBuilder = get("/services/{serviceId}",Long.valueOf(1));

        mvc.perform(requestBuilder);
    }

    public void loadThrowsException(){

    }
}
