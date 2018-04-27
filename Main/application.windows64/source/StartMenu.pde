/** This class is going to be used to draw the start menu at the beginning of the stage. 
 For now, should simply have a button that says "Start Game", which creates and loads a new game scene. 
 Will update to have other buttons which will load other scenes. 
 TODO: Replace the "Start Game" Button with two buttons. 
 "Load Game" will open the LoadGame scene, and is only clickable if there is at least 1 saved character 
 "Create Character" will allow the player to create a new named character
 **/
class StartMenu implements Scene
{
  //Scene Variables
  boolean buttonPressed = false; 
  color button = 200; 
  color buttonHover = 155; 
  color buttonText = 0; 
  color grayedText = 100; 
  File f = new File("/Data/Characters.txt"); 
  boolean canLoad; 

  //1. These functions are inherited from the Scene interface 
  void drawScene(long diff)
  {
    clear(); 
    background(255, 255, 255);
    //Write the title
    text("Rune Battle", Main.screenX * 0.5, Main.screenY * (1.0 / 6.0)); 
    //Two buttons - "Load Game" and "Create Character" 
    //Both buttons change color on hover 
    //Load game is on top, greyed out unless there is at least one character file 
    //Load Game button
    //Check if the mouse is over the button
    if (((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((1.0 / 3.0) + 0.05)) && (mouseY < Main.screenY * ((2.0 / 3.0) - 0.05)))) 
    {
      fill(buttonHover);
    } else
    {
      fill(button);
    }
    rect(Main.screenX * 0.1, Main.screenY * ((1.0 / 3.0) + 0.05), Main.screenX * 0.8, Main.screenY * ((1.0 / 3.0) - 0.1), Main.screenY * 0.05); 
    //Check if the Load Game button is clickable 
    canLoad = f.isFile(); 
    if (canLoad)
    {
      fill(buttonText);
    } else
    {
      fill(grayedText);
    }
    textAlign(CENTER, CENTER); 
    textSize(35); 
    text("Load Game", Main.screenX * 0.5, Main.screenY * 0.5); 
    //Create Character Button
    //Check if mouse is over the button 
    if (((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((2.0 / 3.0) + 0.05)) && (mouseY < Main.screenY - 0.05))) 
    {
      fill(buttonHover);
    } else
    {
      fill(button);
    }
    rect(Main.screenX * 0.1, Main.screenY * ((2.0 / 3.0) + 0.05), Main.screenX * 0.8, Main.screenY * ((1.0 / 3.0) - 0.1), Main.screenY * 0.05); 
    fill(buttonText);
    textAlign(CENTER, CENTER); 
    textSize(35); 
    text("Create \n Character", Main.screenX * 0.5, Main.screenY * (0.5 + (1.0 / 3.0)));
  }

  //2. These functions will be called by the main function if the appropriate event occurs
  //This function will get called from the main function if there is a mouse click. Checks if the user pressed a button, and if so changes to the appropriate scene
  void mouseClicked()
  {
     //Check if mouse is over "Load Game Button". If so, and button is clickable, go to "Load Game" scene
     if (canLoad && ((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((1.0 / 3.0) + 0.05)) && (mouseY < Main.screenY * ((2.0 / 3.0) - 0.05))))
     {
       Main.s = new LoadGame(); 
     }
     //Check if mouse is over the "Create Character" button
     else if (((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((2.0 / 3.0) + 0.05)) && (mouseY < Main.screenY - 0.05)))
     {
       Main.s = new NewCharacter(); 
     }
  }

  //For this particular scene, this function does nothing 
  void mouseDragged()
  {
    return;
  }

  //If the mouse is pressed within the area of the button, change the color of the button to show it is pressed and change the "buttonPressed" flag to 1
  void mousePressed()
  {
    return; 
  }

  //If the "buttonPressed" flag is 1, change it back to zero and change the color of the button back 
  void mouseReleased()
  {
    return; 
  }

  void keyPressed()
  {
    return;
  }

  void keyReleased()
  {
    return;
  }
}
