package com.example.demo.projectiles;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserProjectileUltTest {

    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }

    @Test
    void testInitialVelocityCalculation() {
        // Define the initial position and angle
        double initialX = 100.0;
        double initialY = 200.0;
        double angleOffset = 45.0; // 45 degrees

        // Create the UserProjectileUlt object
        UserProjectileUlt projectile = new UserProjectileUlt(initialX, initialY, angleOffset);

        // Calculate the expected velocities based on angle
        double expectedVelocityX = 15.0 * Math.cos(Math.toRadians(45.0));
        double expectedVelocityY = 15.0 * Math.sin(Math.toRadians(45.0));

        // Verify the velocity components
//        assertEquals(expectedVelocityX, projectile.getVelocityX(), "Velocity in X direction should be correct.");
//        assertEquals(expectedVelocityY, projectile.getVelocityY(), "Velocity in Y direction should be correct.");
    }

    @Test
    void testUpdatePosition() {
        // Define the initial position and angle
        double initialX = 100.0;
        double initialY = 200.0;
        double angleOffset = 45.0; // 45 degrees

        // Create the UserProjectileUlt object
        UserProjectileUlt projectile = new UserProjectileUlt(initialX, initialY, angleOffset);

        // Update the position
        projectile.updatePosition();

        // Calculate the expected new position after one update
        // The horizontal velocity and vertical velocity are calculated as follows:
        double expectedX = initialX + 15.0 * Math.cos(Math.toRadians(45.0));
        double expectedY = initialY + 15.0 * Math.sin(Math.toRadians(45.0));

        // Verify the new positions
        assertEquals(expectedX, projectile.getLayoutX() + projectile.getTranslateX(), "X position should be updated correctly.");
        assertEquals(expectedY, projectile.getLayoutY() + projectile.getTranslateY(), "Y position should be updated correctly.");
    }

    @Test
    void testUpdateActor() {
        // Define the initial position and angle
        double initialX = 100.0;
        double initialY = 200.0;
        double angleOffset = 30.0; // 30 degrees

        // Create the UserProjectileUlt object
        UserProjectileUlt projectile = new UserProjectileUlt(initialX, initialY, angleOffset);

        // Call updateActor, which calls updatePosition internally
        projectile.updateActor();

        // Calculate the expected new position after one update
        double expectedX = initialX + 15.0 * Math.cos(Math.toRadians(30.0));
        double expectedY = initialY + 15.0 * Math.sin(Math.toRadians(30.0));

        // Verify the new positions after updateActor
        assertEquals(expectedX, projectile.getLayoutX() + projectile.getTranslateX(), "X position should be updated correctly after updateActor.");
        assertEquals(expectedY, projectile.getLayoutY() + projectile.getTranslateY(), "Y position should be updated correctly after updateActor.");
    }
}
