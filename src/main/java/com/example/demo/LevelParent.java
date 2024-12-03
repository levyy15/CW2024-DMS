package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;

import javafx.scene.layout.HBox;
import javafx.scene.control.ProgressBar;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import com.example.demo.controller.Sound;

import javafx.scene.effect.BoxBlur;
import javafx.scene.shape.Rectangle;


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
	private LevelView levelView;
	private boolean isPaused = false;
	private Label pausedLabel;
	private HBox bossHealthContainer;
	private Label bossHealthLabel;
	private ProgressBar bossHealthBar;
	private Button settingsButton;
	private Sound soundEffects;
	private Rectangle dimOverlay;
	private BoxBlur blurEffect;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;

		this.soundEffects = new Sound(); // Initialize sound effects
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
		levelView.showHeartDisplay();
		initializeBossHealthDisplay();
		initializePausedLabelDisplay();
//		initializeSettingsButton();
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);

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
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.SPACE) fireProjectile();
				if (kc == KeyCode.M) fireUlt();
				if (kc==KeyCode.P) togglePause();
				if (kc==KeyCode.ESCAPE) options();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
			}
		});
		root.getChildren().add(background);
	}

	// Method to toggle pause and resume the game
	private void togglePause() {
		if (isPaused) {
			resumeGame();  // Resume the game if it's currently paused
		} else {
			pauseGame();   // Pause the game if it's running
		}

		dimOverlay.toFront();
		pausedLabel.toFront();
	}

	private void initializePausedLabelDisplay() {
		// Create a semi-transparent rectangle for the dim effect
		this.dimOverlay = new Rectangle(screenWidth, screenHeight, Color.BLACK);
		this.dimOverlay.setOpacity(0.5);  // Semi-transparent
		this.dimOverlay.setVisible(false);  // Initially hidden

		this.pausedLabel = new Label("PAUSED");
		this.pausedLabel.setFont(new Font("Arial", 50));  // Set large font size
		this.pausedLabel.setTextFill(Color.RED);  // Set the text color to red
		this.pausedLabel.setVisible(false);  // Initially hidden
		this.pausedLabel.setLayoutX(screenWidth / 2 - 100);  // Center horizontally
		this.pausedLabel.setLayoutY(screenHeight / 2 - 25);  // Center vertically


		// Create a blur effect
		this.blurEffect = new BoxBlur(10, 10, 3);  // Blur radius and iterations

		root.getChildren().addAll(dimOverlay, pausedLabel);  // Add the label to the root group
	}

	// Method to pause the game
	private void pauseGame() {
		isPaused = true;
		timeline.pause();  // Stop the game loop
		pausedLabel.setVisible(true);
		dimOverlay.setVisible(true);
	}

	// Method to resume the game
	private void resumeGame() {
		isPaused = false;
		timeline.play();  // Restart the game loop
		pausedLabel.setVisible(false);
		dimOverlay.setVisible(false);
	}

	private void options(){
		
	}


	protected void initializeBossHealthDisplay() {
		// Only create boss health bar if this is LevelThree
		if (this instanceof LevelThree) {
			bossHealthContainer = new HBox();
			bossHealthContainer.setSpacing(10);
			bossHealthContainer.setLayoutX(50); // Adjust X position
			bossHealthContainer.setLayoutY(100); // Adjust below heart display

			bossHealthLabel = new Label("Boss Health: ");
			bossHealthLabel.setFont(new Font("Arial", 20));
			bossHealthLabel.setTextFill(Color.RED);

			bossHealthBar = new ProgressBar(1.0); // Full health at start
			bossHealthBar.setPrefWidth(300);

			bossHealthContainer.getChildren().addAll(bossHealthLabel, bossHealthBar);
			root.getChildren().add(bossHealthContainer);
		}
	}

	public void updateBossHealth(Boss boss) {
		// Update the boss health bar only if it's LevelThree and the bar exists
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

		// Play sound for firing projectile
		soundEffects.setFile(1); // Index for bullet sound
		soundEffects.play();
	}

	private void fireUlt() {
		// Use the current position of the UserPlane
		double centerX = user.getTranslateX() + user.getLayoutX() + 110; // Adjust for plane's X offset
		double centerY = user.getTranslateY() + user.getLayoutY() + 20;  // Adjust for plane's Y offset

		// Define slight angle offsets for the three projectiles
		double[] angleOffsets = {-5, 0, 5};  // Spread by 5 degrees in each direction

		// Loop to create three projectiles with different angle offsets
		for (double angleOffset : angleOffsets) {
			// Create the projectile using the user's current position and angleOffset
			UserProjectileUlt projectile = new UserProjectileUlt(centerX, centerY, angleOffset);

			// Add the projectile to the root node and track it
			root.getChildren().add(projectile);
			userProjectiles.add(projectile);

			// Play sound for firing projectile
			soundEffects.setFile(2); // Index for bullet sound
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
			soundEffects.setFile(3); // Index for bullet sound
			soundEffects.play();
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
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
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage(0.4,0.5);
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
	protected double getScreenHeight(){
		return screenHeight;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

}