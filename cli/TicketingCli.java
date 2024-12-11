import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TicketingCli {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static boolean systemRunning = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean configured = false;

        while (true) {
            if (!configured) {
                System.out.println("Configure the system:");
                System.out.print("Total Tickets: ");
                int totalTickets = scanner.nextInt();
                validateInput(totalTickets);
                System.out.print("Ticket Release Rate (ms): ");
                int releaseRate = scanner.nextInt();
                System.out.print("Customer Retrieval Rate (ms): ");
                int retrievalRate = scanner.nextInt();
                System.out.print("Maximum Ticket Capacity: ");
                int maxCapacity = scanner.nextInt();

                String configurationPayload = String.format(
                    "{\"totalTickets\":%d,\"ticketReleaseRate\":%d,\"customerRetrievalRate\":%d,\"maxTicketCapacity\":%d}",
                    totalTickets, releaseRate, retrievalRate, maxCapacity
                );  

                System.err.println(configurationPayload);


                try {
                    sendPostRequest("/configure", configurationPayload);
                    configured = true;
                    System.out.println("Configuration successful!");
                } catch (Exception e) {
                    System.out.println("Configuration failed. Please try again.");
                }
            } else {
                System.out.println("1. Start System");
                System.out.println("2. Stop System");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                try {
                    switch (choice) {
                        case 1 -> {
                            sendPostRequest("/start", null);
                            systemRunning = true;
                            startRealTimeUpdates();
                        }
                        case 2 -> {
                            sendPostRequest("/stop", null);
                            systemRunning = false;
                        }
                        case 3 -> System.exit(0);
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static void validateInput(int input) {
        if (input < 0) {
            System.out.println("Error: Enter a valid number");
        }
        return;
    }

    private static void sendPostRequest(String endpoint, String payload) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        if (payload != null) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(payload.getBytes("UTF-8"));
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            System.out.println("Request was successful.");
        } else {
            System.out.println("Request failed with response code: " + responseCode);
        }
        connection.disconnect();
    }

    private static void startRealTimeUpdates() {
        new Thread(() -> {
            while (systemRunning) {
                try {
                    String updates = sendGetRequest("/tickets");
                    if (!updates.isEmpty()) {
                        System.out.println(updates);
                    }
                    Thread.sleep(1000); // Poll every second
                } catch (Exception e) {
                    System.out.println("Error fetching updates: " + e.getMessage());
                    systemRunning = false;
                }
            }
        }).start();
    }

    private static String sendGetRequest(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        connection.disconnect();
        return response.toString();
    }
}
