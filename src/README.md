![Title.png](main%2Fresources%2Fcom%2Fexample%2Fdemo%2Fimages%2FTitle.png)
## Description
## INSTALLATION
1. Install IntelliJ IDEA or Eclipse
2. Clone the repository : https://github.com/levyy15/CW2024-DMS.git
3. In IntelliJ IDEA:
    * Open IntelliJ, click File > Open, and select the project folder.
    * Import dependencies if prompted.
4. In Eclipse:
    * Open Eclipse, click File > Import > Existing Projects into Workspace, and select the project folder.
    * Update Maven dependencies if needed (right-click the project > Maven > Update Project).

## RUNNING THE GAME
1. Locate the main class in the src folder. </br>
2. Run the application: </br>
&#8594; In IntelliJ: Right-click the main class and select Run. </br>
&#8594; In Eclipse: Right-click the main class and select Run As > Java Application.

## SYSTEM REQUIREMENTS
- JDK 17 or later.
- IntelliJ IDEA (2022.1 or later) or Eclipse (2023-06 or later).
- Maven pre-installed (if dependencies need manual updating).

## IMPLEMENTED AND WORKING PROPERLY
1. **Health Progression**:
   - Players can progress through levels, whilst health persists between levels using `presistentHealth`.
---
2. **Three Levels**:
   - Implemented a new level: </br>
     &#8594; level two is similar to level one but need 20 kills instead of 10 to advance</br>
     &#8594; Level three is the boss level.</br>
   - Renamed it to `LevelViewLevelThree` as the boss level is in level three instead of two.</br>
---
3. **Pause Display**: 
   - Pause the gameplay when player presses 'P',it blurs and dims the screen .
   - Text display 'PAUSED' appears at the center of the screen. </br>
---
4. **Options**:
   - When pressed 'ESC' or the 'OPTIONS' button it gives the player the option to go to:</br>
   &#8594; MAIN MENU </br>
   &#8594; CONTINUE </br>
   &#8594; QUIT </br>
---
5. **End Options**:
   - Display 'RESTART' and 'QUIT' buttons along with a Win/Game Over image when the game ends. </br>
---
6. **Kill Counter**: 
   - Tracks and displays the number of enemies destroyed and how many kills they need to advance. </br>
---
7. **Fire Ultimate**:
   - When player press 'M' user plane bullets splits into 3.
   - Created `UserProjectileUlt` and called it in `LevelParent` to set the bullet angle offsets. </br>
---
8. **Main Menu**: 
   - Using FXML, Main Menu displays 'START GAME' and 'QUIT'.
   - Allow players time before starting the game.
   - Connected the MainMenu FXML to `MainMenuController` class for functionality.</br>
---
9. **Background Music**:
   - Implemented background music that loops continuously throughout the game until player quits game.</br>
---
10. **Sound Effects**:
    - Distinct Firing sound effects:</br>
      &#8594; Different sound effects when enemy or user fires.</br>
      &#8594; Separate sounds for regular fire projectile and fire projectile ult.</br>
---
11. **Changed game image resources**:
    - Replaced original images with new designs for:</br>
    &#8594; Backgrounds, user plane, enemy plane, boss plane, and win/lose screens.
    - Retained original designs for shield and user/enemy projectiles but updated the boss projectile for a refreshed look.</br>
---
12. **Boss Health**:
    - Tracks the Boss's remaining health 
    - `initializeBossHealthDisplay` renders a visual health bar on the screen to display the Boss's health.</br>
---
13. **IDK**


## Implemented but Not Working Properly
- No known issues or non-functional features. Any failed or incomplete features have been removed or fixed to ensure smooth gameplay.

## Features Not Implemented
1. **Multiplayer Mode**: 
   - Planned but left out due to time constraints. 
   - Tried to implement 'W' and 'S' to be player 1 and '&#8593;' and '&#8595;' player 2.</br></br>
2. **Player customisation**: 
   - It was complicated to allow user to change the `userplane` to different images.
   - Feature intended to offer players the ability to choose different designs.

## New Java Classes
### 1. **LevelThree**
- **Purpose**:  
  &rarr; Introduced a new level to the game, and move the Boss encounter to this stage.</br>
  &rarr; The Boss moves up/down and shoots user in random.</br>
  &rarr; Includes a health bar for the Boss, which must be depleted to complete the game. </br></br>

  &nbsp; - Implement Boss with health 50. </br>
  &nbsp; - Boss has a fire rate of .04. </br>
  &nbsp; - There is a .002 probability that the Boss will activate a shield during combat. </br></br>

