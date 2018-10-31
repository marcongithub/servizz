package com.servizz.core.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByDescription(String description);

    List<ServiceRequest> findByDescriptionContaining(String description);

    /**
     * Find all valid within date range
     *
     * @param dateFrom
     * @param dateTo
     * @return
     */
    List<ServiceRequest> findByDateFromGreaterThanEqualAndDateToLessThanEqual(Date dateFrom, Date dateTo);

    /**
     * Find current, i.e. by current between to and from dates.
     *
     * @return
     */
    @Query("select s from  ServiceRequest s where CURRENT_DATE between s.dateFrom and s.dateTo")
    List<ServiceRequest> findCurrent();


    List<ServiceRequest> findAllByServiceType(ServiceRequest.ServiceSpecification serviceType);
}
