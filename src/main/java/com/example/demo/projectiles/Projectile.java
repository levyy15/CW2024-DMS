package com.example.demo.projectiles;

import com.example.demo.models.ActiveActorDestructible;

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

	public double getXPosition() {
		return xPosition;
	}

	@Override
	public abstract void updatePosition();


}
