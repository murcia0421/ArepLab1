package edu.escuelaing.arem;

import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new RequestHandler(socket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error in server: " + e.getMessage());
        }
    }
}
