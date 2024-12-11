package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnemyProjectileTest {

    private EnemyProjectile enemyProjectile;
    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }

    @BeforeEach
    public void setUp() {
        // Initialize the EnemyProjectile with some initial position (e.g., 100.0, 200.0)
        enemyProjectile = new EnemyProjectile(100.0, 200.0);
    }

    @Test
    public void testUpdatePosition() {
        // Initial X position is 100
        double initialXPos = enemyProjectile.getLayoutX();

        // Call updatePosition() which should move the projectile horizontally by -10
        enemyProjectile.updatePosition();

        // Assert that the X position has changed by -10
        assertEquals(initialXPos - 10, enemyProjectile.getLayoutX() + enemyProjectile.getTranslateX(), 0.01);
    }

    @Test
    public void testUpdateActor() {
        // Initial X position is 100
        double initialXPos = enemyProjectile.getLayoutX();

        // Call updateActor() which should update the position as well
        enemyProjectile.updateActor();

        // Assert that the X position has changed by -10
        assertEquals(initialXPos - 10, enemyProjectile.getLayoutX() + enemyProjectile.getTranslateX(), 0.01);
    }
}