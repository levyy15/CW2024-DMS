package com.example.demo.models;

/**
 * The {@code ActiveActorDestructible} class extends {@code ActiveActor} and implements the {@code Destructible} interface.
 * This class represents actors in the game that can take damage and be destroyed, with functionality to shrink
 * upon destruction or during damage states.
 *
 * <p>The class adds destructible behavior to active actors, including handling the destroyed state and configurable
 * shrink factors for width and height when damaged or destroyed.
 *
 * @see ActiveActor
 * @see Destructible
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;
	private double shrinkFactorWidth = 0.3;
	private double shrinkFactorHeight = 0.3;

	/**
	 * Constructs a {@code ActiveActorDestructible} with the specified image, size, and initial position.
	 *
	 * @param imageName    the name of the image file to be used for the actor (relative to IMAGE_LOCATION)
	 * @param imageHeight  the height to which the image will be resized while maintaining its aspect ratio
	 * @param initialXPos  the initial horizontal position of the actor
	 * @param initialYPos  the initial vertical position of the actor
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
        isDestroyed = false;
	}

	/**
	 * Gets the shrink factor for the actor's width.
	 *
	 * @return the shrink factor for the width
	 */
	public double getShrinkFactorWidth() {
		return shrinkFactorWidth;
	}

	/**
	 * Sets the shrink factor for the actor's width.
	 *
	 * @param shrinkFactorWidth the new shrink factor for the width
	 */
	public void setShrinkFactorWidth(double shrinkFactorWidth) {
		this.shrinkFactorWidth = shrinkFactorWidth;
	}

	/**
	 * Gets the shrink factor for the actor's height.
	 *
	 * @return the shrink factor for the height
	 */
	public double getShrinkFactorHeight() {
		return shrinkFactorHeight;
	}

	/**
	 * Sets the shrink factor for the actor's height.
	 *
	 * @param shrinkFactorHeight the new shrink factor for the height
	 */
	public void setShrinkFactorHeight(double shrinkFactorHeight) {
		this.shrinkFactorHeight = shrinkFactorHeight;
	}

	/**
	 * Updates the position of the actor. Concrete subclasses must implement this method to define specific
	 * position updates for the actor.
	 */
	@Override
	public abstract void updatePosition();

	/**
	 * Updates the actor's state or behavior. Concrete subclasses must implement this method to define
	 * specific state updates.
	 */
	public abstract void updateActor();

	/**
	 * Handles damage taken by the actor. Concrete subclasses must implement this method to define
	 * specific damage-handling logic.
	 */
	@Override
	public abstract void takeDamage();

	/**
	 * Destroys the actor by setting its destroyed state to true.
	 */
	@Override
	public void destroy() {
		setDestroyed();
	}

	/**
	 * Sets the destroyed state of the actor to true.
	 */
	protected void setDestroyed() {
		this.isDestroyed = true;
	}

	/**
	 * Checks whether the actor has been destroyed.
	 *
	 * @return {@code true} if the actor is destroyed, {@code false} otherwise
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}
	
}
