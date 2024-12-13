package com.example.demo.models;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;
	private double shrinkFactorWidth = 0.3;
	private double shrinkFactorHeight = 0.3;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
        isDestroyed = false;
	}

	public double getShrinkFactorWidth() {
		return shrinkFactorWidth;
	}

	public void setShrinkFactorWidth(double shrinkFactorWidth) {
		this.shrinkFactorWidth = shrinkFactorWidth;
	}

	public double getShrinkFactorHeight() {
		return shrinkFactorHeight;
	}

	public void setShrinkFactorHeight(double shrinkFactorHeight) {
		this.shrinkFactorHeight = shrinkFactorHeight;
	}

	@Override
	public abstract void updatePosition();

	public abstract void updateActor();

	@Override
	public abstract void takeDamage();

	@Override
	public void destroy() {
		setDestroyed();
	}

	protected void setDestroyed() {
		this.isDestroyed = true;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}
	
}
