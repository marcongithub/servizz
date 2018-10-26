package com.servizz.core.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.assertj.core.api.Java6Assertions.assertThat;


//TODO: Setup isolated integration test directory and adapt maven
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class SRCfullStackWithH2Test {

    public static final String DESCRIPTION = "Test Request";
    public static final String CHANGED_DESCRIPTION = "Changed description";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;



    private Long serviceRequestId;

    @Before
    public void setUp() {
    }

    @Test
    public void serviceRequestFound() throws Exception {
        //persist some data
        ServiceRequest serviceRequestExpected = persistServiceRequest();
        Long id = serviceRequestExpected.getId();
        //make call
        ResponseEntity<ServiceRequest> response = this.restTemplate.getForEntity("http://localhost:" + port + "/services/" + id, ServiceRequest.class);
        //assert
        ServiceRequest serviceRequest = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(serviceRequest.getDescription()).isEqualTo(serviceRequestExpected.getDescription());
    }

    @Test
    public void serviceRequestNotFound() throws Exception {
        //when
        ResponseEntity<ServiceRequest> response = this.restTemplate.getForEntity("http://localhost:" + port + "/services/42", ServiceRequest.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getId()).isNull();

        System.out.println("######## "+response.toString());
        System.out.println("++++++++ "+response.getBody().toString());

    }

    @Test
    public void createServiceRequest() throws Exception {
        // when
        ServiceRequest serviceRequest = new ServiceRequest("Test description");
        ResponseEntity<ServiceRequest> response = restTemplate.postForEntity("/services",
                serviceRequest, ServiceRequest.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getDescription()).isEqualTo(serviceRequest.getDescription());
        assertThat(response.getBody().getId()).isNotNull();
    }

    public void createServiceRequestFails() {
        //TODO
    }

    @Test
    public void updateServiceRequest() {
        ServiceRequest toUpdate = persistServiceRequest();
        toUpdate.setDescription(CHANGED_DESCRIPTION);
        Long id = toUpdate.getId();

        ResponseEntity<Void> response =
                restTemplate.exchange("http://localhost:" + port + "/services/" + id, HttpMethod.PUT, new HttpEntity<ServiceRequest>(toUpdate), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<ServiceRequest> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/services/" + id, ServiceRequest.class);
        assertThat(getResponse.getBody().getDescription()).isEqualTo(CHANGED_DESCRIPTION);
    }

    @Test
    public void updateServiceRequestFails() {
        ServiceRequest toUpdate = new ServiceRequest("Failing update");
        Field id = ReflectionUtils.findField(ServiceRequest.class, "id");
        ReflectionUtils.makeAccessible(id);
        ReflectionUtils.setField(id, toUpdate, 4222l);

        ResponseEntity<Void> response =
                restTemplate.exchange("http://localhost:" + port + "/services/4222", HttpMethod.PUT, new HttpEntity<ServiceRequest>(toUpdate), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    private ServiceRequest persistServiceRequest() {
        ServiceRequest serviceRequestExpected = new ServiceRequest();
        serviceRequestExpected.setDescription(DESCRIPTION);
        serviceRequestExpected = serviceRequestRepository.saveAndFlush(serviceRequestExpected);
//        serviceRequestExpected = serviceRequestRepository.save(serviceRequestExpected);


        serviceRequestId = serviceRequestExpected.getId();
        return serviceRequestExpected;
    }


}
