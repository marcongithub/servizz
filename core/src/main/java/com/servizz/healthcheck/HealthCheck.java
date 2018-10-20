package com.servizz.healthcheck;

public class HealthCheck {

    private final long id;
    private final String content;

    public HealthCheck(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
