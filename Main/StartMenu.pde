/** This class is going to be used to draw the start menu at the beginning of the stage. 
  For now, should simply have a button that says "Start Game", which creates and loads a new game scene. 
  Will update to have other buttons which will load other scenes. 
**/
class StartMenu implements Scene
{
  //Scene Variables
  int buttonR = 255; 
  int buttonG = 155; 
  int buttonB = 55; 
  boolean buttonPressed = false; 
 
  //1. These functions are inherited from the Scene interface 
  void drawScene(long diff)
  {
   clear(); 
   background(255,255,255);
   fill(buttonR, buttonG, buttonB); 
   rect(Main.screenX * 0.25, Main.screenY * 0.4, Main.screenX * 0.5, Main.screenY * 0.2, Main.screenY * 0.01); 
   fill(0,0,0);
   textAlign(CENTER); 
   textSize(35); 
   text("Start Game", Main.screenX * 0.5, Main.screenY * 0.5); 
   text("Rune Battle", Main.screenX * 0.5, Main.screenY * 0.2); 
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
          buttonR = 204; 
          buttonG = 102; 
          buttonB = 0; 
          buttonPressed = true; 
       }
  }

    //If the "buttonPressed" flag is 1, change it back to zero and change the color of the button back 
  void mouseReleased()
  {
      if(buttonPressed)
      {
        buttonR = 255; 
        buttonG = 155; 
        buttonB = 55; 
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