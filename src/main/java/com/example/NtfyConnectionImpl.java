package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;
import java.util.stream.Stream;
import com.google.gson.Gson;

public class NtfyConnectionImpl implements NtfyConnection {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String backendUrl;
    private final String topic;
    private final Gson gson = new Gson();

    public NtfyConnectionImpl(String backendUrl, String topic) {
        this.backendUrl = backendUrl;
        this.topic = topic;
    }

    @Override
    public boolean send(String message) {
        try {
            String json = gson.toJson(new NtfyMessageDto("user", message));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(backendUrl + "/" + topic))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.discarding());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void receive(Consumer<NtfyMessageDto> consumer) {
        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(backendUrl + "/" + topic + "/json"))
                        .build();

                HttpResponse<Stream<String>> response =
                        client.send(request, HttpResponse.BodyHandlers.ofLines());

                response.body().forEach(line -> {
                    if (line.contains("\"message\"")) {
                        NtfyMessageDto dto = gson.fromJson(line, NtfyMessageDto.class);
                        consumer.accept(dto);
                    }
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}