package com.example.demo;

import com.example.demo.models.ActiveActorDestructible;
import com.example.demo.models.UserPlane;
import com.example.demo.projectiles.UserProjectile;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPlaneTest {

    private UserPlane userPlane;
    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }


    @BeforeEach
    void setUp() {
        userPlane = new UserPlane(3); // Initial health set to 3
    }

    @Test
    void testInitialState() {
        assertEquals(3, userPlane.getHealth(), "Initial health should be 3.");
        assertEquals(0, userPlane.getNumberOfKills(), "Initial kill count should be 0.");
        assertEquals(5.0, userPlane.getLayoutX(), "Initial X position should match.");
        assertEquals(300.0, userPlane.getLayoutY(), "Initial Y position should match.");
    }

    @Test
    void testMovement() {
        userPlane.moveUp();
        userPlane.updatePosition();
        assertTrue(userPlane.getTranslateY() < 0, "Plane should move up.");

        userPlane.moveDown();
        userPlane.updatePosition(); // Reset the plane back to the starting position
        userPlane.updatePosition(); // Move plane downwards from starting position
        assertTrue(userPlane.getTranslateY() > 0, "Plane should move down.");

        userPlane.stop();
        double currentTranslateY = userPlane.getTranslateY();
        userPlane.updatePosition();
        assertEquals(currentTranslateY, userPlane.getTranslateY(), "Plane should stop moving.");
    }

    @Test
    void testBoundaryCheck() {
        userPlane.moveUp();
        userPlane.setLayoutY(-900); // Simulate moving beyond upper bound
        userPlane.updatePosition();
        assertTrue(userPlane.getTranslateY() >= -40, "Plane should stay within upper bound.");

        userPlane.moveDown();
        userPlane.setLayoutY(1000); // Simulate moving beyond lower bound
        userPlane.updatePosition();
        assertTrue(userPlane.getTranslateY() <= 900.0, "Plane should stay within lower bound.");
    }

    @Test
    void testFireProjectile() {
        ActiveActorDestructible projectile = userPlane.fireProjectile();
        assertNotNull(projectile, "fireProjectile should return a projectile instance.");
        assertTrue(projectile instanceof UserProjectile, "Projectile should be an instance of UserProjectile.");
        assertEquals(110, projectile.getLayoutX(), "Projectile X position should be correct.");
        assertEquals(userPlane.getProjectileYPosition(20), projectile.getLayoutY(), "Projectile Y position should be correct.");
    }

    @Test
    void testKillCount() {
        assertEquals(0, userPlane.getNumberOfKills(), "Initial kill count should be 0.");

        userPlane.incrementKillCount();
        assertEquals(1, userPlane.getNumberOfKills(), "Kill count should increment correctly.");

        userPlane.incrementKillCount();
        assertEquals(2, userPlane.getNumberOfKills(), "Kill count should increment correctly.");
    }

    @Test
    void testTakeDamage() {
        assertEquals(3, userPlane.getHealth(), "Initial health should be 3.");

        userPlane.takeDamage();
        assertEquals(2, userPlane.getHealth(), "Health should decrement after taking damage.");

        userPlane.takeDamage();
        userPlane.takeDamage();
        assertTrue(userPlane.isDestroyed(), "Plane should be destroyed when health reaches 0.");
    }
}
