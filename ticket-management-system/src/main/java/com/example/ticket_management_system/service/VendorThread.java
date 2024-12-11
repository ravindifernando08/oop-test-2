package com.example.ticket_management_system.service;

import com.example.ticket_management_system.model.TicketPool;

import java.util.concurrent.BlockingQueue;

public class VendorThread implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate;
    private int ticketsLeft;
    private final BlockingQueue<String> updates;

    public VendorThread(TicketPool ticketPool, int releaseRate, int ticketsLeft, BlockingQueue<String> updates) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.ticketsLeft = ticketsLeft;
        this.updates = updates;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && ticketsLeft > 0) {
            ticketPool.addTicket();
            ticketsLeft--;
            updates.add("Ticket added. Remaining tickets to release: " + ticketsLeft);
            try {
                Thread.sleep(releaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