- **Key Methods**:</br>
  &emsp; &bull; `spawnEnemyUnits`   
  &emsp; &bull; `updateShield()`   
  &emsp; &bull; `checkIfGameOver()`      
  &emsp; &bull; `updateScene()`   
  &emsp; &bull; `initializeBossHealthDisplay()`  
  &emsp; &bull; `updateBossHealth`</br></br>

- **Location**: `src/main/java/com/example/demo/levels/LevelThree.java`

---

### 2. **LevelViewLevelThree**
- **Purpose**:  
  &rarr; Dedicated to rendering and managing the graphical interface for Level Three.  
  &rarr; Renamed from `LevelViewLevelTwo` to align with the new level structure.</br></br>

- **Key Methods**:  
  &emsp; &bull; `LevelViewLevelThree()`  
  &emsp; &bull; `levelView()`        
  &emsp; &bull; `updateLevelView()`  </br></br>    

- **Location**: `src/main/java/com/example/demo/ui/LevelViewLevelThree.java`

---

### 3. **UserProjectileUlt**
- **Purpose**:  
  &rarr; Introduced a new type of user projectile with customizable trajectory, determined by an angle offset.  
  &rarr; Enables more strategic gameplay with advanced attack options. </br></br>

  &nbsp; - Fires `userfire.png` that split into three, a powerful attack to cover more targets.</br></br>

- **Key Methods**:  
  &emsp; &bull; `updatePosition()`   
  &emsp; &bull; `UserProjectileUlt()`  
  &emsp; &bull; `fireUlt()`  
  &emsp; &bull; `updateActor()` </br></br>


- **Location**: `src/main/java/com/example/demo/projectiles/UserProjectileUlt.java`

---

### 4. **Sound**
- **Purpose**:  
  &rarr; Centralized management of all `.wav` sound files for the game.  
  &rarr; Handles playback, looping, and stopping of sound effects and music. </br></br>

  &nbsp; - `backgroundMusic()` is played in `Main`.
  &nbsp; - In both fire, regular and Ult, `soundEffects()` is played each time its called. </br></br>

- **Key Methods**:  
  &emsp; &bull; `soundURL[]`     
  &emsp; &bull; `setFile`    
  &emsp; &bull; `play()`   
  &emsp; &bull; `loop()`  
  &emsp; &bull; `stop()`   </br></br>

- **Location**: `src/main/java/com/example/demo/Sound.java`

---

### 5. **MainMenuController**
- **Purpose**:  
  &rarr; Manages interactions with the main menu buttons, 'START GAME', 'QUIT'.  
  &rarr; Ensures smooth navigation and responsive user interactions. </br></br>

 Incorporates a unique background image. </br>
  &nbsp; - Background music initialise. </br>
  &nbsp; - 'START GAME' launches the game. </br>
  &nbsp; - 'QUIT' exits the game window. </br></br>

- **Key Methods**:</br>
  &emsp; &bull; `setStage()`  
  &emsp; &bull; `initialize()`     
  &emsp; &bull; `handleStartGame()`    
  &emsp; &bull; `handleQuit()` </br></br>

- **Location**: `src/main/java/com/example/demo/controller/MainMenuController.java`

---

### 6. **MainMenu.fxml** (Not a Java class but is required.)
- Using SceneBuilder, The file formats the main menu screen in the game. </br></br> 
- **Location**: `src/main/resources/com/example/demo/images/menu/MainMenu.fxml`
---
### 7. **MainMenu.css**
- Formats the background image for main menu. </br></br>
- **Location**: `src/main/resources/com/example/demo/images/menu/MainMenu.css`


