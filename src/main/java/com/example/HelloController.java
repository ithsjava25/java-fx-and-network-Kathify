package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller layer: connects UI with the model.
 */
public class HelloController {

    private final HelloModel model = new HelloModel();

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private void initialize() {
        // Starta lyssnare på inkommande meddelanden
        model.startMessageListener(message -> Platform.runLater(() -> {
            chatArea.appendText(message + "\n");
        }));
    }

    @FXML
    private void onSendClicked() {
        String text = inputField.getText();
        if (text.isBlank()) return;

        model.sendMessage("Kian", text);  // byt ut "Kian" till ditt namn om du vill
        inputField.clear();
    }
}