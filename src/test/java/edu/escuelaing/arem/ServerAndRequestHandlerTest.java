package edu.escuelaing.arem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerAndRequestHandlerTest {

    @BeforeAll
    public static void startServer() {
        new Thread(() -> {
            Server.main(new String[] {});
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHandleGetRequest() throws IOException {
        String request = "GET /hello?name=John HTTP/1.1\r\n\r\n";
        String response = sendRequest(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Hello, John!"));
    }

    @Test
    public void testHandlePostRequest() throws IOException {
        String request = "POST /hellopost?name=Jane HTTP/1.1\r\n\r\n";
        String response = sendRequest(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Hello from POST, Jane!"));
    }

    @Test
    public void testHandleNonExistentPath() throws IOException {
        String request = "GET /nonexistent HTTP/1.1\r\n\r\n";
        String response = sendRequest(request);

        assertTrue(response.contains("HTTP/1.1 404 Not Found"));
    }

    @Test
    public void testHandleStaticFile() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\n\r\n";
        String response = sendRequest(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("text/html"));
    }

    @Test
    public void testServerResponse() throws IOException {
        String request = "GET /hello?name=John HTTP/1.1\r\n\r\n";
        String response = sendRequest(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Hello, John!"));
    }

    private String sendRequest(String request) throws IOException {
        try (Socket socket = new Socket("localhost", 8080);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(request);
            out.flush();

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\r\n");
            }
            return response.toString();
        }
    }

    @Test
    public void testConcurrentRequests() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            futures.add(executor.submit(
                    () -> sendRequest("GET /hello?name=User" + Thread.currentThread().getId() + " HTTP/1.1\r\n\r\n")));
        }

        for (Future<String> future : futures) {
            try {
                String response = future.get();
                assertTrue(response.contains("HTTP/1.1 200 OK"));
            } catch (ExecutionException e) {
                fail("Error en la solicitud concurrente: " + e.getMessage());
            }
        }

        executor.shutdown();
    }

    @Test
    public void testStaticFileCSS() throws IOException {
        String request = "GET /styles.css HTTP/1.1\r\n\r\n";
        String response = sendRequest(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"), "El servidor no devolvió un código 200 para el archivo CSS.");
        assertTrue(response.contains("text/css"), "El tipo MIME no es text/css.");
    }

    @Test
    public void testStaticFileHTML() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\n\r\n";
        String response = sendRequest(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"), "El servidor no devolvió un código 200 para el archivo HTML.");
        assertTrue(response.contains("text/html"), "El tipo MIME no es text/html.");
    }

    @Test
    public void testPerformance() throws IOException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String request = "GET /hello?name=User" + i + " HTTP/1.1\r\n\r\n";
            String response = sendRequest(request);
            assertTrue(response.contains("HTTP/1.1 200 OK"));
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 1000, "El servidor tardó demasiado en responder: " + duration + " ms");
    }
}