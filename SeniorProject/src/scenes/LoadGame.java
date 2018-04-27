package scenes;

import seniorproject.Main;

//This scene will be called when the player presses the "Load Game" button. Will list all playable characters and load the selected character. 
/* TODO 
Implement clickable buttons 
Write the character's name 
Implement loading characters from JSON file 
*/

class LoadGame extends Scene {
	String characters[];

	LoadGame(Main main) {
		super(main);
		characters = main.loadStrings("Data/Characters.txt");
	}

	public void drawScene(long diff) {
		main.clear();
		main.background(255);
		for (int i = 0; i < characters.length; i++) {
			main.fill(200);
			main.rect(Main.screenX * 0.1f + (Main.screenX * 0.3f * i), Main.screenY * 0.1f, Main.screenX * 0.8f,
					Main.screenX * 0.2f, Main.screenX * 0.05f);
			main.fill(0);
			main.textAlign(Main.CENTER, Main.CENTER);
			main.text(characters[i], Main.screenX * 0.5f, Main.screenY * (0.5f + (1.0f / 3.0f)));
		}
	}
}