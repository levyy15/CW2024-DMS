package com.example.demo.models;

import com.example.demo.projectiles.EnemyProjectile;

/**
 * Represents an enemy plane in the game. This class is a specialized type of fighter plane,
 * designed to move horizontally across the screen and periodically fire projectiles.
 * It inherits from the {@link FighterPlane} class and overrides methods for movement,
 * projectile firing, and update logic.
 *
 * <p>The enemy plane has a fixed velocity, fire rate, and health. It also calculates
 * the position of its projectiles relative to its own position.</p>
 *
 * @see FighterPlane
 * @see EnemyProjectile
 */
public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 70;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = .01;

	/**
	 * Constructs an EnemyPlane object with a specified initial position.
	 *
	 * @param initialXPos the initial X position of the enemy plane
	 * @param initialYPos the initial Y position of the enemy plane
	 */
	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
	}

	/**
	 * Updates the position of the enemy plane by moving it horizontally.
	 * The plane moves at a constant horizontal velocity defined by the class constant.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Fires a projectile from the enemy plane at a rate defined by the fire rate.
	 * The projectile's position is calculated relative to the enemy plane's position.
	 *
	 * @return a new {@link EnemyProjectile} if the random chance condition is met,
	 *         otherwise returns null (no projectile is fired).
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPostion = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPostion);
		}
		return null;
	}

	/**
	 * Updates the enemy plane by calling the {@link #updatePosition()} method.
	 * This method is part of the game loop and is used to update the actor's
	 * position during each frame of the game.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Gets the horizontal velocity of the enemy plane.
	 *
	 * @return the horizontal velocity of the enemy plane
	 */
	public int getHorizontalVelocity(){
		return HORIZONTAL_VELOCITY;
	}

}
