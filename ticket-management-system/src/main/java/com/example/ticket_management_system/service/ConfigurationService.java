package com.example.ticket_management_system.service;


import org.springframework.stereotype.Service;

import com.example.ticket_management_system.model.Configuration;

@Service
public class ConfigurationService {
    private Configuration configuration;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean isConfigured() {
        return configuration != null;
    }
}
