package com.servizz;


import com.servizz.core.offer.ServiceRequest;
import com.servizz.core.offer.ServiceRequestRepository;
import com.servizz.core.offer.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;


//TODO: adapt mvn to copy env-props to user-defined location
@SpringBootApplication
public class ServizzSetupApplication implements CommandLineRunner {

    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    private Logger logger = LoggerFactory.getLogger(ServizzSetupApplication.class);
    @Value("${application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(ServizzSetupApplication.class, args);
    }

    public void run(String... args) throws Exception {
        logger.info("STARTING SETUP FOR " + appName + " ...");
        initServiceRequestTable();
    }

    private void initServiceRequestTable() {
        //relocation
        ServiceRequest relocationServiceRequest = new ServiceRequest();
        relocationServiceRequest.setDateFrom(new Date());
        relocationServiceRequest.setDateTo(new Date());
        relocationServiceRequest.setDescription("Umzug von der Boschetsrieder Straße nach Bernried a. S.");
        relocationServiceRequest.setServiceType(ServiceType.ServiceSpecification.NATIONAL_RELOCATION);
        serviceRequestRepository.save(relocationServiceRequest);
        //garden cleaning
        ServiceRequest gardenCleaningServiceRequest = new ServiceRequest();
        gardenCleaningServiceRequest.setDateFrom(new Date());
        gardenCleaningServiceRequest.setDateTo(new Date());
        gardenCleaningServiceRequest.setDescription("Vorgarten aufräumen");
        gardenCleaningServiceRequest.setServiceType(ServiceType.ServiceSpecification.GARDEN_CLEANING);
        serviceRequestRepository.save(gardenCleaningServiceRequest);
    }
}
