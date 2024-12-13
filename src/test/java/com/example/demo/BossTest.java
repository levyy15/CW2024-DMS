package com.example.demo;

import com.example.demo.models.Boss;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BossTest {

    private Boss boss;

    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }
    @BeforeEach
    void setUp() {
        boss = new Boss();
    }

    @Test
    void testInitialHealth() {
        assertEquals(50, boss.getHealth(), "Boss initial health should be 50.");
        assertEquals(50, boss.getMaxHealth(), "Boss max health should be 50.");
    }

    @Test
    void testPositionUpdateWithinBounds() {
        double initialTranslateY = boss.getTranslateY();
        boss.updatePosition();
        double newTranslateY = boss.getTranslateY();

        // Assert that position changes but stays within bounds
        assertTrue(newTranslateY >= -100 && newTranslateY <= 475,
                "Boss position should remain within vertical bounds.");
        assertNotEquals(initialTranslateY, newTranslateY, "Boss position should update.");
    }

    @Test
    void testShieldActivation() {
        boss.setShielded(false);
        assertFalse(boss.isShielded(), "Shield should initially be deactivated.");

        boss.activateShield();
        assertTrue(boss.isShielded(), "Shield should activate when triggered.");
        assertEquals(0.4, boss.getShrinkFactorHeight(), "Shield activation should change height factor.");
        assertEquals(0.7, boss.getShrinkFactorWidth(), "Shield activation should change width factor.");
    }

    @Test
    void testShieldDeactivation() {
        boss.activateShield();
        boss.deactivateShield();
        assertFalse(boss.isShielded(), "Shield should deactivate when triggered.");
        assertEquals(0.1, boss.getShrinkFactorHeight(), "Shield deactivation should reset height factor.");
        assertEquals(0.5, boss.getShrinkFactorWidth(), "Shield deactivation should reset width factor.");
    }

    @Test
    void testTakeDamageWithoutShield() {
        int initialHealth = boss.getHealth();
        boss.takeDamage();
        assertEquals(initialHealth - 1, boss.getHealth(), "Boss should take damage when shield is not active.");
    }

    @Test
    void testTakeDamageWithShield() {
        boss.activateShield();
        int initialHealth = boss.getHealth();
        boss.takeDamage();
        assertEquals(initialHealth, boss.getHealth(), "Boss should not take damage when shield is active.");
    }

    @Test
    void testFireProjectile() {
        boolean fired = false;
        for (int i = 0; i < 1000; i++) {
            if (boss.fireProjectile() != null) {
                fired = true;
                break;
            }
        }
        assertTrue(fired, "Boss should fire projectiles occasionally based on probability.");
    }

    @Test
    void testFramesWithShield() {
        boss.activateShield();
        for (int i = 0; i < 500; i++) {
            boss.updateActor();
        }
        assertFalse(boss.isShielded(), "Shield should deactivate after max frames.");
    }
}

