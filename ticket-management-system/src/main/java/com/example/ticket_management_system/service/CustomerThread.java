package com.example.ticket_management_system.service;

import com.example.ticket_management_system.model.TicketPool;

import java.util.concurrent.BlockingQueue;

public class CustomerThread implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final BlockingQueue<String> updates;

    public CustomerThread(TicketPool ticketPool, int retrievalRate, BlockingQueue<String> updates) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.updates = updates;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ticketPool.removeTicket();
            updates.add("Ticket consumed. Current tickets in pool: " + ticketPool.getCurrentTickets());
            try {
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
