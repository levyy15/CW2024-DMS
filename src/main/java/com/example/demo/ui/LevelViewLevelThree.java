package com.example.demo.ui;

import javafx.scene.Group;

public class LevelViewLevelThree extends LevelView {

    private static final int SHIELD_X_POSITION = 1150;
    private static final int SHIELD_Y_POSITION = 500;
    private final Group root;
    private final ShieldImage shieldImage;

    public LevelViewLevelThree(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);
        this.root = root;
        this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
        addImagesToRoot();
    }

    private void addImagesToRoot() {
        root.getChildren().addAll(shieldImage);
    }


}
