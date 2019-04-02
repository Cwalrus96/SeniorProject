package scenes;

import processing.core.PImage;
import seniorproject.CharacterClass;
import seniorproject.Main;
import userInterface.ButtonAction;
import userInterface.GameButton;

public class CustomizeCharacterScene extends Scene {
	PImage knightSprite;
	GameButton chooseKnight; 
	PImage wizardSprite;
	GameButton chooseWizard; 

	CustomizeCharacterScene(Main main) {
		super(main);
		knightSprite = main.loadImage("Resources/KnightIdle.png"); 
		wizardSprite = main.loadImage("Resources/WizardIdle.png"); 
		chooseKnight = new GameButton(Main.screenX * 0.1f,  Main.screenY * 0.25f, Main.screenX * 0.8f, Main.screenY  * 0.2f,
				true, "Knight", main.color(255, 200, 200), main.color(255, 155, 155), 0, 100, 35, 
				Main.screenY * 0.05f,  main); 
		chooseWizard = new GameButton(Main.screenX * 0.1f,  Main.screenY * 0.75f, Main.screenX * 0.8f, Main.screenY  * 0.2f,
				true, "Wizard", main.color(200, 200, 255), main.color(155, 155, 255), 0, 100, 35, 
				Main.screenY * 0.05f,  main); 
		chooseKnight.action = new ButtonAction() {
			public void clickAction(Main main) {
				Main.p.characterClass = CharacterClass.KNIGHT;
				main.savePlayer();
				Main.s = new MapScene(main);
			}
		};
		chooseWizard.action = new ButtonAction() {
			public void clickAction(Main main) {
				Main.p.characterClass = CharacterClass.WIZARD;
				main.savePlayer();
				Main.s = new MapScene(main);
			}
		};
	}

	@Override
	public void drawScene(long diff) {
		main.clear(); 
		main.background(255);
		main.image(knightSprite.get(0, 0, 32, 32), (Main.screenX * 0.5f) - 48.0f, (Main.screenY * 0.15f) - 48.0f, 96, 96); 
		main.image(wizardSprite.get(0, 0, 32, 32), (Main.screenX * 0.5f) - 48.0f, (Main.screenY * 0.65f) - 48.0f, 96, 96);
		chooseKnight.checkHover(main.mouseX, main.mouseY);
		chooseKnight.drawButton();
		chooseWizard.checkHover(main.mouseX, main.mouseY);
		chooseWizard.drawButton();
	}
	
	//Reads the users mouse location to determine their selection
	public void mouseClicked() {
		if (chooseKnight.isClicked(main.mouseX, main.mouseY)) {
			return;
		}
		// Check if mouse is over the "Create Character" button
		else if (chooseWizard.isClicked(main.mouseX, main.mouseY)) {
			return;
		}
	}

}
