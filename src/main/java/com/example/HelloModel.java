package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class HelloModel {
    private final HttpClient client = HttpClient.newHttpClient();

    private final String backendUrl = System.getenv().getOrDefault("NTFY_URL", "https://ntfy.sh");
    private final String topic = "javafx-demo-chat";

    public void sendMessage(String user, String message) {
        try {
            // skapar JSONsträng manuellt
            String json = String.format("{\"user\":\"%s\", \"message\":\"%s\"}", user, message);

            System.out.println("Skickar meddelande: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(backendUrl + "/" + topic))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // sicka meddelandet
            client.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMessageListener(Consumer<String> onMessage) {
        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(backendUrl + "/" + topic + "/json"))
                        .build();

                // läs JSONström o vidarebefordra meddelanden
                client.send(request, HttpResponse.BodyHandlers.ofLines())
                        .body()
                        .forEach(line -> {
                            if (line.contains("\"message\"") && line.contains("\"user\"")) {
                                String user = extractValue(line, "user");
                                String message = extractValue(line, "message");
                                if (user != null && message != null) {
                                    System.out.println("Meddelande mottaget: " + user + ": " + message);
                                    onMessage.accept(user + ": " + message);
                                }
                            }
                        });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String extractValue(String jsonLine, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = jsonLine.indexOf(pattern);
        if (start < 0) return null;
        start += pattern.length();
        int end = jsonLine.indexOf("\"", start);
        if (end < 0) return null;
        return jsonLine.substring(start, end);
    }
}