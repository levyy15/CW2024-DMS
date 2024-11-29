package com.example.demo;

public abstract class Projectile extends ActiveActorDestructible {

	protected double xPosition;
	protected double yPosition;

	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.xPosition = initialXPos;
		this.yPosition = initialYPos;
	}

	@Override
	public void takeDamage() {
		this.destroy();
	}

	// Getter for X position
	public double getXPosition() {
		return xPosition;
	}

	// Setter for X position
	public void setXPosition(double xPosition) {
		this.xPosition = xPosition;
	}

	// Getter for Y position
	public double getYPosition() {
		return yPosition;
	}

	// Setter for Y position
	public void setYPosition(double yPosition) {
		this.yPosition = yPosition;
	}


	@Override
	public abstract void updatePosition();


}
