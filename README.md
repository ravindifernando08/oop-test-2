**Ticket Management System**

**Introduction**
The Ticket management system is a client server application designed to simulate 
a ticketing system where tickets are dynamically released by a vendor and retrieved by customers.
The system ensures real-time updates and maintains a configurable pool of tickets, allowing you to control various parameteres such as ticket release rate, customer retrieval rate, and maximum ticket capacity.

**Setup Instructions**
Before setting up the application checked if the relevant prerequisites are installed,
JDK
Maven for building the backend application
Node.js 

**Technology Stack**
CLI: JAVA
Frontend: React.js
Backend: Springboot

**Usage Instructions**

**Configuring the System**
Upon starting the CLI, the application will prompt you to configure the ticketing system with the following parameters:

Total Tickets: Total number of tickets available for release.

Ticket Release Rate: Interval (in milliseconds) at which tickets are released by the vendor.

Customer Retrieval Rate: Interval (in milliseconds) at which tickets are retrieved by customers.

Maximum Ticket Capacity: Maximum number of tickets that can be stored in the pool at a time.

Enter positive integer values for each parameter. Configuration will be sent to the backend server.

**Starting and Stopping the System**
After configuration:

Start the system: Select the option to start the ticketing process. The vendor will release tickets at the specified rate, and customers will retrieve them simultaneously.

Stop the system: Select the option to halt all ticketing processes. This stops both the vendor and customer threads.

Exit the application: Choose the exit option to terminate the CLI.

**Real-Time Updates**

When the system is running, the CLI will display real-time updates from the backend, including:

Tickets being added to the pool by the vendor.

Tickets being consumed by customers.

These updates are fetched every second to ensure the latest state of the system is displayed.

**Explanation of UI Controls**

This application uses a command-line interface with the following options:

Start System: Starts the ticketing process.

Stop System: Stops the ticketing process.

Exit: Exits the application.

During configuration, numerical inputs are required to set the system parameters. After configuration, menu options can be selected by entering the corresponding number.
