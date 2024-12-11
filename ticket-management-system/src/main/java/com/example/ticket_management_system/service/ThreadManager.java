package com.example.ticket_management_system.service;

import com.example.ticket_management_system.model.TicketPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import com.example.ticket_management_system.model.Configuration;

import org.springframework.stereotype.Service;

@Service
public class ThreadManager {
    private TicketPool ticketPool;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private final LinkedBlockingQueue<String> updates = new LinkedBlockingQueue<>();

    private int totalTickets;

    public void initializeTicketPool(int maxTicketCapacity, Configuration configuration) {
        this.ticketPool = new TicketPool(maxTicketCapacity, configuration);
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void start(int releaseRate, int retrievalRate) {
        Runnable vendor = new VendorThread(ticketPool, releaseRate, totalTickets, updates);
        Runnable customer = new CustomerThread(ticketPool, retrievalRate, updates);

        executorService.submit(vendor);
        executorService.submit(customer);
    }

    public void stopAll() {
        executorService.shutdownNow();
        executorService = Executors.newCachedThreadPool(); // Reset executor service
        updates.clear();
    }

    public String getTicketUpdates() {
        StringBuilder builder = new StringBuilder();
        updates.forEach(update -> builder.append(update).append("\n"));
        updates.clear(); // Clear the queue after processing
        return builder.toString();
    }
    
}
