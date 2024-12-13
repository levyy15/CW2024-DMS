package com.example.demo.projectiles;

/**
 * Represents a projectile fired by the boss character in the game. The BossProjectile class extends
 * {@link Projectile} and is specifically designed for the boss character's fireball attack.
 *
 * <p>This class handles the horizontal movement of the boss's fireball and updates its position
 * accordingly. It uses a predefined horizontal velocity and the projectile's Y position is set at
 * the time of creation.</p>
 *
 * @see Projectile
 */
public class BossProjectile extends Projectile {
	
	private static final String IMAGE_NAME = "fireball.gif";
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -15;
	private static final int INITIAL_X_POSITION = 1150;

	/**
	 * Constructs a BossProjectile object with the specified initial Y position. The projectile is
	 * created with a predefined image and horizontal velocity.
	 *
	 * @param initialYPos the initial Y position of the projectile
	 */
	public BossProjectile(double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
	}

	/**
	 * Updates the position of the boss's fireball by moving it horizontally at the defined velocity.
	 * The fireball moves left across the screen at a constant speed.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the actor's state by calling the method to update its position.
	 * This method ensures that the boss's projectile is continuously updated during gameplay.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
	
}
