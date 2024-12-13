package com.example.demo.levels;

import com.example.demo.models.Boss;
import com.example.demo.ui.LevelView;
import com.example.demo.ui.LevelViewLevelThree;
import com.example.demo.ui.ShieldImage;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/backgroundboss.gif";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;
    private LevelViewLevelThree levelView;
    private ShieldImage shieldImage;
    private static final int KIILS_TO_ADVANCED = 1 ;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, KIILS_TO_ADVANCED);
        boss = new Boss();
        shieldImage = new ShieldImage(boss.getLayoutX() - 50, boss.getLayoutY() - 100);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        }
        else if (boss.isDestroyed()) {
            winGame();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
            getRoot().getChildren().add(shieldImage);
            shieldImage.showShield();
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelThree(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

    private void updateShieldPosition(){
        if (shieldImage != null && boss != null){
            double offsetX = -65;
            double offsetY = 50;
            shieldImage.setTranslateX(boss.getTranslateX() + offsetX);
            shieldImage.setTranslateY(boss.getTranslateY() + offsetY);
        }
    }

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
