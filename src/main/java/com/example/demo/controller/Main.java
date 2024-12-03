package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	private static final int SCREEN_WIDTH = 1550;
	private static final int SCREEN_HEIGHT = 800;
	private static final String TITLE = "Sky Battle";
	private Controller myController;
	private Sound backgroundMusic;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		// Initialize background music
		backgroundMusic = new Sound();
		backgroundMusic.setFile(0); // Index for background music
		backgroundMusic.loop(); // Loop the background music

		// Load the main menu FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/images/menu/MainMenu.fxml"));
		Parent root = loader.load();

		// Set the scene for the main menu
		Scene mainMenuScene = new Scene(root);
		stage.setScene(mainMenuScene);

		// Pass the stage reference to MainMenuController
		MainMenuController controller = loader.getController();
		controller.setStage(stage);

		// Show the stage
		stage.show();

	}

	@Override
	public void stop() throws Exception {
		// Stop music when the application is closed
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
		super.stop();
	}

		public static void main(String[] args) {
		launch();
	}
}