## Modified Java Classes
1. **LevelParent**: </br>
   &#8594; **Timeline bug**: </br>
   &nbsp; - Fixed the bug to advance to teh next level </br> </br>
    - **Updated method**:
        - `timeline.stop();`: Stoped level one so player can advance to the next level.  </br></br>

   &#8594; **Pause Functionality**: </br>
   &nbsp; - Press **'P'** to pause the game, dimming the game background. </br></br>
   - **New variables** :
        - `pausedLabel()`: Displays the text 'PAUSED' when game is paused. 
        - `isPaused (boolen)`: Tracks whether the game is currently paused.  </br></br>
     
   - **New methods** :
        - `togglePause()`:Toggles the game's pause state, showing the paused label and stopping game mechanics. Pressing 'P' again resumes the game and hides the paused label.
        - `initializePausedLabelDisplay()`: Sets up the appearance, font and position of the paused label on the screen. </br></br>

   &#8594; **Options button**: </br>
   &nbsp; - Press **'ESC'**, to display functional buttons for 'Main Menu,' 'Continue,' and 'Quit.'  </br> </br>
    - **New variables** :
        - `optionsBox(VBox)` The container for the options menu buttons, 'Main Menu', 'Continue', and 'Quit'.</br> </br>

    - **New methods** :
        - `initializeOptionsButton()`: Can be refactored to handle the layout, styling, and initialization of the `optionsBox`.
        - `options()`: Displays the menu, stops the game timeline, and prepares for user input. </br> </br>

   &#8594; **End Options Buttons**: </br>
   &nbsp; - Appears when the user wins or loses, allowing them to retry or quit the game. </br> </br>
   - **New variables**:
       - `optionsBox (VBox)`: Contains buttons 'RETRY' and 'QUIT'.</br> </br>

   - **New methods**:
        - `showEndOptions()`: Sets up the buttons and layout for buttons actions.</br> </br>

   &#8594; **Fire Ultimate** : </br>
   &nbsp; - Press **'M'** to shoot three projectiles in a wide spread, accompanied by a firing sound effect.  </br></br>
    - **New methods**:
        - `fireUlt()`: Fires three projectiles in a spread pattern with accompanying sound effects.

   &#8594; **Move UP/DOWN** : </br>
   &nbsp; - Made **'W'** and **'S'** also move up and down. </br> </br>
   - **Updated methods**:
       - if (kc == KeyCode.S) `user.moveDown()`;
       - if (kc == KeyCode.W) `user.moveUp()`; </br> </br>

   &#8594; **Kill Counter**: </br>
   &nbsp; - Tracks destroyed enemies  and progress toward advancing levels.</br> </br>
   - **New variables**:
       - `killCount (int)`: Counts the number of enemies destroyed.
       - `killsToAdvance (int)`: Determines the threshold for advancing to the next level.
       - `initializeKillCounter`: 
       -  `updateKillCount`: Updates the counter for ech enemies destroyed

   - **New methods**:
       - `initializeKillCounter()`: Updates the kill counter when an enemy is destroyed.

   &#8594; **Boss health display**: </br>
   &nbsp; - Shows a bar indicating the Boss's remaining health. </br> </br>
   - **New variables**:
       - `bossHealthContainer(HBox)`:  Ensures the health bar is positioned correctly. </br>
       - `bossHealthBar()`: Visually displays the current health of the boss. </br></br>

   - **New methods**:
       - `updateBossHealthBar()`: Updates the health bar based on the boss's current health.
       - `initializeBossHealthDisplay()`: Sets up the appearance and position of the health bar.</br></br>
   
   &#8594; **Health**: </br>
   &nbsp; - Retains player health when advancing to the next level. </br></br>
    - **New method**:
        - `user.getHealth()`: Retrieves the current health of the player from `UserPlane`. </br> </br>

   &#8594; **Fonts**: </br>
   &nbsp; - Updated all button text to pixel fonts. </br> </br>
   - **New variables**:
       - `pixelFont (Font)`: Loads and applies a pixel font to all text elements.

   - **New methods**:
       - `setFont()`: Applies the pixel font to text elements. </br> </br>

   &#8594; **Sound Effects**: </br>
   &nbsp; - Added sound effects when the player, enemies, or the boss shoot projectiles.</br> </br>
   - **New methods**:
       - `soundEffects.play()`: Plays the sound effect when uer fires.

---
2. **Main**: </br>
   &#8594; **Background Music**: Added looping background music the entire game.</br>
   &#8594; **Main Menu FXML**: Load Main Menu using FXML to `START GAME` or `QUIT`. </br> </br>
---
3. **Controller**: Updated to handle health persistence and level transition. 
---
4. **UserPlane**: 
5. 


## Unexpected Problems
1. **Time**: Background image not updating properly during level change.
   - **Solution**: Refactored `Controller` to include a level initialization method.
2. **Random Crash on Enemy Spawn**: Occurred due to null pointers in enemy array.
   - **Solution**: Added null checks before spawning enemies.  