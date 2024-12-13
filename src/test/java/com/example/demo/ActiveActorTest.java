package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.models.ActiveActor;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActiveActorTest {

    private ActiveActor testActor;
    @BeforeAll
    static void setupJavaFxToolkit() {
        Platform.startup(()->{});
    }

    @BeforeEach
    public void setUp() {
        // Create a TestActor with an initial position of (100.0, 200.0)
        testActor = new ActiveActor("userplane.png", 50, 100.0, 200.0) {
            @Override
            public void updatePosition() {

            }
        };
    }

    @Test
    public void testMoveHorizontally() {
        // Initial X position is 100
        double initialXPos = testActor.getTranslateX();

        // Move the actor horizontally by 10 units
        testActor.moveHorizontally(10);

        // Assert that the X position has changed by 10
        assertEquals(initialXPos + 10, testActor.getTranslateX(), 0.01);
    }

    @Test
    public void testMoveVertically() {
        // Initial Y position is 200
        double initialYPos = testActor.getTranslateY();

        // Move the actor vertically by 15 units
        testActor.moveVertically(15);

        // Assert that the Y position has changed by 15
        assertEquals(initialYPos + 15, testActor.getTranslateY(), 0.01);
    }
}