package com.example.demo.projectiles;

/**
 * Represents a special user projectile in the game that follows a custom trajectory based on an angle offset.
 * The UserProjectileUlt class extends the base {@link Projectile} class and provides behavior for a more
 * dynamic projectile fired by the user's plane. The trajectory of this projectile is determined by an
 * angle offset, allowing it to move in different directions rather than only horizontally.
 *
 * <p>The UserProjectileUlt moves both horizontally and vertically, and its movement is determined by the
 * angle offset provided at the time of its creation.</p>
 *
 * @see Projectile
 */
public class UserProjectileUlt extends Projectile {

    private static final String IMAGE_NAME = "userfire.png";
    private static final int IMAGE_HEIGHT = 45;
    private static final int HORIZONTAL_VELOCITY = 15;
    private final double velocityX;
    private final double velocityY;

    /**
     * Constructs a new UserProjectileUlt with the specified initial position and angle offset.
     * The angle offset is used to calculate the projectile's velocity components.
     *
     * @param initialXPos the initial X position of the projectile
     * @param initialYPos the initial Y position of the projectile
     * @param angleOffset the angle offset (in degrees) that affects the trajectory of the projectile
     */
    public UserProjectileUlt(double initialXPos, double initialYPos, double angleOffset) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);

        double radians = Math.toRadians(angleOffset);
        this.velocityX = HORIZONTAL_VELOCITY * Math.cos(radians);
        this.velocityY = HORIZONTAL_VELOCITY * Math.sin(radians);
    }

    /**
     * Updates the position of the projectile by moving it both horizontally and vertically.
     * The position update is based on the velocity components calculated from the angle offset.
     *
     * <p>This method is called every frame to update the projectile’s position in the game world.</p>
     */
    @Override
    public void updatePosition() {
        moveHorizontally(velocityX);
        moveVertically(velocityY);
    }

    /**
     * Updates the actor by calling the {@link #updatePosition()} method. This method is called
     * during each frame to ensure the projectile's position is updated.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

}
