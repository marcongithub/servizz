package com.servizz.healthcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HealthCheckController {

    private static final String template = "Hello %s!";
    private final AtomicLong counter = new AtomicLong();



    @RequestMapping(value = "/healthcheck", produces = {"application/json"})
    public HealthCheck greeting(@RequestParam(value = "name", defaultValue = "Servizz") String name) {
        return new HealthCheck(counter.incrementAndGet(),
                String.format(template, name));
    }
}
