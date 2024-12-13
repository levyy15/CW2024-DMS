package com.example.demo.projectiles;

public class UserProjectileUlt extends Projectile {

    private static final String IMAGE_NAME = "userfire.png";
    private static final int IMAGE_HEIGHT = 45;
    private static final int HORIZONTAL_VELOCITY = 15;
//    private static final int VERTICAL_VELOCITY = 8;
    private final double velocityX;
    private final double velocityY;

    // Constructor that takes angleOffset to adjust the trajectory
    public UserProjectileUlt(double initialXPos, double initialYPos, double angleOffset) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);

        // Calculate the new velocity components based on the angle
        double radians = Math.toRadians(angleOffset);
        this.velocityX = HORIZONTAL_VELOCITY * Math.cos(radians);
        this.velocityY = HORIZONTAL_VELOCITY * Math.sin(radians);
    }

    @Override
    public void updatePosition() {
        // Update the projectile position based on its velocity
        moveHorizontally(velocityX);
        moveVertically(velocityY);
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

}
