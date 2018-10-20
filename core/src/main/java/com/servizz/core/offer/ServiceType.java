package com.servizz.core.offer;

public class ServiceType {

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


    private Long id;


    private ServiceSpecification specification;


}
