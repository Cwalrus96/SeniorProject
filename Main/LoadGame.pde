//This scene will be called when the player presses the "Load Game" button. Will list all playable characters and load the selected character. 
/* TODO 
Implement clickable buttons 
Write the character's name 
Implement loading characters from JSON file 
*/ 

class LoadGame implements Scene
{
  String characters[]; 
  
  LoadGame()
  {
    characters = loadStrings("Data/Characters.txt"); 
  }
  
  void drawScene(long diff)
  {
     for(int i = 0; i < characters.length; i++)
     {
        rect(Main.screenX * 0.1 + (Main.screenX * 0.3 * i), Main.screenY * 0.1, Main.screenX * 0.9, Main.screenX * 0.2); 
     }
  }

  //These are system functions that every scene must implement 
  void mouseClicked()
  {
    
  }

  void mouseDragged()
  {
    
  }

  void mousePressed()
  {
    
  }

  void mouseReleased()
  {
    
  }

  void keyPressed()
  {
    
  }

  void keyReleased()
  {
    
  }
}
