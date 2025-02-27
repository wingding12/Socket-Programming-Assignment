import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5001;
    private static final String SERVER_NAME = "Server of William Ding";

    // server number is 12
    private static final int SERVER_NUMBER = 12;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        
        try {
            // server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for client connections...");
            
            // run the server until we get a bad integer
            while (true) {
                Socket clientSocket = null;
                DataInputStream in = null;
                DataOutputStream out = null;
                
                try {
                    // client connection
                    clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    
                    // data streams
                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new DataOutputStream(clientSocket.getOutputStream());
                    
                    // read client data
                    String clientName = in.readUTF();
                    int clientNumber = in.readInt();
                    
                    // display information
                    System.out.println("Client's name: " + clientName);
                    System.out.println("Server's name: " + SERVER_NAME);
                    
                    // return an error if the client number is out of range
                    if (clientNumber < 1 || clientNumber > 100) {
                        System.out.println("Client sent out-of-range value: " + clientNumber);
                        System.out.println("Server shutting down...");
                        return;
                    }
                    
                    // calculate and display sum
                    int sum = clientNumber + SERVER_NUMBER;
                    System.out.println("Client's number: " + clientNumber);
                    System.out.println("Server's number: " + SERVER_NUMBER);
                    System.out.println("Sum: " + sum + '\n');
                    
                    // send response to client
                    out.writeUTF(SERVER_NAME);
                    out.writeInt(SERVER_NUMBER);
                    
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                } finally {
                    // close resources
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
            // close server socket
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