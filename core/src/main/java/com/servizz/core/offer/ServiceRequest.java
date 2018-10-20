package com.servizz.core.offer;

import javax.persistence.*;
import java.io.Serializable;
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
    private ServiceType.ServiceSpecification serviceType;

    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateFrom;

    @Temporal(TemporalType.DATE)
    private Date dateTo;


    public ServiceType.ServiceSpecification getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType.ServiceSpecification serviceType) {
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
}
