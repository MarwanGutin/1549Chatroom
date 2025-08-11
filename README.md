# 1549 Chatroom
This is a networked chatroom application built in Java. The project is designed to demonstrate a basic client-server model for real-time communication over a network.

## Features
* **Client-Server Architecture:** The application is structured with a central server that manages connections and message relay, and multiple clients that can connect to it.
* **Multi-Client Support:** The server is capable of handling connections from multiple clients simultaneously, allowing for group communication.
* **Command-Line Interface (CLI):** The application uses a simple command-line interface for user interaction, as opposed to a graphical user interface.
* **Private Messaging & Group Info:** Users can send private messages to specified clients and request information about other active members from the coordinator.

## Project Structure
The main project files are located in the chatroom/ directory.

### Key Classes
* **Server.java:** This class acts as the host for the chatroom. It follows the singleton design pattern and is responsible for creating a ServerSocket and spawning a new ClientHandler thread for each client that connects.

* **ClientHandler.java:** This is the largest and most crucial class, acting as an intermediary between the clients and the server. It listens for incoming messages, identifies commands (e.g., /quit, /info, /pm), and handles the logic for broadcasting, private messaging, and providing information to the coordinator.

* **Client.java:** This class represents the end user. It handles connecting to the server, sending messages via a BufferedWriter, and receiving messages. It incorporates an observer pattern in its ListenForMessage method to asynchronously receive messages without blocking the main program flow.

* **Coordinator.java:** This class handles the specific functions of the group coordinator. It manages a list of client information, selects the initial coordinator, chooses a new coordinator when one leaves, and periodically checks for active users. It follows a mix of the utility and singleton design patterns.

## How to Run
* **Compile the Java files:** Navigate to the chatroom/ directory and compile the .java files.

* **Start the Server:** The server must be running before any clients can connect. Run the server application first. It will listen on a hard-coded port.

* **Start the Clients:** Once the server is active, you can launch multiple client applications. Each client will be able to connect to the server and start communicating.

## How It Works
The chatroom uses a client-server model. The Server listens for new connections, and for each new Client, it creates a dedicated ClientHandler thread. This allows the server to manage multiple concurrent conversations. Messages sent from a client are processed by its ClientHandler, which then forwards the message to the appropriate recipients—either broadcasting to all other clients or sending a private message to a specific one. The Coordinator class aids in managing the state of the chatroom, such as tracking which clients are online and managing the coordinator role.
