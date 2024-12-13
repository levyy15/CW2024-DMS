package com.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * The MainMenuController class handles the logic and event handling for the main menu of the game.
 * It provides functionality to start the game or quit the application, and interacts with the primary stage.
 */
public class MainMenuController {

    @FXML
    private Button startGameButton;

    @FXML
    private Button quitButton;

    private Stage stage;

    /**
     * Sets the stage reference for this controller.
     *
     * @param stage the primary stage of the application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller by setting up event handlers for the buttons.
     */
    @FXML
    public void initialize() {
        // Event handlers for buttons
        startGameButton.setOnAction(this::handleStartGame);
        quitButton.setOnAction(this::handleQuit);
    }

    /**
     * Handles the "Start Game" button click event by initiating the game launch process.
     *
     * @param event the ActionEvent triggered by the button click
     */
    private void handleStartGame(ActionEvent event) {
        try {
            new Controller(stage).launchGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Quit" button click event by closing the application.
     *
     * @param event the ActionEvent triggered by the button click
     */
    private void handleQuit(ActionEvent event) {
        // Exit the application
        System.out.println("Exiting the game...");
        stage.close();
    }
}
