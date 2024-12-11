package com.example.demo;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BossProjectileTest {

    private BossProjectile bossProjectile;
    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }

    @BeforeEach
    void setUp() {
        // Initialize BossProjectile with a specific Y position
        bossProjectile = new BossProjectile(200);  // Example initial Y position
    }

    @Test
    void testInitialPosition() {
        // Verify the initial horizontal position is as expected
        assertEquals(1150, bossProjectile.getXPosition(), "Initial X position should be 1150.");
    }

    @Test
    void testUpdatePosition() {
        // Test that the horizontal velocity correctly updates the X position
        bossProjectile.updatePosition();
        assertEquals(1150 - 15, bossProjectile.getLayoutX() + bossProjectile.getTranslateX(), "X position should decrease by 15 after update.");
    }

    @Test
    void testUpdateActor() {
        // Test that updateActor calls updatePosition
        bossProjectile.updateActor();
        assertEquals(1150 - 15, bossProjectile.getLayoutX() + bossProjectile.getTranslateX(), "X position should decrease by 15 after updateActor.");
    }
}
