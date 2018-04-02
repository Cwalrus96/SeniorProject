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
 
  //1. These functions are inherited from the Scene interface 
  void drawScene(long diff)
  {
   clear(); 
   background(255,255,255);
   //Write the title
   text("Rune Battle", Main.screenX * 0.5, Main.screenY * (1.0 / 6.0)); 
   //Two buttons - "Load Game" and "Create Character" 
   //Both buttons change color on hover 
   //Load game is on top, greyed out unless there is at least one character file 
     //Load Game button
     //Check if the mouse is over the button
   if(((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((1.0 / 3.0) + 0.05)) && (mouseY < Main.screenY * ((2.0 / 3.0) - 0.05)))) 
   {
      fill(100);  
   }
   else
   {
      fill(155);
   }
   
   rect(Main.screenX * 0.1, Main.screenY * ((1.0 / 3.0) + 0.05), Main.screenX * 0.8, Main.screenY * ((1.0 / 3.0) - 0.1), Main.screenY * 0.05); 
   fill(0,0,0);
   textAlign(CENTER, CENTER); 
   textSize(35); 
   text("Load Game", Main.screenX * 0.5, Main.screenY * 0.5); 
     //Create Character Button
   fill(155);
   rect(Main.screenX * 0.1, Main.screenY * ((2.0 / 3.0) + 0.05), Main.screenX * 0.8, Main.screenY * ((1.0 / 3.0) - 0.1), Main.screenY * 0.05); 
   fill(0,0,0);
   textAlign(CENTER, CENTER); 
   textSize(35); 
   text("Create \n Character", Main.screenX * 0.5, Main.screenY * (0.5 + (1.0 / 3.0))); 
   
  }
  
  //2. These functions will be called by the main function if the appropriate event occurs
    //This function will get called from the main function if there is a mouse click. Checks if the user pressed a button, and if so changes to the appropriate scene
  void mouseClicked()
  {
    if(mouseOverButton())
    {
       Main.s = new DemoGameScene();  
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
       if(mouseOverButton())
       {
          buttonPressed = true; 
       }
  }

    //If the "buttonPressed" flag is 1, change it back to zero and change the color of the button back 
  void mouseReleased()
  {
      if(buttonPressed)
      { 
        buttonPressed = false; 
      }
  }
  
  //This function checks if the mouse's position is within the boundaries of the current button
  boolean mouseOverButton()
  {
    if(((mouseX > Main.screenX * 0.25) && (mouseX < Main.screenX * 0.75)) && ((mouseY > Main.screenY * 0.4) && (mouseY < Main.screenY * 0.6)))
    {
       return true; 
    }
    else return false; 
    
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
