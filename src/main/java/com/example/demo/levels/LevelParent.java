package com.example.demo.levels;
import com.example.demo.controller.MainMenuController;


import java.util.*;

import com.example.demo.models.ActiveActorDestructible;
import com.example.demo.models.Boss;
import com.example.demo.models.FighterPlane;
import com.example.demo.models.UserPlane;
import com.example.demo.projectiles.UserProjectileUlt;
import com.example.demo.ui.LevelView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javafx.scene.layout.HBox;
import javafx.scene.control.ProgressBar;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import com.example.demo.Sound;

import javafx.scene.shape.Rectangle;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Abstract class LevelParent.
 * This class serves as a base class for implementing different levels in the game.
 * It manages game components such as user planes, enemy units, projectiles, and the game timeline.
 * LevelParent includes methods for initializing and updating the game scene, handling user inputs,
 * managing game states (e.g., pause and options), and transitioning between levels.
 *
 * <p>Original Source Code:
 * {@link com.example.demo.levels.LevelParent}
 */
public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	protected final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;

	private int currentNumberOfEnemies;
	private final LevelView levelView;
	private boolean isPaused = false;
	private Label pausedLabel;
	private HBox bossHealthContainer;
    private ProgressBar bossHealthBar;
    private final Sound soundEffects;

    private Rectangle dimOverlay;
    private VBox optionsBox;
	private final int killsToAdvance;
	private Text killCountText;


	/**
	 * Constructor for LevelParent.
	 *
	 * @param backgroundImageName The name of the background image file.
	 * @param screenHeight        The height of the game screen.
	 * @param screenWidth         The width of the game screen.
	 * @param playerInitialHealth Initial health of the player.
	 * @param killsToAdvance      Number of kills required to advance to the next level.
	 */
	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, int killsToAdvance) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.killsToAdvance = killsToAdvance;

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;

		this.soundEffects = new Sound();
		initializeTimeline();
		friendlyUnits.add(user);
	}

	/**
	 * Initializes friendly units specific to the level.
	 */
	protected abstract void initializeFriendlyUnits();

	/**
	 * Checks if the game is over and handles appropriate actions.
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Spawns enemy units for the level.
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Instantiates the LevelView object for the level.
	 *
	 * @return The instantiated LevelView object.
	 */
	protected abstract LevelView instantiateLevelView();

	/**
	 * Initializes the game scene, including background, units, and UI elements.
	 *
	 * @return The initialized Scene object.
	 */
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay(user.getHealth());
		initializeBossHealthDisplay();
		initializePausedLabelDisplay();
		initializeOptionsButton();
		initializeKillCounter();
		return scene;
	}

	/**
	 * Starts the game by playing the game timeline.
	 */
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	/**
	 * Transitions to the next level.
	 *
	 * @param levelName The name of the next level to transition to.
	 */
	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);
		UserPlane.resetHealth(user.getHealth());

		timeline.stop();
	}

	/**
	 * Updates the game scene by spawning enemies, updating actors, and checking collisions.
	 */
	protected void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();

	}

	/**
	 * Initializes the game timeline for periodic updates.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	/**
	 * Initializes the background image and user input handlers.
	 */
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		/**
		 * Handles key press events for user input.
		 *
		 * @param e The KeyEvent representing the user input.
		 */
		background.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();
            if (kc == KeyCode.UP) user.moveUp();
            if (kc == KeyCode.W) user.moveUp();
            if (kc == KeyCode.DOWN) user.moveDown();
            if (kc == KeyCode.S) user.moveDown();
            if (kc == KeyCode.SPACE) fireProjectile();
            if (kc == KeyCode.M) fireUlt();
            if (kc==KeyCode.P) togglePause();
            if (kc==KeyCode.ESCAPE) options();
        });

		/**
		 * Handles key release events for user input.
		 *
		 * @param e The KeyEvent representing the user input.
		 */
		background.setOnKeyReleased(e -> {
            KeyCode kc = e.getCode();
            if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
        });
		root.getChildren().add(background);
	}

	/**
	 * Toggles the game pause state.
	 */
	private void togglePause() {
		if (isPaused) {
			resumeGame();
		} else {
			pauseGame();
		}

		dimOverlay.toFront();
		pausedLabel.toFront();
	}

	/**
	 * Initializes the paused label display.
	 */
	private void initializePausedLabelDisplay() {
		this.dimOverlay = new Rectangle(screenWidth, screenHeight, Color.BLACK);
		this.dimOverlay.setOpacity(0.5);
		this.dimOverlay.setVisible(false);

		Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 70);

		this.pausedLabel = new Label("PAUSED");
		this.pausedLabel.setFont(pixelFont);
		this.pausedLabel.setTextFill(Color.RED);
		this.pausedLabel.setVisible(false);
		this.pausedLabel.setLayoutX(screenWidth / 2 - 100);
		this.pausedLabel.setLayoutY(screenHeight / 2 - 25);

        root.getChildren().addAll(dimOverlay, pausedLabel);
	}

	/**
	 * Pauses the game timeline and shows the pause overlay.
	 */
	private void pauseGame() {
		isPaused = true;
		timeline.pause();
		pausedLabel.setVisible(true);
		dimOverlay.setVisible(true);
	}

	/**
	 * Resumes the game timeline and hides the pause overlay.
	 */
	private void resumeGame() {
		isPaused = false;
		timeline.play();
		pausedLabel.setVisible(false);
		dimOverlay.setVisible(false);
	}

	/**
	 * Initializes the options button on the game screen.
	 */
	private void initializeOptionsButton() {
        Button settingsButton = new Button("Options");
		Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 22);

		settingsButton.setFont(pixelFont); // Optional: Style the button
		settingsButton.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");

		settingsButton.setLayoutX(screenWidth - 150);
		settingsButton.setLayoutY(20);

		settingsButton.setOnAction(e -> options());

		root.getChildren().add(settingsButton);
	}

	/**
	 * Displays the options menu, pausing the game in the process.
	 */
	private void options() {
		if (getOptionsBox() == null) {
			Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 24);

			setOptionsBox(new VBox());
			getOptionsBox().setLayoutX(screenWidth / 2 - 300);
			getOptionsBox().setLayoutY(screenHeight / 2 - 200);
			getOptionsBox().setStyle(" -fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 70px; -fx-border-color: white; -fx-border-width: 2px; -fx-spacing: 10px;");

			Button menuButton = new Button("Main Menu");
			menuButton.setFont(pixelFont);
			menuButton.setStyle("-fx-padding: 10px 20px; -fx-min-width: 450px;");

			menuButton.setOnAction(e -> {
				timeline.stop();
				loadMainMenu();
			});

			Button quitButton = new Button("Quit");
			quitButton.setFont(pixelFont);
			quitButton.setStyle("-fx-padding: 10px 20px; -fx-min-width: 450px;");

			quitButton.setOnAction(e -> System.exit(0));

			Button continueButton = new Button("Continue");
			continueButton.setFont(pixelFont);
			continueButton.setStyle("-fx-padding: 10px 20px; -fx-min-width: 450px;");

			continueButton.setOnAction(e -> {
				getOptionsBox().setVisible(false);
				startGame();
			});

			getOptionsBox().getChildren().addAll(menuButton, continueButton, quitButton);
			root.getChildren().add(getOptionsBox());
		}

		getOptionsBox().setVisible(true);
		timeline.stop();
	}

	/**
	 * Loads the main menu scene.
	 */
	private void loadMainMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/images/menu/MainMenu.fxml"));
			Parent rootMenu = loader.load();

			Scene mainMenuScene = new Scene(rootMenu);

			Stage primaryStage = (Stage) root.getScene().getWindow();
			primaryStage.setScene(mainMenuScene);

			MainMenuController controller = loader.getController();
			controller.setStage(primaryStage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays the end-game options, including Restart and Exit buttons, in a styled VBox.
	 * This method is invoked when the game ends, either in victory or defeat.
	 */
	public void showEndOptions() {
		VBox optionsBox = new VBox(10);
		Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 24);

		optionsBox.setAlignment(Pos.CENTER);
		optionsBox.setLayoutX(screenWidth / 2 - 300);
		optionsBox.setLayoutY(screenHeight / 2 - 0);
		optionsBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 70px; -fx-border-color: white; -fx-border-width: 2px; -fx-spacing: 10px;");

		Button restartButton = new Button("Restart");
		restartButton.setFont(pixelFont);
		restartButton.setStyle("-fx-padding: 10px 20px; -fx-min-width: 450px;");
		restartButton.setOnAction(event -> handleRestartGame());

		Button exitButton = new Button("Exit");
		exitButton.setFont(pixelFont);
		exitButton.setStyle("-fx-padding: 10px 20px; -fx-min-width: 450px;");
		exitButton.setOnAction(event -> Platform.exit());

		optionsBox.getChildren().addAll(restartButton, exitButton);

		root.getChildren().add(optionsBox);
	}

	/**
	 * Handles the logic to restart the game by notifying observers with the next level's class.
	 */
	private void handleRestartGame() {
		setChanged();
		notifyObservers("com.example.demo.levels.LevelOne");
	}

	/**
	 * Initializes the display of the boss health bar for Level Three.
	 * This method dynamically creates and styles the health bar and label.
	 */
	protected void initializeBossHealthDisplay() {
		if (this instanceof LevelThree) {
			bossHealthContainer = new HBox();
			bossHealthContainer.setSpacing(10);
			bossHealthContainer.setLayoutX(450);
			bossHealthContainer.setLayoutY(50);

            Label bossHealthLabel = new Label("Boss Health: ");
			Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 28);
			bossHealthLabel.setFont(pixelFont);
			bossHealthLabel.setTextFill(Color.RED);

			bossHealthBar = new ProgressBar(1.0);
			bossHealthBar.setPrefWidth(500);
			bossHealthBar.setPrefHeight(25);

			bossHealthContainer.getChildren().addAll(bossHealthLabel, bossHealthBar);
			root.getChildren().add(bossHealthContainer);
		}
	}

	/**
	 * Sets up the kill counter display, showing the player's progress toward the required kills to advance.
	 */
	protected void initializeKillCounter() {
		killCountText = new Text("Kills: 0 / " + killsToAdvance);
		Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 32);

		killCountText.setFont(pixelFont);
		killCountText.setFill(Color.WHITE);
		killCountText.setX(8);
		killCountText.setY(120);
		root.getChildren().add(killCountText);
	}

	/**
	 * Updates the boss's health bar based on the boss's current health.
	 * If the boss's health reaches zero, the health bar is removed from the screen.
	 *
	 * @param boss The boss whose health is being monitored.
	 */
	public void updateBossHealth(Boss boss) {
		if (this instanceof LevelThree && bossHealthBar != null) {
			double healthPercentage = (double) boss.getHealth() / boss.getMaxHealth();
			bossHealthBar.setProgress(healthPercentage);
			if (healthPercentage <= 0) {
				root.getChildren().remove(bossHealthContainer);
			}
		}
	}

	/**
	 * Fires a standard projectile from the user's plane, adds it to the game, and plays a firing sound effect.
	 */
	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);

		soundEffects.setFile(1);
		soundEffects.play();
	}

	/**
	 * Fires an ultimate attack consisting of multiple projectiles in a spread pattern.
	 * The projectiles are added to the game, and an ultimate attack sound effect is played.
	 */
	private void fireUlt() {

		double centerX = user.getTranslateX() + user.getLayoutX() + 110;
		double centerY = user.getTranslateY() + user.getLayoutY() + 20;

		double[] angleOffsets = {-5, 0, 5};

		for (double angleOffset : angleOffsets) {

			UserProjectileUlt projectile = new UserProjectileUlt(centerX, centerY, angleOffset);

			root.getChildren().add(projectile);
			userProjectiles.add(projectile);

			soundEffects.setFile(2);
			soundEffects.play();
		}
	}

	/**
	 * Generates projectile fire from enemy units and spawns them in the game.
	 */
	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	/**
	 * Adds an enemy projectile to the game and plays the corresponding sound effect.
	 *
	 * @param projectile The enemy projectile to be added.
	 */
	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
			soundEffects.setFile(3);
			soundEffects.play();
		}
	}

	/**
	 * Updates all active game actors, ensuring their positions and states are refreshed for the current frame.
	 */
	private void updateActors() {
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyUnits.forEach(ActiveActorDestructible::updateActor);
		userProjectiles.forEach(ActiveActorDestructible::updateActor);
		enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
	}

	/**
	 * Removes all actors from the game that have been marked as destroyed.
	 */
	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	/**
	 * Removes destroyed actors from the specified list and the game root node.
	 *
	 * @param actors The list of actors to check for destruction.
	 */
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(ActiveActorDestructible::isDestroyed)
				.toList();
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	/**
	 * Handles collisions between friendly and enemy planes, applying damage to both actors involved.
	 */
	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	/**
	 * Handles collisions between user projectiles and enemy units, applying damage and updating boss health if applicable.
	 */
	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
		for (ActiveActorDestructible actor : enemyUnits) {
			if (actor instanceof Boss) {
				updateBossHealth((Boss) actor);
			}
		}
	}

	/**
	 * Handles collisions between enemy projectiles and friendly units, applying damage to both actors involved.
	 */
	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	/**
	 * Processes collisions between two groups of actors, applying damage to both actors that collide.
	 *
	 * @param actors1 The first list of actors to check for collisions.
	 * @param actors2 The second list of actors to check for collisions.
	 */
	private void handleCollisions(List<ActiveActorDestructible> actors1,
								  List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}

	/**
	 * Checks for enemies that have penetrated defenses and applies damage to the user accordingly.
	 */
	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	/**
	 * Updates the display of the user's health in the level view.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	/**
	 * Updates the kill counter display based on the number of enemies defeated.
	 */
	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
		killCountText.setText("Kills: " + user.getNumberOfKills() + " / " + killsToAdvance);
	}

	/**
	 * Determines whether an enemy has penetrated the player's defenses.
	 *
	 * @param enemy The enemy to check.
	 * @return True if the enemy has crossed the defensive line, false otherwise.
	 */
	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	/**
	 * Handles the actions to perform when the player wins the game, including stopping the timeline,
	 * displaying a win image, and showing end options.
	 */
	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
		showEndOptions();
	}

	/**
	 * Handles the actions to perform when the player loses the game, including stopping the timeline,
	 * displaying a game-over image, and showing end options.
	 */
	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage(0.4, 0.5);
		showEndOptions();
	}

	/**
	 * Retrieves the user's plane object.
	 *
	 * @return The user's plane.
	 */
	protected UserPlane getUser() {
		return user;
	}

	/**
	 * Retrieves the root group node of the game scene.
	 *
	 * @return The root group node.
	 */
	protected Group getRoot() {
		return root;
	}

	/**
	 * Retrieves the current number of enemies in the game.
	 *
	 * @return The number of enemies.
	 */
	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	/**
	 * Adds an enemy unit to the game and to the root node for rendering.
	 *
	 * @param enemy The enemy unit to add.
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	/**
	 * Retrieves the maximum Y position allowed for enemy units.
	 *
	 * @return The maximum Y position.
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	/**
	 * Retrieves the screen width of the game.
	 *
	 * @return The screen width.
	 */
	protected double getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Checks if the user's plane has been destroyed.
	 *
	 * @return True if the user's plane is destroyed, false otherwise.
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	/**
	 * Updates the current number of enemies in the game.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	public VBox getOptionsBox() {
		return optionsBox;
	}

	public void setOptionsBox(VBox optionsBox) {
		this.optionsBox = optionsBox;
	}
}