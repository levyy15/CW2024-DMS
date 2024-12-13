package com.example.demo.models;

import javafx.scene.image.*;

import java.util.Objects;

/**
 * The {@code ActiveActor} class serves as a base class for all game actors that are visually represented
 * and actively participate in the game by changing their positions. It extends {@code ImageView} and provides
 * utility methods for movement and image setup.
 *
 * <p>This class initializes the visual properties of actors, such as their image, size, and initial positions,
 * and provides abstract and concrete methods to support their motion in the game.
 *
 * @see javafx.scene.image.ImageView
 */
public abstract class ActiveActor extends ImageView {
	
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	/**
	 * Constructs an {@code ActiveActor} with the specified image, size, and initial position.
	 *
	 * @param imageName    the name of the image file to be used for the actor (relative to IMAGE_LOCATION)
	 * @param imageHeight  the height to which the image will be resized while maintaining its aspect ratio
	 * @param initialXPos  the initial horizontal position of the actor
	 * @param initialYPos  the initial vertical position of the actor
	 *
	 * @throws NullPointerException if the specified image resource cannot be found
	 */
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		//this.setImage(new Image(IMAGE_LOCATION + imageName));
		this.setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_LOCATION + imageName)).toExternalForm()));
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}

	/**
	 * Updates the position of the actor. Concrete subclasses must provide specific implementations
	 * for position updates to define the actor's behavior.
	 */
	public abstract void updatePosition();

	/**
	 * Moves the actor horizontally by the specified amount.
	 *
	 * @param horizontalMove the amount to move the actor horizontally (positive for right, negative for left)
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	/**
	 * Moves the actor vertically by the specified amount.
	 *
	 * @param verticalMove the amount to move the actor vertically (positive for down, negative for up)
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}

}
