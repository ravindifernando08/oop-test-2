package com.example.ticket_management_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.ticket_management_system.model.Configuration;
import com.example.ticket_management_system.service.ConfigurationService;
import com.example.ticket_management_system.service.ThreadManager;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ThreadManager threadManager;

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @PostMapping("/configure")
    public String configure(@RequestBody Configuration configuration) {
        logger.info("Received Configuration: {}", configuration);
        configurationService.setConfiguration(configuration);
        threadManager.initializeTicketPool(configuration.getMaxTicketCapacity(), configuration);
        threadManager.setTotalTickets(configuration.getTotalTickets());
        return "Configuration set successfully!";
    }

    @PostMapping("/start")
    public String startSystem() {
        if (!configurationService.isConfigured()) {
            return "System not configured. Please configure first.";
        }
        Configuration config = configurationService.getConfiguration();
        threadManager.start(config.getTicketReleaseRate(), config.getCustomerRetrievalRate());
        return "System started.";
    }

    @PostMapping("/stop")
    public String stopSystem() {
        threadManager.stopAll();
        return "System stopped.";
    }

    @GetMapping("/tickets")
    public String getTicketUpdates() {
        return threadManager.getTicketUpdates();
    }
}
