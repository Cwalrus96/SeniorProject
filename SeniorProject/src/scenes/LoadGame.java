package scenes;

import java.util.ArrayList;

import seniorproject.Main;
import userInterface.ButtonAction;
import userInterface.GameButton;

//This scene will be called when the player presses the "Load Game" button. Will list all playable characters and load the selected character. 
/* TODO 
Implement clickable buttons 
Write the character's name 
Implement loading characters from JSON file 
*/

class LoadGame extends Scene {
	ArrayList<GameButton> buttons;
	String characters[];

	LoadGame(Main main) {
		super(main);
		buttons = new ArrayList<GameButton>();
		characters = main.loadStrings("Data/Characters.txt");
		for (int i = 0; i < characters.length; i++) {
			String name = characters[i];
			GameButton b = new GameButton(Main.screenX * 0.1f, Main.screenY * 0.1f + (Main.screenX * 0.3f * i),
					Main.screenX * 0.8f, Main.screenX * 0.2f, true, name, 200, 155, 0, 0, 20, Main.screenX * 0.05f,
					main);
			b.action = new ButtonAction() {
				public void clickAction(Main main) {
					main.loadPlayer(name);
				}
			};
			buttons.add(b);
		}
	}

	public void drawScene(long diff) {
		main.clear();
		main.background(255);
		for (GameButton b : buttons) {
			b.checkHover(main.mouseX, main.mouseY);
			b.drawButton();
		}
	}

	/*
	 * This function will go through all the buttons to see which one is clicked, and load the proper character
	 */
	public void mouseClicked() {
		for(GameButton b: buttons)
		{
			if(b.isClicked(main.mouseX, main.mouseY))
			{
				return;
			}
		}
	}

}