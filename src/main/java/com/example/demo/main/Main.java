package com.example.demo.main;

import com.example.demo.controller.MainMenuController;
import com.example.demo.Sound;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * The Main class serves as the entry point for the Sky Battle game. It initializes
 * the main menu, sets up the game window, and manages background music.
 */
public class Main extends Application {

	private static final int SCREEN_WIDTH = 1550;
	private static final int SCREEN_HEIGHT = 900;
	private static final String TITLE = "Sky Battle";
	private Sound backgroundMusic;

	/**
	 * Starts the JavaFX application by setting up the main game window and initializing
	 * the main menu.
	 *
	 * @param stage the primary stage for the application
	 * @throws Exception if there is an error during initialization
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		// Initialize background music
		backgroundMusic = new Sound();
		backgroundMusic.setFile(0); // Index for background music

		// Start playing the music in a loop
		backgroundMusic.loop();

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

	/**
	 * Stops the application and releases resources, including stopping the background music.
	 *
	 * @throws Exception if an error occurs during the stop process
	 */
	@Override
	public void stop() throws Exception {
		// Stop music when the application is closed
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
		super.stop();
	}

	/**
	 * The main method serves as the entry point for the application.
	 *
	 * @param args command-line arguments (not used in this application)
	 */
		public static void main(String[] args) {
		launch();
	}
}