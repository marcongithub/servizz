package com.servizz.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/services")
public class ServiceRequestController {

    public static final String BAD_SYNTAX_GET_BY_QUERY = "Either call service without arguments or with bot date-from and date-to arguments provided";
    public static final String ENTITY_NOT_FOUND_FOR = "Unable to update entity with id: %s for %s";
    public static final String SERVICE_QUALIFIER_GET = "GET SERVICE-REQUEST";
    public static final String SERVICE_QUALIFIER_UPDATE = "UPDATE SERVICE-REQUEST";


    private final ServiceRequestRepository serviceRequestRepository;

    @Autowired
    public ServiceRequestController(ServiceRequestRepository serviceRequestRepository){
        this.serviceRequestRepository = serviceRequestRepository;
    }


    @GetMapping(value = "/{serviceId}")
    public ServiceRequest load(@PathVariable Long serviceId) {
        Optional<ServiceRequest> serviceRequest = serviceRequestRepository.findById(serviceId);

        return serviceRequest.orElseThrow(() -> new ServiceRequestNotFoundException(String.format(ENTITY_NOT_FOUND_FOR, serviceId, SERVICE_QUALIFIER_GET)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceRequest createServiceRequest(@RequestBody ServiceRequest serviceRequest) {
        return serviceRequestRepository.save(serviceRequest);
    }

    //TODO find out if there's a way to make spring data throw an exception for non-persisted entities
    @PutMapping("/{serviceId}")
    public void updateServiceRequest(@RequestBody ServiceRequest serviceRequest, @PathVariable Long serviceId) {
        if (!serviceRequestRepository.existsById(serviceId)) {
            throw new ServiceRequestNotFoundException(String.format(ENTITY_NOT_FOUND_FOR, serviceId, SERVICE_QUALIFIER_UPDATE));
        }
        serviceRequestRepository.save(serviceRequest);

    }

    /**
     * Get all service requests within given range or only current ones
     *
     * @param dateFrom
     * @param dateTo
     * @return the list of service requests or an empty list
     */
    @GetMapping
    public List<ServiceRequest> getByQuery(@RequestParam(required = false, value = "date-from") @DateTimeFormat(pattern = "dd.MM.yyyy") Date dateFrom,
                                           @RequestParam(required = false, value = "date-to") @DateTimeFormat(pattern = "dd.MM.yyyy") Date dateTo,
                                           @RequestParam(required = false, value = "service-spec") ServiceRequest.ServiceSpecification serviceSpecification) {
        List<ServiceRequest> list;
        if (dateFrom != null && dateTo != null && serviceSpecification == null) {
            list = serviceRequestRepository.findByDateFromGreaterThanEqualAndDateToLessThanEqual(dateFrom, dateTo);
        } else if (dateFrom == null && dateTo == null && serviceSpecification == null) {
            list = serviceRequestRepository.findCurrent();
        } else if(dateFrom == null && dateTo == null && serviceSpecification != null){
            list = serviceRequestRepository.findAllByServiceType(serviceSpecification);
        }else{
            throw new BadRequestException(BAD_SYNTAX_GET_BY_QUERY);
        }

        return list;
    }

    public class ServiceRequestNotFoundException extends RuntimeException {

        public ServiceRequestNotFoundException(String message) {
            super(message);
        }
    }

    public class BadRequestException extends RuntimeException {

        public BadRequestException(String message) {
            super(message);
        }
    }


}
