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

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

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

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);
		UserPlane.resetHealth(user.getHealth());

		timeline.stop();
	}

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

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
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
		background.setOnKeyReleased(e -> {
            KeyCode kc = e.getCode();
            if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
        });
		root.getChildren().add(background);
	}

	private void togglePause() {
		if (isPaused) {
			resumeGame();
		} else {
			pauseGame();
		}

		dimOverlay.toFront();
		pausedLabel.toFront();
	}

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

	private void pauseGame() {
		isPaused = true;
		timeline.pause();
		pausedLabel.setVisible(true);
		dimOverlay.setVisible(true);
	}

	private void resumeGame() {
		isPaused = false;
		timeline.play();
		pausedLabel.setVisible(false);
		dimOverlay.setVisible(false);
	}

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

	private void options() {
		if (optionsBox == null) {
			Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 24);

			optionsBox = new VBox();
			optionsBox.setLayoutX(screenWidth / 2 - 300);
			optionsBox.setLayoutY(screenHeight / 2 - 200);
			optionsBox.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 70px; -fx-border-color: white; -fx-border-width: 2px; -fx-spacing: 10px;");

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
				optionsBox.setVisible(false);
				startGame();
			});

			optionsBox.getChildren().addAll(menuButton, continueButton, quitButton);
			root.getChildren().add(optionsBox);
		}

		optionsBox.setVisible(true);
		timeline.stop();
	}

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
	private void handleRestartGame() {
		setChanged();
		notifyObservers("com.example.demo.levels.LevelOne");
	}


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
	protected void initializeKillCounter() {
		killCountText = new Text("Kills: 0 / " + killsToAdvance);
		Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelifySans-Regular.ttf"), 32);

		killCountText.setFont(pixelFont);
		killCountText.setFill(Color.WHITE);
		killCountText.setX(8);
		killCountText.setY(120);
		root.getChildren().add(killCountText);
	}

	public void updateBossHealth(Boss boss) {
		if (this instanceof LevelThree && bossHealthBar != null) {
			double healthPercentage = (double) boss.getHealth() / boss.getMaxHealth();
			bossHealthBar.setProgress(healthPercentage);
			if (healthPercentage <= 0) {
				root.getChildren().remove(bossHealthContainer);
			}
		}
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);

		soundEffects.setFile(1);
		soundEffects.play();
	}

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

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
			soundEffects.setFile(3);
			soundEffects.play();
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyUnits.forEach(ActiveActorDestructible::updateActor);
		userProjectiles.forEach(ActiveActorDestructible::updateActor);
		enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(ActiveActorDestructible::isDestroyed)
				.toList();
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
		for (ActiveActorDestructible actor : enemyUnits) {
			if (actor instanceof Boss) {
				updateBossHealth((Boss) actor);
			}
		}
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

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

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
		killCountText.setText("Kills: " + user.getNumberOfKills() + " / " + killsToAdvance);
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
		showEndOptions();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage(0.4, 0.5);
		showEndOptions();
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

}