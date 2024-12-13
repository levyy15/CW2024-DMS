package com.example.demo.models;

import com.example.demo.models.EnemyPlane;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnemyPlaneTest {

    private EnemyPlane enemyPlane;

    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        enemyPlane = new EnemyPlane(500.0, 300.0);
    }

    @Test
    void testInitialHealth() {
        assertEquals(1, enemyPlane.getHealth(), "Enemy plane initial health should be 1.");
    }

    @Test
    void testUpdatePosition() {
        double initialXPosition = enemyPlane.getTranslateX();
        enemyPlane.updatePosition();
        double updatedXPosition = enemyPlane.getTranslateX();

        assertEquals(initialXPosition + enemyPlane.getHorizontalVelocity(), updatedXPosition,
                "Enemy plane should move horizontally by the defined velocity.");
    }

    @Test
    void testFireProjectile() {
        boolean fired = false;
        for (int i = 0; i < 1000; i++) {
            if (enemyPlane.fireProjectile() != null) {
                fired = true;
                break;
            }
        }
        assertTrue(fired, "Enemy plane should fire projectiles occasionally based on probability.");
    }

    @Test
    void testUpdateActor() {
        double initialXPosition = enemyPlane.getTranslateX();
        enemyPlane.updateActor();
        double updatedXPosition = enemyPlane.getTranslateX();

        assertEquals(initialXPosition + enemyPlane.getHorizontalVelocity(),updatedXPosition,
                "updateActor should update the enemy plane's position correctly.");
    }
}
