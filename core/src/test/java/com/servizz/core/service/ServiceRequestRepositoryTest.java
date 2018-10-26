package com.servizz.core.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.servizz.core.service.ServiceRequestTestProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DataJpaTest
public class ServiceRequestRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ServiceRequestRepository underTest;

    @Test
    public void create() {
        ServiceRequest serviceRequest = new ServiceRequest("jpa-data-test");
        serviceRequest.setDateFrom(new Date());
        serviceRequest = underTest.saveAndFlush(serviceRequest);
        assertThat(serviceRequest.getId()).isNotNull();
    }

    @Test
    public void findByDescription() {
        entityManager.persist(serviceRequest(8, 1));
        entityManager.flush();
        List<ServiceRequest> list = underTest.findByDescription(SERVICE_REQUEST_DESCRIPTION + 1);
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void findByDescriptionReturnsEmptyList() {
        entityManager.persist(serviceRequest(8, 1));
        entityManager.flush();
        List<ServiceRequest> list = underTest.findByDescription(SERVICE_REQUEST_DESCRIPTION + 2);
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    public void findByDescriptionContaining() {
        entityManager.persist(serviceRequest(8, 1));
        entityManager.persist(serviceRequest(9, 2));
        entityManager.persist(serviceRequest(10, 3));
        entityManager.flush();

        List<ServiceRequest> list = underTest.findByDescriptionContaining(SERVICE_REQUEST_DESCRIPTION);
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    public void findCurrent() {
        int currentMonth = LocalDate.now().getMonthValue();
        entityManager.persist(serviceRequest(currentMonth, 1));
        entityManager.persist(serviceRequest((currentMonth >= 12 ? currentMonth - 1 : currentMonth + 1), 2));
        entityManager.persist(serviceRequest(currentMonth, 3));
        entityManager.flush();

        List<ServiceRequest> list = underTest.findCurrent();
        //underTest.findAll().stream().forEach((serviceRequest) -> System.out.println(serviceRequest.toString()));
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void findByDateFromGreaterThanEqualAndDateToLessThanEqual() {
        serviceRequestsOfOneYear().forEach((serviceRequest) -> entityManager.persist(serviceRequest));
        entityManager.flush();
        //find all q3
        List<ServiceRequest> list = underTest.findByDateFromGreaterThanEqualAndDateToLessThanEqual(dateInCurrentYear(6), dateInCurrentYear(9));
        assertThat(list.size()).isEqualTo(2);
        //find all q4
        list = underTest.findByDateFromGreaterThanEqualAndDateToLessThanEqual(dateInCurrentYear(10), dateInCurrentYear(12));
        assertThat(list.size()).isEqualTo(1);
        //find all between january and february
        list = underTest.findByDateFromGreaterThanEqualAndDateToLessThanEqual(dateInCurrentYear(1), dateInCurrentYear(2));
        assertThat(list.size()).isEqualTo(2);
        //find all of current year
        list = underTest.findByDateFromGreaterThanEqualAndDateToLessThanEqual(dateInCurrentYear(1), dateInCurrentYear(12));
        assertThat(list.size()).isEqualTo(7);
    }


}
