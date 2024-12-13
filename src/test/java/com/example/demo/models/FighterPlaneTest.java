package com.example.demo.models;

import com.example.demo.models.ActiveActorDestructible;
import com.example.demo.models.FighterPlane;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FighterPlaneTest {

    private FighterPlane fighterPlane;
    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }

    @BeforeEach
    void setUp() {
        // Create a mocked subclass of FighterPlane for testing
        fighterPlane = new FighterPlane("userplane.png", 70, 100.0, 200.0, 3) {
            @Override
            public ActiveActorDestructible fireProjectile() {
                // Return a mock projectile for testing
                return new ActiveActorDestructible("userfire.png", 10, getLayoutX(), getLayoutY()) {
                    private boolean isDestroyed = false;
                    @Override
                    public void updatePosition() {
                        setTranslateX(getTranslateX() + 5);
                        setTranslateY(getTranslateY() + 5);
                    }

                    @Override
                    public void updateActor() {
                        updatePosition();
                    }
                    @Override
                    public void takeDamage() {
                        isDestroyed = true; // Mark projectile as destroyed for testing
                    }

                    public boolean isDestroyed() {
                        return isDestroyed;}

                    };
            }

            @Override
            public void updatePosition() {
                setTranslateX(getTranslateX() + 1);
                setTranslateY(getTranslateY() + 1);
            }

            @Override
            public void updateActor() {
                updatePosition();
            }
        };
    }

    @Test
    void testTakeDamage() {
        assertEquals(3, fighterPlane.getHealth(), "Initial health should be 3.");

        fighterPlane.takeDamage();
        assertEquals(2, fighterPlane.getHealth(), "Health should decrement after taking damage.");

        fighterPlane.takeDamage();
        fighterPlane.takeDamage();
        assertTrue(fighterPlane.isDestroyed(), "Plane should be destroyed when health reaches 0.");
    }

    @Test
    void testProjectilePosition() {
        double xOffset = 20.0;
        double yOffset = 15.0;

        double expectedXPosition = fighterPlane.getLayoutX() + fighterPlane.getTranslateX() + xOffset;
        double expectedYPosition = fighterPlane.getLayoutY() + fighterPlane.getTranslateY() + yOffset;

        assertEquals(expectedXPosition, fighterPlane.getProjectileXPosition(xOffset),
                "Projectile X position should be correctly offset.");
        assertEquals(expectedYPosition, fighterPlane.getProjectileYPosition(yOffset),
                "Projectile Y position should be correctly offset.");
    }

    @Test
    void testFireProjectile() {
        ActiveActorDestructible projectile = fighterPlane.fireProjectile();
        assertNotNull(projectile, "fireProjectile should return a projectile instance.");
        assertEquals(fighterPlane.getLayoutX(), projectile.getLayoutX(),
                "Projectile's initial X position should match plane's layout X.");
        assertEquals(fighterPlane.getLayoutY(), projectile.getLayoutY(),
                "Projectile's initial Y position should match plane's layout Y.");

        projectile.updateActor();
        assertEquals(5, projectile.getTranslateX(), "Projectile X translation should be updated correctly.");
        assertEquals(5, projectile.getTranslateY(), "Projectile Y translation should be updated correctly.");
    }
}
