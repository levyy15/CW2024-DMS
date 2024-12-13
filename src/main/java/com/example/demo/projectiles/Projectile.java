package com.example.demo.projectiles;

import com.example.demo.models.ActiveActorDestructible;

/**
 * Abstract class representing a projectile in the game. The Projectile class is responsible for
 * managing the basic properties and behavior of all types of projectiles, including their position
 * and the action taken when they take damage.
 *
 * <p>This class serves as a base class for different projectile types, such as user or enemy projectiles.
 * It provides methods to update the projectile's position, take damage, and retrieve its position.</p>
 *
 * @see ActiveActorDestructible
 * @see UserProjectile
 * @see EnemyProjectile
 */
public abstract class Projectile extends ActiveActorDestructible {

	protected double xPosition;
	protected double yPosition;

	/**
	 * Constructs a new Projectile object with the specified image name, image height, and initial
	 * positions (X and Y).
	 *
	 * @param imageName the name of the image associated with the projectile
	 * @param imageHeight the height of the projectile's image
	 * @param initialXPos the initial X position of the projectile
	 * @param initialYPos the initial Y position of the projectile
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.xPosition = initialXPos;
		this.yPosition = initialYPos;
	}

	/**
	 * Takes damage by calling the destroy method. This method is invoked when the projectile collides
	 * with another object or takes damage, causing it to be removed from the game.
	 */
	@Override
	public void takeDamage() {
		this.destroy();
	}

	/**
	 * Gets the X position of the projectile.
	 *
	 * @return the X position of the projectile
	 */
	public double getXPosition() {
		return xPosition;
	}


	/**
	 * Updates the position of the projectile. This method is abstract and must be implemented by
	 * subclasses, such as {@link UserProjectile} or {@link EnemyProjectile}, to define the specific
	 * movement behavior for each type of projectile.
	 */
	@Override
	public abstract void updatePosition();


}
