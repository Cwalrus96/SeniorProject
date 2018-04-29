package scenes;

import java.util.ArrayList;

import seniorproject.Main;
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
		for (int i = 0; i < characters.length; i++)
		{
			buttons.add(new GameButton(Main.screenX * 0.1f, Main.screenY * 0.1f + (Main.screenX * 0.3f * i),
					Main.screenX * 0.8f, Main.screenX * 0.2f, true, characters[i], 200, 200, 0, 0, 20, 
					Main.screenX * 0.05f, main  )); 
		}
	}

	public void drawScene(long diff) {
		main.clear();
		main.background(255);
		for (GameButton b : buttons)
		{
			b.drawButton();
		}
	}
}