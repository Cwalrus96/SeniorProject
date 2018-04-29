package scenes;

import java.io.File;

import seniorproject.Main;
import seniorproject.Player;

//This scene is called when the player wants to create a new character. 
class NewCharacter extends Scene {

	NewCharacter(Main main) {
		super(main);
		// TODO Auto-generated constructor stub
	}

	int maxLength = 30;
	char text[] = new char[maxLength];
	int charPointer = 0;
	boolean addedTextField = false;
	int characterButton = main.color(100, 255, 100);
	int characterHover = main.color(55, 200, 55);
	int backButton = main.color(255, 100, 100);
	int backHover = main.color(200, 55, 55);
	int buttonText = 0;
	int grayedText = 100;
	File f = new File("/Data/Characters.txt");

public void drawScene(long diff)
{
  main.clear(); 
  main.background(255); 
  main.fill(0); 
  main.textAlign(Main.CENTER, Main.CENTER); 
  main.textSize(22); 
  main.text("Type Your Character's Name Here", Main.screenX * 0.5f, Main.screenY * 0.1f);
  main.fill(220); 
  main.rect(Main.screenX * 0.1f, Main.screenY * 0.2f, Main.screenX * 0.8f, Main.screenY * 0.1f, Main.screenX * 0.05f); 
  main.fill(0); 
  main.textAlign(Main.LEFT, Main.CENTER); 
  String textString = new String(text).trim(); 
  main.text(textString, Main.screenX * 0.1f, Main.screenY * 0.25f);
  //Two buttons - "Create Character" is green, "Back to Menu" is red 
  //Both buttons change color on hover 
  //"Create Character" button on top, clickable if main.text is non-empty 
  //Create Character button
  //Check if the mouse is over the button
  if (((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f)) && ((main.mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f)) && (main.mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f)))) 
  {
    main.fill(characterHover);
  } else
  {
    main.fill(characterButton);
  }
  main.rect(Main.screenX * 0.1f, Main.screenY * ((1.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
  //Check if character button is clickable 
  if(textString.length() == 0)
  {
    main.fill(grayedText);
  }
  else main.fill(buttonText); 
  main.textAlign(Main.CENTER, Main.CENTER); 
  main.textSize(35); 
  main.text("Create Character", Main.screenX * 0.5f, Main.screenY * 0.5f); 
  //Create Character Button
  //Check if mouse is over the button 
  if (((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f)) && ((main.mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (main.mouseY < Main.screenY - 0.05f))) 
  {
    main.fill(backHover);
  } else
  {
    main.fill(backButton);
  }
  main.rect(Main.screenX * 0.1f, Main.screenY * ((2.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
  main.fill(buttonText);
  main.textAlign(Main.CENTER, Main.CENTER); 
  main.textSize(35); 
  main.text("Back to Menu", Main.screenX * 0.5f, Main.screenY * (0.5f + (1.0f / 3.0f)));
}

	// These are system functions that every scene must implement
	public void mouseClicked() {
		// Check if mouse is over "Create Character"
		if ((new String(text).trim().length() > 0)
				&& ((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f))
				&& ((main.mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f))
						&& (main.mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f)))) {
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
				main.savePlayer();
			}
		}
		// Check if mouse is over the "Back" button
		else if (((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f))
				&& ((main.mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (main.mouseY < Main.screenY - 0.05f))) {
			Main.s = new StartMenu(main);
		}
	}


	public void keyPressed() {
		if ((main.key >= ' ') && (main.key <= '~')) // If the user types in any main.text character, add it to the string
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