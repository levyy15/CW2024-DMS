package com.example.demo.projectiles;

/**
 * Represents a projectile fired by an enemy character in the game. The EnemyProjectile class
 * extends {@link Projectile} and is used to simulate an enemy's fire attack.
 *
 * <p>This class handles the horizontal movement of the enemy's fireball, which moves across the
 * screen at a specified velocity. The position of the projectile is set when it is created, and
 * it continues to move horizontally until it leaves the screen or is destroyed.</p>
 *
 * @see Projectile
 */
public class EnemyProjectile extends Projectile {
	
	private static final String IMAGE_NAME = "enemyFire.png";
	private static final int IMAGE_HEIGHT = 50;
	private static final int HORIZONTAL_VELOCITY = -10;

	/**
	 * Constructs an EnemyProjectile object with the specified initial X and Y positions.
	 * The projectile is initialized with an image and a horizontal velocity.
	 *
	 * @param initialXPos the initial X position of the projectile
	 * @param initialYPos the initial Y position of the projectile
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the enemy's fireball by moving it horizontally at the defined velocity.
	 * The fireball moves left across the screen at a constant speed.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the actor's state by calling the method to update its position.
	 * This method ensures that the enemy's projectile is continuously updated during gameplay.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}


}
