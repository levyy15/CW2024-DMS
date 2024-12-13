package com.example.demo.models;

import com.example.demo.projectiles.UserProjectile;


/**
 * Represents a user-controlled fighter plane in the game. The UserPlane class extends
 * {@link FighterPlane} and provides specific functionality for movement, projectile firing,
 * and health management for the user's plane.
 *
 * <p>This class handles the movement of the user's plane (up and down), the firing of
 * projectiles, and maintains the user's kill count and health. The class also supports
 * persistent health across game sessions, updating health after taking damage, and restricting
 * the plane's vertical movement within certain bounds.</p>
 *
 * @see FighterPlane
 * @see UserProjectile
 */
public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 900.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 80;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private int velocityMultiplier;
	private int numberOfKills;
	private static int persistentHealth;


	/**
	 * Constructs a UserPlane object with the specified initial health. The health is persistent
	 * across game sessions, meaning it will be retained if a player resumes the game after a pause.
	 *
	 * @param initialHealth the initial health of the user's plane
	 */
	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, persistentHealth > 0 ? persistentHealth : initialHealth);
		persistentHealth = getHealth();
		velocityMultiplier = 0;
	}


	/**
	 * Updates the position of the user's plane by moving vertically. The plane will stop
	 * moving if it reaches the defined vertical boundaries (Y_UPPER_BOUND and Y_LOWER_BOUND).
	 */
	@Override
	public void updatePosition() {
		if (isMoving()) {
			double initialTranslateY = getTranslateY();
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
			double newPosition = getLayoutY() + getTranslateY();
			if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}
		}
	}

	/**
	 * Updates the actor's state by calling the method to update its position.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Decreases the health of the user's plane by one. Updates the persistent health
	 * to reflect the current health after taking damage.
	 */
	@Override
	public void takeDamage() {
		super.takeDamage();
		persistentHealth = getHealth(); // Update persistent health
	}

	/**
	 * Resets the persistent health to a new value. This is typically used when the player
	 * starts a new game or resumes the game with a different health value.
	 *
	 * @param health the new health value to set as persistent
	 */
	public static void resetHealth(int health) {
		persistentHealth = health;
	}

	/**
	 * Fires a projectile from the user's plane. The projectile is created at a fixed X position
	 * and the Y position is offset from the plane's current position.
	 *
	 * @return an {@link ActiveActorDestructible} representing the fired projectile
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
	}

	/**
	 * Checks if the plane is currently moving vertically (up or down).
	 *
	 * @return true if the plane is moving, false otherwise
	 */
	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	/**
	 * Moves the plane upwards by setting the vertical velocity multiplier to -1.
	 */
	public void moveUp() {
		velocityMultiplier = -1;
	}

	/**
	 * Moves the plane downwards by setting the vertical velocity multiplier to 1.
	 */
	public void moveDown() {
		velocityMultiplier = 1;
	}

	/**
	 * Stops the plane from moving by setting the vertical velocity multiplier to 0.
	 */
	public void stop() {
		velocityMultiplier = 0;
	}

	/**
	 * Retrieves the number of kills made by the user (i.e., the number of enemy planes destroyed).
	 *
	 * @return the number of kills
	 */
	public int getNumberOfKills() {
		return numberOfKills;
	}


	/**
	 * Increments the kill count by 1 whenever the user destroys an enemy plane.
	 */
	public void incrementKillCount() {
		numberOfKills++;
	}

}
