package com.example.demo.models;

/**
 * Abstract class representing a fighter plane in the game. The FighterPlane class extends
 * {@link ActiveActorDestructible} and provides functionality for health management, projectile
 * firing, and determining the positions for firing projectiles.
 *
 * <p>This class serves as a blueprint for all fighter planes, including both player and enemy
 * planes, with common behavior such as health tracking, taking damage, and calculating projectile
 * positions. Specific fighter plane types should implement the {@link #fireProjectile()} method
 * to define their projectile behavior.</p>
 *
 * @see ActiveActorDestructible
 */
public abstract class FighterPlane extends ActiveActorDestructible {

	private int health;

	/**
	 * Constructs a FighterPlane object with the specified image, position, and health.
	 *
	 * @param imageName the name of the image used to represent the fighter plane
	 * @param imageHeight the height of the image representing the fighter plane
	 * @param initialXPos the initial X position of the fighter plane
	 * @param initialYPos the initial Y position of the fighter plane
	 * @param health the initial health of the fighter plane
	 */
	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	/**
	 * Abstract method for firing a projectile from the fighter plane. Specific implementations
	 * of this method should define the behavior for firing projectiles (e.g., direction, speed, etc.).
	 *
	 * @return an {@link ActiveActorDestructible} representing the fired projectile, or null if no projectile is fired
	 */
	public abstract ActiveActorDestructible fireProjectile();

	/**
	 * Decreases the health of the fighter plane by one. If the health reaches zero,
	 * the fighter plane is destroyed.
	 */
	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
	}

	/**
	 * Calculates the X position for the projectile relative to the fighter plane's current position.
	 *
	 * @param xPositionOffset the horizontal offset for the projectile position
	 * @return the calculated X position for the projectile
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Calculates the Y position for the projectile relative to the fighter plane's current position.
	 *
	 * @param yPositionOffset the vertical offset for the projectile position
	 * @return the calculated Y position for the projectile
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the health of the fighter plane has reached zero.
	 *
	 * @return true if the health is zero, false otherwise
	 */
	private boolean healthAtZero() {
		return health == 0;
	}

	/**
	 * Retrieves the current health of the fighter plane.
	 *
	 * @return the current health of the fighter plane
	 */
	public int getHealth() {
		return health;
	}
		
}
