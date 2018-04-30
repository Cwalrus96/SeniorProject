package scenes;

import java.io.File;

import seniorproject.Main;
import seniorproject.Player;
import userInterface.GameButton;
import userInterface.ButtonAction;

//This scene is called when the player wants to create a new character. 
class NewCharacter extends Scene {
	GameButton backButton;
	GameButton characterButton;
	int maxLength = 30;
	char text[] = new char[maxLength];
	int charPointer = 0;
	boolean addedTextField = false;
	int characterColor = main.color(100, 255, 100);
	int characterHover = main.color(55, 200, 55);
	int backColor = main.color(255, 100, 100);
	int backHover = main.color(200, 55, 55);
	int buttonText = 0;
	int grayedText = 100;
	File f = new File("/Data/Characters.txt");

	NewCharacter(Main main) {
		super(main);
		characterButton = new GameButton(Main.screenX * 0.1f, Main.screenY * ((1.0f / 3.0f) + 0.05f),
				Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), false, "Create Character", characterColor,
				characterHover, buttonText, grayedText, 35, Main.screenX * 0.05f, main);
		backButton = new GameButton(Main.screenX * 0.1f, Main.screenY * ((2.0f / 3.0f) + 0.05f), Main.screenX * 0.8f,
				Main.screenY * ((1.0f / 3.0f) - 0.1f), true, "Back to Menu", backColor, backHover, buttonText,
				grayedText, 35, Main.screenX * 0.05f, main);
		characterButton.action = new ButtonAction() {
			public void clickAction(Main main) {
				// Check if a file with that character name already exists, and if so print a
				// message asking to change the name
				if (f.isFile() && !(Main.match(Main.join(Main.loadStrings(f), ' '), new String(text)) == null)) {
					System.out.println("Cannot create character with that name");
				}
				// if character does not already exist, create new default character and save it
				// to a new file with the given name.
				// Also, add name to characters list file
				else {
					Main.p = new Player(new String(text).trim());
					Main.s = new CustomizeCharacterScene(main);
				}
			}
		};
		backButton.action = new ButtonAction() {
			public void clickAction(Main main) {
				Main.s = new StartMenu(main);
			}
		};
	}

	public void drawScene(long diff) {
		main.clear();
		main.background(255);
		main.fill(0);
		main.textAlign(Main.CENTER, Main.CENTER);
		main.textSize(22);
		main.text("Type Your Character's Name Here", Main.screenX * 0.5f, Main.screenY * 0.1f);
		main.fill(220);
		main.rect(Main.screenX * 0.1f, Main.screenY * 0.2f, Main.screenX * 0.8f, Main.screenY * 0.1f,
				Main.screenX * 0.05f);
		main.fill(0);
		main.textAlign(Main.LEFT, Main.CENTER);
		String textString = new String(text).trim();
		main.text(textString, Main.screenX * 0.1f, Main.screenY * 0.25f);
		// Two buttons - "Create Character" is green, "Back to Menu" is red
		// Both buttons change color on hover
		// "Create Character" button on top, clickable if main.text is non-empty
		// Create Character button
		// Check if the mouse is over the button
		characterButton.checkHover(main.mouseX, main.mouseY);
		characterButton.clickable = !(new String(text).trim().length() == 0);
		characterButton.drawButton();
		// Create back Button
		// Check if mouse is over the button
		backButton.checkHover(main.mouseX, main.mouseY);
		backButton.drawButton();

	}

	// These are system functions that every scene must implement
	public void mouseClicked() {
		if (characterButton.isClicked(main.mouseX, main.mouseY)) {
			return;
		}
		// Check if mouse is over the "Create Character" button
		else if (backButton.isClicked(main.mouseX, main.mouseY)) {
			return;
		}
	}

	public void keyPressed() {
		if ((main.key >= ' ') && (main.key <= '~')) // If the user types in any main.text character, add it to the
													// string
		{
			text[charPointer] = main.key;
			charPointer++;
			if (charPointer == maxLength) {
				charPointer = (maxLength - 1);
			}
		}
		if (main.key == Main.BACKSPACE)// if the user presses backspace, delete the previous entry
		{
			if (charPointer != 0)
				charPointer--;
			text[charPointer] = ' ';
		} else if (main.key == Main.CODED) {
		}
	}
}