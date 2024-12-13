package com.example.demo.levels;

import com.example.demo.models.Boss;
import com.example.demo.ui.LevelView;
import com.example.demo.ui.LevelViewLevelThree;
import com.example.demo.ui.ShieldImage;

/**
 * The LevelThree class represents the third level of the game.
 * It introduces a boss enemy with a shield mechanic and challenges the player to defeat the boss.
 * The level is complete when the boss is destroyed, and the player loses if their health reaches zero.
 */
public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/backgroundboss.gif";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;
    private LevelViewLevelThree levelView;
    private final ShieldImage shieldImage;
    private static final int KIILS_TO_ADVANCED = 1 ;

    /**
     * Constructs a LevelThree instance with the specified screen dimensions.
     *
     * @param screenHeight the height of the screen
     * @param screenWidth  the width of the screen
     */
    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, KIILS_TO_ADVANCED);
        boss = new Boss();
        shieldImage = new ShieldImage(boss.getLayoutX() - 50, boss.getLayoutY() - 100);
    }

    /**
     * Initializes friendly units in the level, specifically adding the player to the game root.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks whether the game is over. The game ends if the player is destroyed or the boss is defeated.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        }
        else if (boss.isDestroyed()) {
            winGame();
        }
    }

    /**
     * Spawns the boss enemy and its shield if no enemies are currently on screen.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
            getRoot().getChildren().add(shieldImage);
            shieldImage.showShield();
        }
    }

    /**
     * Instantiates and returns the LevelView specific to Level Three.
     *
     * @return a LevelViewLevelThree instance configured for this level
     */
    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelThree(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

    /**
     * Updates the position of the shield image relative to the boss.
     */
    private void updateShieldPosition(){
        if (shieldImage != null && boss != null){
            double offsetX = -65;
            double offsetY = 50;
            shieldImage.setTranslateX(boss.getTranslateX() + offsetX);
            shieldImage.setTranslateY(boss.getTranslateY() + offsetY);
        }
    }

    /**
     * Updates the game scene, including shield mechanics and boss behavior.
     */
    @Override
    protected void updateScene() {
        super.updateScene();
        updateShieldPosition();
        if (boss.getFramesWithShieldActivated() == 0) {
            getRoot().getChildren().remove(shieldImage);
            shieldImage.hideShield();
            boss.deactivateShield();
        }

        if (!boss.isShielded() && boss.shieldShouldBeActivated() && boss.getFramesWithShieldActivated() == 0) {
            boss.activateShield();
            getRoot().getChildren().add(shieldImage);
            shieldImage.showShield();
        }

    }

}
