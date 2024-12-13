module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.demo to javafx.fxml;
    opens com.example.demo.controller to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.images.menu to javafx.fxml;
    opens com.example.demo.projectiles to javafx.fxml;
    opens com.example.demo.levels to javafx.fxml;
    exports com.example.demo.main;
    opens com.example.demo.main to javafx.fxml;
    opens com.example.demo.ui to javafx.fxml;
    opens com.example.demo.models to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.models;
    exports com.example.demo.ui;
}