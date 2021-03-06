package com.servizz.core.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceRequestTestProvider {

    public static final String SERVICE_REQUEST_DESCRIPTION = "service request-";

    private static ServiceRequest serviceRequest(int monthFrom, int monthTo, int index, ServiceRequest.ServiceSpecification serviceType) {
        assertThat(monthTo).isLessThan(13);
        ServiceRequest serviceRequest = serviceRequest(monthFrom, index);
        //overwrite default
        serviceRequest.setDateTo(dateInCurrentYear(monthTo));
        serviceRequest.setServiceType(serviceType);
        return serviceRequest;
    }

    static ServiceRequest serviceRequest(int monthFrom, int index) {
        assertThat(monthFrom).isLessThan(13);
        ServiceRequest serviceRequest = new ServiceRequest(SERVICE_REQUEST_DESCRIPTION + index);
        Date dateFrom = dateInCurrentYear(monthFrom);
        int monthTo = monthFrom == 12 ? monthFrom : monthFrom + 1;
        Date dateTo = dateInCurrentYear(monthTo);
        serviceRequest.setDateFrom(dateFrom);
        serviceRequest.setDateTo(dateTo);

        return serviceRequest;
    }

    static Date dateInCurrentYear(int month) {
        LocalDate now = LocalDate.now();
        now = now.withMonth(month);
        return Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    static ServiceRequest getDefaultServiceRequest() {
        ServiceRequest serviceRequest = new ServiceRequest("Test description");
        serviceRequest.setDateFrom(new Date());
        serviceRequest.setDateTo(new Date());
        serviceRequest.setServiceType(ServiceRequest.ServiceSpecification.NATIONAL_RELOCATION);
        return serviceRequest;
    }

    static List<ServiceRequest> serviceRequestsOfOneYear() {
        ArrayList<ServiceRequest> requestsOfOneYear = new ArrayList<>();
        //q1
        requestsOfOneYear.add(serviceRequest(1, 1, 11, ServiceRequest.ServiceSpecification.NATIONAL_RELOCATION));
        requestsOfOneYear.add(serviceRequest(1, 2, 12, ServiceRequest.ServiceSpecification.GARDEN_CLEANING));
        requestsOfOneYear.add(serviceRequest(2, 3, 13, ServiceRequest.ServiceSpecification.HOUSE_CLEANING));
        //q2
        requestsOfOneYear.add(serviceRequest(4, 5, 21, ServiceRequest.ServiceSpecification.INTERNATIONAL_RELOCATION));
        //q3
        requestsOfOneYear.add(serviceRequest(8, 8, 31, ServiceRequest.ServiceSpecification.PAINTING));
        requestsOfOneYear.add(serviceRequest(6, 9, 32, ServiceRequest.ServiceSpecification.HOUSE_CLEANING));
        //q4
        requestsOfOneYear.add(serviceRequest(10, 12, 41, ServiceRequest.ServiceSpecification.PAINTING));

        return requestsOfOneYear;
    }
}
