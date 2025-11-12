package com.example;

import com.google.gson.Gson;
import java.util.function.Consumer;
import java.util.Map;

public class HelloModel {
    private final NtfyConnection connection;
    private final Gson gson = new Gson();

    public HelloModel() {
        String backendUrl = System.getenv().getOrDefault("NTFY_URL", "https://ntfy.sh");
        String topic = "javafx-demo-chat";
        this.connection = new NtfyConnectionImpl(backendUrl, topic);
    }

    // skickar json med username o message
    public void sendMessage(String username, String message) {
        String formattedMessage = username + ": " + message;
        connection.send(formattedMessage);
    }

    public void startMessageListener(Consumer<String> onMessage) {
        connection.receive(dto -> {
            String formatted = dto.getMessage();
            onMessage.accept(formatted);
        });
    }
}