package org.webapp;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebApp {
    public static void main(String [] args) throws IOException {
    // Set up the HTTP server
    HttpServer server = HttpServer.create(new InetSocketAddress(11111), 0);
    server.createContext("/", new HtmlHandler());
    server.start();
    System.out.println(server.getAddress());
    }

    static class HtmlHandler implements HttpHandler {
        public void handle (HttpExchange t) throws IOException {
            // Get the request
            String requestMethod = t.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                // Prepare the response

                String response = Files.readString(Paths.get(
                        "src/main/java/org/webapp/login_page.html"));

                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}