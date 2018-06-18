package scenes;

import java.io.File;
import java.util.ArrayList;

import main.Main;
import userInterface.ButtonAction;
import userInterface.GameButton;

/**
 * This class is going to be used to draw the start menu at the beginning of the
 * stage. For now, should simply have a button that says "Start Game", which
 * creates and loads a new game scene. Will update to have other buttons which
 * will load other scenes. TODO: Replace the "Start Game" Button with two
 * buttons. "Load Game" will open the LoadGame scene, and is only clickable if
 * there is at least 1 saved character "Create Character" will allow the player
 * to create a new named character
 **/
public class StartMenu extends Scene {
	// Scene Variables
	GameButton loadGameButton;
	GameButton createCharacterButton;
	boolean buttonPressed = false;
	int button = 200;
	int buttonHover = 155;
	int buttonText = 0;
	int grayedText = 100;
	File f = new File("Data/Characters.txt");

	public StartMenu(Main main) {
		super(main);
		loadGameButton = new GameButton(Main.screenX * 0.1f, Main.screenY * ((1.0f / 3.0f) + 0.05f),
				Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), false, "Load Game", button, buttonHover,
				buttonText, grayedText, 35, Main.screenY * 0.05f, main);
		createCharacterButton = new GameButton(Main.screenX * 0.1f, Main.screenY * ((2.0f / 3.0f) + 0.05f),
				Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), true, "Create \n Character", button,
				buttonHover, buttonText, grayedText, 35, Main.screenY * 0.05f, main);
		loadGameButton.action = new ButtonAction() {
			public void clickAction(Main main) {
				Main.s = new LoadGame(main);
			}
		};
		createCharacterButton.action = new ButtonAction() {
			public void clickAction(Main main) {
				Main.s = new NewCharacter(main);
			}
		};

	}

	// 1. These functions are inherited from the Scene interface
	public void drawScene(long diff) {
		main.clear();
		main.background(255, 255, 255);
		// Write the title
		main.text("Rune Battle", Main.screenX * 0.5f, Main.screenY * (1.0f / 6.0f));
		// Two buttons - "Load Game" and "Create Character"
		// Both buttons change color on hover
		// Load game is on top, greyed out unless there is at least one character file
		// Load Game button
		// Check if the mouse is over the button
		loadGameButton.checkHover(main.mouseX, main.mouseY);
		// Check if the Load Game button is clickable
		loadGameButton.clickable = f.exists();
		loadGameButton.drawButton();
		// Check if mouse is over the button
		createCharacterButton.checkHover(main.mouseX, main.mouseY);
		createCharacterButton.drawButton();
	}

	// 2. These functions will be called by the main function if the appropriate
	// event occurs
	// This function will get called from the main function if there is a mouse
	// click. Checks if the user pressed a button, and if so changes to the
	// appropriate scene
	public void mouseClicked() {
		// Check if mouse is over "Load Game Button". If so, and button is clickable, go
		// to "Load Game" scene
		if (loadGameButton.isClicked(main.mouseX, main.mouseY)) {
			return;
		}
		// Check if mouse is over the "Create Character" button
		else if (createCharacterButton.isClicked(main.mouseX, main.mouseY)) {
			return;
		}
	}

}
