package com.servizz.core.service;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


//TODO: override equals and hashcode, read article
@Entity
public class ServiceRequest implements Serializable {

    @Id
    @SequenceGenerator(name = "ServiceRequestSeqGen", sequenceName = "SERVICE_REQUEST_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(generator = "ServiceRequestSeqGen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "SERVICE_TYPE")
    @Enumerated(EnumType.STRING)
    private ServiceSpecification serviceType;

    private String description;

    /**
     * service request valid from
     */
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date dateFrom;

    /**
     * service request valid to
     */
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date dateTo;

    public ServiceRequest() {
    }

    public ServiceRequest(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public ServiceSpecification getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceSpecification serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "{ id: " + id +
                ", description: " + description +
                ", type: " + ((this.serviceType != null) ? serviceType.name() : null) +
                ", dateFrom: " + (dateFrom != null ? new SimpleDateFormat("dd-MM-yyyy").format(dateFrom) : null) +
                ", dateTo: " + (dateTo != null ? new SimpleDateFormat("dd-MM-yyyy").format(dateTo)  :null) +
                "}";
    }

    public enum ServiceCategory {
        CLEANING, CRAFT, RELOCATION
    }

    public enum ServiceSpecification {

        HOUSE_CLEANING(ServiceCategory.CLEANING),
        GARDEN_CLEANING(ServiceCategory.CLEANING),
        PAINTING(ServiceCategory.CRAFT),
        NATIONAL_RELOCATION(ServiceCategory.RELOCATION),
        INTERNATIONAL_RELOCATION(ServiceCategory.RELOCATION);

        private final ServiceCategory serviceCategory;

        ServiceSpecification(ServiceCategory serviceCategory) {
            this.serviceCategory = serviceCategory;
        }
    }
}
