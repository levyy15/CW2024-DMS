package com.example.demo.models;

import com.example.demo.projectiles.BossProjectile;

import java.util.*;

/**
 * The {@code Boss} class represents a boss enemy in the game. It extends the {@code FighterPlane} class
 * and introduces additional features such as move patterns, shield activation, and specialized projectile firing.
 * This class is designed to provide a challenging adversary with unique behavior and mechanics.
 *
 * @see FighterPlane
 */
public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1200.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 30.0;
	private static final double BOSS_FIRE_RATE = .04;
	private static final double BOSS_SHIELD_PROBABILITY = .002;
	private static final int IMAGE_HEIGHT = 100;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 50;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;
	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;

	/**
	 * Constructs a {@code Boss} object with predefined attributes including image, position, and health.
	 */
	public Boss() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();
	}

	/**
	 * Gets the maximum health of the boss.
	 *
	 * @return the maximum health value
	 */
	public int getMaxHealth() {
		return HEALTH;
	}

	/**
	 * Updates the position of the boss based on its move pattern. Ensures that the boss stays within valid bounds.
	 */
	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
	}

	/**
	 * Updates the state of the boss, including its position and shield status.
	 */
	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
	}

	/**
	 * Fires a projectile if the boss is allowed to fire in the current frame.
	 *
	 * @return a {@code BossProjectile} if fired, or {@code null} otherwise
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}

	/**
	 * Handles the boss taking damage. Damage is only applied if the shield is not active.
	 */
	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
		}
	}

	/**
	 * Initializes the move pattern for the boss with randomized sequences of vertical velocities and stationary states.
	 */
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	/**
	 * Updates the shield status of the boss. Activates or deactivates the shield based on probabilities and frame limits.
	 */
	private void updateShield() {
		if (isShielded) framesWithShieldActivated++;
		else if (shieldShouldBeActivated()) activateShield();	
		if (shieldExhausted()) deactivateShield();
	}

	/**
	 * Determines the next move in the boss's move pattern.
	 *
	 * @return the velocity for the next move
	 */
	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	/**
	 * Determines whether the boss fires a projectile in the current frame based on a random probability.
	 *
	 * @return {@code true} if the boss fires, {@code false} otherwise
	 */
	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	/**
	 * Calculates the initial vertical position of a projectile fired by the boss.
	 *
	 * @return the vertical position for the projectile
	 */
	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	/**
	 * Determines whether the shield should be activated based on a random probability.
	 *
	 * @return {@code true} if the shield should be activated, {@code false} otherwise
	 */
	public boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	/**
	 * Checks if the shield duration has been exhausted.
	 *
	 * @return {@code true} if the shield duration is exhausted, {@code false} otherwise
	 */
	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	/**
	 * Activates the shield for the boss and adjusts its shrink factors.
	 */
	public void activateShield() {
		isShielded = true;
		setShrinkFactorHeight(0.4);
		setShrinkFactorWidth(0.7);
	}

	/**
	 * Deactivates the shield and resets related attributes.
	 */
	public void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
		setShrinkFactorHeight(0.1);
		setShrinkFactorWidth(0.5);
	}

	/**
	 * Gets the number of frames for which the shield has been active.
	 *
	 * @return the number of active shield frames
	 */
	public int getFramesWithShieldActivated(){
		return framesWithShieldActivated;
	}

	/**
	 * Checks if the boss's shield is currently active.
	 *
	 * @return {@code true} if the shield is active, {@code false} otherwise
	 */
	public boolean isShielded(){
		return isShielded;
	}

	/**
	 * Sets the shield status of the boss. Deactivates the shield if set to {@code false}.
	 *
	 * @param shielded {@code true} to activate the shield, {@code false} to deactivate it
	 */
	public void setShielded(boolean shielded){
		isShielded = shielded;
		if (!isShielded) deactivateShield();
	}

}
