package com.example.ticket_management_system.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketPool {
    private final int maxTicketCapacity;
    private int currentTickets;
    private final Configuration configuration;

    public TicketPool(int maxTicketCapacity, Configuration configuration) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.currentTickets = configuration.getTotalTickets();
        this.configuration = configuration;
    }

    private final Logger logger = LoggerFactory.getLogger(TicketPool.class);

    public synchronized void addTicket() {
        while (currentTickets >= maxTicketCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        currentTickets++;
        logger.info("Ticket Count Add: " + currentTickets);
        notifyAll();
    }

    public synchronized void removeTicket() {
        while (currentTickets <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        currentTickets--;
        logger.info("Ticket Count Remove: " + currentTickets);
        notifyAll();
    }

    public synchronized int getCurrentTickets() {
        return currentTickets;
    }
}
