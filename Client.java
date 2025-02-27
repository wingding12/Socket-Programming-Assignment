/*
    William Ding
    CS 446 - Assignment 1
    Client.java
*/

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5001;
    private static final String CLIENT_NAME = "William Ding Client";

    public static void main(String[] args) {
        Scanner scanner = null;
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        
        try {
            // get user input
            scanner = new Scanner(System.in);
            System.out.print("Enter an integer between 1 and 100: ");
            int clientNumber = scanner.nextInt();
            
            // connect to server
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);
            
            // data streams
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            
            // send data to server
            out.writeUTF(CLIENT_NAME);
            out.writeInt(clientNumber);
            
            // receive response from server
            String serverName = in.readUTF();
            int serverNumber = in.readInt();
            
            // display information
            System.out.println("Client name: " + CLIENT_NAME);
            System.out.println("Server name: " + serverName);
            System.out.println("Client's number: " + clientNumber);
            System.out.println("Server's number: " + serverNumber);
            
            // calculate and display sum
            int sum = clientNumber + serverNumber;
            System.out.println("Sum: " + sum);
            
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            // close resources
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                if (scanner != null) scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} 