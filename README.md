# Chatty

A simple real-time chat application with a GUI. Multiple users can register, login, and chat with each other.

## Features

- User registration and login
- Real-time messaging between multiple users
- Clean graphical interface

## How to Run

### 1. Start the Server

```bash
javac Server.java
java Server
```

The server will start on port 4242.

### 2. Start the Client

Open a new terminal and run:

```bash
javac ChatGUI.java
java ChatGUI
```

You can open multiple clients to chat between different users.

## How to Use

1. **Register**: Create a new account with a username and password (minimum 8 characters)
2. **Login**: Sign in with your credentials
3. **Chat**: Type your message and press Enter or click Send

## Files

- `Server.java` - Server that handles multiple client connections
- `Client.java` - Client networking and message handling
- `ChatGUI.java` - Graphical user interface
- `MessageListener.java` - Interface for receiving messages
- `UserManager.java` - Handles user registration and login

## Requirements

- Java 8 or higher

## Technical Details

- Uses Java Sockets for networking
- Multi-threaded server (one thread per client)
- Messages are broadcast to all connected users
- GUI built with Java Swing

## Notes

This is a learning project. For production use, you would need to add:
- Password encryption
- Message encryption
- Database for user storage
- Better error handling