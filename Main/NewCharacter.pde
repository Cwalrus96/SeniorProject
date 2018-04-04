//This scene is called when the player wants to create a new character. 
class NewCharacter implements Scene
{
  int maxLength = 20;
  char text[] = new char[maxLength];  
  int charPointer = 0; 
  boolean addedTextField = false; 
   

  void drawScene(long diff)
  {
    clear(); 
    background(255); 
    fill(0); 
    textAlign(CENTER, CENTER); 
    textSize(16); 
    text("Type Your Character's Name Here", Main.screenX * 0.5, Main.screenY * 0.1);
    fill(220); 
    rect(Main.screenX * 0.1, Main.screenY * 0.2, Main.screenX * 0.8, Main.screenY * 0.1, Main.screenX * 0.05); 
    fill(0); 
    textAlign(LEFT, CENTER); 
    text(new String(text).trim(), Main.screenX * 0.1, Main.screenY * 0.25); 
    
  }

  //These are system functions that every scene must implement 
  void mouseClicked()
  {
    return;
  }

  void mouseDragged()
  {
    return;
  }

  void mousePressed()
  {
    return;
  } 

  void mouseReleased()
  {
    return;
  }

  void keyPressed()
  {
    if((key >= ' ') && (key <= '~'))
    {
      text[charPointer] = key; 
      charPointer++; 
      if(charPointer == maxLength) 
      {
         charPointer = (maxLength - 1);  
      }
    }
    else if(key == CODED)
    {
       if(keyCode == BACKSPACE)
       {
         text[--charPointer] = ' ';
       }
    }
    
  } 

  void keyReleased()
  {
    return;
  }
}
