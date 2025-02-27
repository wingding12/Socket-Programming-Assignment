/*
    William Ding
    CS 446 - Assignment 1
    Server.java
*/

import java.io.*;
import java.net.*;

public class Server {
    // Constants for the server
    private static final int PORT = 5001;  // using port 5001
    private static final String SERVER_NAME = "Server of William Ding";
    private static final int SERVER_NUMBER = 42;  // using 42
    
    // boolean to keep running the server
    private static volatile boolean keepRunning = true;

    public static void main(String[] args) {
        // create server socket
        ServerSocket welcomeSocket = null;
        
        try {
            // create server socket to listen on port 5001
            welcomeSocket = new ServerSocket(PORT);
            System.out.println("Listening on port " + PORT);
            System.out.println("Waiting for clients to connect...");
            
            // continue accepting new clients until we shut down
            while (keepRunning) {
                try {
                    // wait until a client connects
                    Socket clientSocket = welcomeSocket.accept();
                    
                    // print the client
                    System.out.println("New client connected: " + clientSocket.getInetAddress());
                    
                    // create a new thread to handle this client
                    ClientWorker worker = new ClientWorker(clientSocket);
                    Thread clientThread = new Thread(worker);
                    
                    // start the thread to process the client
                    clientThread.start();
                    
                } catch (IOException e) {
                    System.out.println("Error accepting client");
                }
            }
            
            System.out.println("Server main loop has ended");
            
        } catch (IOException e) {
            System.out.println("SERVER ERROR");
        } finally {
            try {
                if (welcomeSocket != null) welcomeSocket.close();
            } catch (IOException e) { }
        }
    }
    
    // this is a separate class that does the actual client handling
    private static class ClientWorker implements Runnable {
        private Socket clientSocket;  // socket connected to the client
        
        // constructor - just save the socket
        public ClientWorker(Socket socket) {
            this.clientSocket = socket;
        }
        
        // this is the code that runs in the separate thread
        @Override
        public void run() {
            DataInputStream in = null;
            DataOutputStream out = null;
            
            try {
                // set up streams to read/write data with the client
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                
                // read the information the client sent us
                String clientName = in.readUTF();
                int clientNumber = in.readInt();
                
                // show what we received
                System.out.println("Thread: Name = " + clientName + ", Number = " + clientNumber);
                
                // check if the client's number is valid
                if (clientNumber < 1 || clientNumber > 100) {
                    System.out.println("⚠ Client sent invalid number: " + clientNumber);
                    System.out.println("⚠ Shutting down server...");
                    
                    // tell the main thread to stop accepting new clients
                    keepRunning = false;
                    return;
                }
                
                // calculate and display sum
                int sum = clientNumber + SERVER_NUMBER;
                System.out.println("Client number: " + clientNumber);
                System.out.println("Server number: " + SERVER_NUMBER);
                System.out.println("Sum: " + sum + "\n");
                
                // send response to client
                out.writeUTF(SERVER_NAME);
                out.writeInt(SERVER_NUMBER);
                
                System.out.println("Sent response to " + clientName + '\n');
                
            } catch (IOException e) {
                System.out.println("Error with client connection");
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (clientSocket != null) clientSocket.close();
                } catch (IOException e) { }
            }
        }
    }
}