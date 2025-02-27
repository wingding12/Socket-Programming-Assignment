import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 8888;
    private static final String SERVER_NAME = "William Ding's Server";
    private static final int SERVER_NUMBER = 42; // Server's chosen number between 1-100

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        
        try {
            // Create server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for client connections...");
            
            // Server runs continuously until receiving out-of-range integer
            while (true) {
                Socket clientSocket = null;
                DataInputStream in = null;
                DataOutputStream out = null;
                
                try {
                    // Accept client connection
                    clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    
                    // Set up data streams
                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new DataOutputStream(clientSocket.getOutputStream());
                    
                    // Read client data
                    String clientName = in.readUTF();
                    int clientNumber = in.readInt();
                    
                    // Display information
                    System.out.println("Client's name: " + clientName);
                    System.out.println("Server's name: " + SERVER_NAME);
                    
                    // Check if client number is in range (1-100)
                    if (clientNumber < 1 || clientNumber > 100) {
                        System.out.println("Client sent out-of-range value: " + clientNumber);
                        System.out.println("Server shutting down...");
                        return; // Exit program
                    }
                    
                    // Calculate and display sum
                    int sum = clientNumber + SERVER_NUMBER;
                    System.out.println("Client's number: " + clientNumber);
                    System.out.println("Server's number: " + SERVER_NUMBER);
                    System.out.println("Sum: " + sum);
                    
                    // Send response to client
                    out.writeUTF(SERVER_NAME);
                    out.writeInt(SERVER_NUMBER);
                    
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                } finally {
                    // Close resources
                    try {
                        if (out != null) out.close();
                        if (in != null) in.close();
                        if (clientSocket != null) clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            // Close server socket
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} 