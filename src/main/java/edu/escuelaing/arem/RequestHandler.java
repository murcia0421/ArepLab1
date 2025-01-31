package edu.escuelaing.arem;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestHandler implements Runnable {
    private Socket clientSocket;

    public RequestHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            if (requestLine != null && !requestLine.isEmpty()) {
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String filePath = requestParts[1];

                if (method.equals("GET")) {
                    handleGetRequest(out, filePath);
                } else if (method.equals("POST")) {
                    handlePostRequest(out, filePath);
                } else {
                    sendResponse(out, "HTTP/1.1 405 Method Not Allowed", "Method Not Allowed");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleGetRequest(PrintWriter out, String filePath) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }

        if (filePath.startsWith("/hello")) {
            String name = filePath.contains("?") ? filePath.split("\\?")[1].split("=")[1] : "Guest";
            String response = "Hello, " + name + "!";
            sendResponse(out, "HTTP/1.1 200 OK", "text/plain", response.getBytes());
            return;
        }

        Path path = Paths.get("src/main/resources/static" + filePath);
        if (Files.exists(path)) {
            try {
                String contentType = Files.probeContentType(path);
                byte[] fileContent = Files.readAllBytes(path);
                sendResponse(out, "HTTP/1.1 200 OK", contentType, fileContent);
            } catch (IOException e) {
                sendResponse(out, "HTTP/1.1 500 Internal Server Error", "Internal Server Error");
            }
        } else {
            sendResponse(out, "HTTP/1.1 404 Not Found", "File Not Found");
        }
    }

    private void handlePostRequest(PrintWriter out, String filePath) {
        if (filePath.startsWith("/hellopost")) {
            String name = filePath.contains("?") ? filePath.split("\\?")[1].split("=")[1] : "Guest";
            String response = "Hello from POST, " + name + "!";
            sendResponse(out, "HTTP/1.1 200 OK", "text/plain", response.getBytes());
        } else {
            sendResponse(out, "HTTP/1.1 404 Not Found", "File Not Found");
        }
    }

    private void sendResponse(PrintWriter out, String statusLine, String content) {
        out.println(statusLine);
        out.println("Content-Type: text/plain");
        out.println("Content-Length: " + content.length());
        out.println();
        out.println(content);
    }

    private void sendResponse(PrintWriter out, String statusLine, String contentType, byte[] content) {
        out.println(statusLine);
        out.println("Content-Type: " + contentType);
        out.println("Content-Length: " + content.length);
        out.println();
        out.flush();
        try {
            OutputStream os = clientSocket.getOutputStream();
            os.write(content);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
