package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;

/**
 * The Controller class manages the game's main stage and transitions between levels.
 * It utilizes reflection to dynamically instantiate level classes and manages their lifecycle.
 * This class also implements the Observer interface to respond to level updates.
 */

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne";

	private final Stage stage;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Launches the game by showing the stage and navigating to the first level.
	 *
	 * @throws ClassNotFoundException       if the level class cannot be found
	 * @throws NoSuchMethodException        if the constructor with the required parameters is not found
	 * @throws SecurityException            if there is a security violation
	 * @throws InstantiationException       if the class cannot be instantiated
	 * @throws IllegalAccessException       if the constructor is not accessible
	 * @throws IllegalArgumentException     if invalid arguments are passed to the constructor
	 * @throws InvocationTargetException    if the constructor invocation throws an exception
	 */

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {

			stage.show();
			goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	/**
	 * Navigates to a specified level by dynamically loading and initializing it.
	 *
	 * @param className the fully qualified class name of the level to navigate to
	 * @throws ClassNotFoundException       if the level class cannot be found
	 * @throws NoSuchMethodException        if the constructor with the required parameters is not found
	 * @throws SecurityException            if there is a security violation
	 * @throws InstantiationException       if the class cannot be instantiated
	 * @throws IllegalAccessException       if the constructor is not accessible
	 * @throws IllegalArgumentException     if invalid arguments are passed to the constructor
	 * @throws InvocationTargetException    if the constructor invocation throws an exception
	 */

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Class<?> myClass = Class.forName(className);
			Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
			LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
			myLevel.addObserver(this);
			Scene scene = myLevel.initializeScene();
			stage.setScene(scene);
			myLevel.startGame();

	}

	/**
	 * Handles updates from the observed level and transitions to a new level if required.
	 *
	 * @param arg0 the observable object that triggered the update
	 * @param arg1        an argument passed by the observable, expected to be the class name of the next level
	 */

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			goToLevel((String) arg1);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		}
	}

}
