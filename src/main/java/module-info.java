module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;

    opens com.example to javafx.fxml, com.google.gson;

    exports com.example;
}