package scenes;

import java.io.File;

import seniorproject.Main;


/** This class is going to be used to draw the start menu at the beginning of the stage. 
For now, should simply have a button that says "Start Game", which creates and loads a new game scene. 
Will update to have other buttons which will load other scenes. 
TODO: Replace the "Start Game" Button with two buttons. 
"Load Game" will open the LoadGame scene, and is only clickable if there is at least 1 saved character 
"Create Character" will allow the player to create a new named character
**/
public class StartMenu extends Scene
{
 //Scene Variables
 boolean buttonPressed = false; 
 int button = 200; 
 int buttonHover = 155; 
 int buttonText = 0; 
 int grayedText = 100; 
 File f = new File("/Data/Characters.txt"); 
 boolean canLoad; 
 
 public StartMenu(Main main)
 {
	 super(main);
 }

 //1. These functions are inherited from the Scene interface 
 public void drawScene(long diff)
 {
   main.clear(); 
   main.background(255, 255, 255);
   //Write the title
   main.text("Rune Battle", Main.screenX * 0.5f, Main.screenY * (1.0f / 6.0f)); 
   //Two buttons - "Load Game" and "Create Character" 
   //Both buttons change color on hover 
   //Load game is on top, greyed out unless there is at least one character file 
   //Load Game button
   //Check if the mouse is over the button
   if (((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f)) && ((main.mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f)) && (main.mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f)))) 
   {
     main.fill(buttonHover);
   } else
   {
     main.fill(button);
   }
   main.rect(Main.screenX * 0.1f, Main.screenY * ((1.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
   //Check if the Load Game button is clickable 
   canLoad = f.isFile(); 
   if (canLoad)
   {
     main.fill(buttonText);
   } else
   {
     main.fill(grayedText);
   }
   main.textAlign(Main.CENTER, Main.CENTER); 
   main.textSize(35); 
   main.text("Load Game", Main.screenX * 0.5f, Main.screenY * 0.5f); 
   //Create Character Button
   //Check if mouse is over the button 
   if (((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f)) && ((main.mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (main.mouseY < Main.screenY - 0.05f))) 
   {
     main.fill(buttonHover);
   } else
   {
     main.fill(button);
   }
   main.rect(Main.screenX * 0.1f, Main.screenY * ((2.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
   main.fill(buttonText);
   main.textAlign(Main.CENTER, Main.CENTER); 
   main.textSize(35); 
   main.text("Create \n Character", Main.screenX * 0.5f, Main.screenY * (0.5f + (1.0f / 3.0f)));
 }

 //2. These functions will be called by the main function if the appropriate event occurs
 //This function will get called from the main function if there is a mouse click. Checks if the user pressed a button, and if so changes to the appropriate scene
 public void mouseClicked()
 {
    //Check if mouse is over "Load Game Button". If so, and button is clickable, go to "Load Game" scene
    if (canLoad && ((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f)) && ((main.mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f)) && (main.mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f))))
    {
      Main.s = new LoadGame(main); 
    }
    //Check if mouse is over the "Create Character" button
    else if (((main.mouseX > Main.screenX * 0.1f) && (main.mouseX < Main.screenX * 0.9f)) && ((main.mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (main.mouseY < Main.screenY - 0.05f)))
    {
      Main.s = new NewCharacter(main); 
    }
 }
 
}

 