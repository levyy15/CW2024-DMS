package com.example.demo.projectiles;


/**
 * Represents a projectile fired by the user's plane in the game. The UserProjectile class extends
 * the base {@link Projectile} class and provides specific behavior for the user’s projectiles,
 * including the image, speed, and the method for updating its position.
 *
 * <p>When fired, the projectile moves horizontally across the screen with a constant velocity. The
 * UserProjectile class is responsible for updating the position of the projectile and handling its
 * movement during gameplay.</p>
 *
 * @see Projectile
 */
public class UserProjectile extends Projectile {

	private static final String IMAGE_NAME = "userfire.png";
	private static final int IMAGE_HEIGHT = 45;
	private static final int HORIZONTAL_VELOCITY = 15;


	/**
	 * Constructs a new UserProjectile with the specified initial positions.
	 *
	 * @param initialXPos the initial X position of the projectile
	 * @param initialYPos the initial Y position of the projectile
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the projectile by moving it horizontally. The projectile moves
	 * with a constant velocity defined by {@link UserProjectile#HORIZONTAL_VELOCITY}.
	 *
	 * <p>This method is invoked every frame to update the projectile's position in the game world.</p>
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the actor by calling the {@link #updatePosition()} method. This method is called
	 * during each frame to ensure the projectile’s position is updated.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
	
}
