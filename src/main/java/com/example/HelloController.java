package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {

    private final HelloModel model = new HelloModel();

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private void initialize() {
        model.startMessageListener(message -> Platform.runLater(() -> {
            chatArea.appendText(message + "\n");
        }));
    }

    @FXML
    private void onSendClicked() {
        String text = inputField.getText();
        if (text.isBlank()) return;

        model.sendMessage("Kian", text);
        inputField.clear();
    }
}