package com.example.demo.projectiles;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserProjectileTest {

    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }

    @Test
    void testUpdatePosition() {
        // Create a UserProjectile with initial position
        double initialX = 100.0;
        double initialY = 200.0;
        UserProjectile projectile = new UserProjectile(initialX, initialY);

        // Verify initial position
        assertEquals(initialX, projectile.getLayoutX(), "Initial X position should be correct.");
        assertEquals(initialY, projectile.getLayoutY(), "Initial Y position should be correct.");

        // Update position
        projectile.updatePosition();

        // Verify the X position after update
        // Horizontal velocity is 15, so the X position should have increased by 15
        assertEquals(initialX + 15, projectile.getLayoutX() + projectile.getTranslateX(), "X position should have increased by 15.");
    }

    @Test
    void testUpdateActor() {
        // Create a UserProjectile with initial position
        double initialX = 100.0;
        double initialY = 200.0;
        UserProjectile projectile = new UserProjectile(initialX, initialY);

        // Call updateActor, which internally calls updatePosition
        projectile.updateActor();

        // The X position should have increased by 15 as well
        assertEquals(initialX + 15, projectile.getLayoutX()+ projectile.getTranslateX(), "X position should have increased by 15 after updateActor.");
    }
}