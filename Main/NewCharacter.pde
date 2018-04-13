//This scene is called when the player wants to create a new character. 
class NewCharacter implements Scene
{
  int maxLength = 30;
  char text[] = new char[maxLength];  
  int charPointer = 0; 
  boolean addedTextField = false; 
  color characterButton = color(100, 255, 100);
  color characterHover = color(55, 200, 55); 
  color backButton = color(255, 100, 100); 
  color backHover = color(200, 55, 55); 
  color buttonText = 0; 
  color grayedText = 100;
  File f = new File("/Data/Characters.txt");
  

  void drawScene(long diff)
  {
    clear(); 
    background(255); 
    fill(0); 
    textAlign(CENTER, CENTER); 
    textSize(22); 
    text("Type Your Character's Name Here", Main.screenX * 0.5, Main.screenY * 0.1);
    fill(220); 
    rect(Main.screenX * 0.1, Main.screenY * 0.2, Main.screenX * 0.8, Main.screenY * 0.1, Main.screenX * 0.05); 
    fill(0); 
    textAlign(LEFT, CENTER); 
    String textString = new String(text).trim(); 
    text(textString, Main.screenX * 0.1, Main.screenY * 0.25);
    //Two buttons - "Create Character" is green, "Back to Menu" is red 
    //Both buttons change color on hover 
    //"Create Character" button on top, clickable if text is non-empty 
    //Create Character button
    //Check if the mouse is over the button
    if (((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((1.0 / 3.0) + 0.05)) && (mouseY < Main.screenY * ((2.0 / 3.0) - 0.05)))) 
    {
      fill(characterHover);
    } else
    {
      fill(characterButton);
    }
    rect(Main.screenX * 0.1, Main.screenY * ((1.0 / 3.0) + 0.05), Main.screenX * 0.8, Main.screenY * ((1.0 / 3.0) - 0.1), Main.screenY * 0.05); 
    //Check if character button is clickable 
    if(textString.length() == 0)
    {
      fill(grayedText);
    }
    else fill(buttonText); 
    textAlign(CENTER, CENTER); 
    textSize(35); 
    text("Create Character", Main.screenX * 0.5, Main.screenY * 0.5); 
    //Create Character Button
    //Check if mouse is over the button 
    if (((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((2.0 / 3.0) + 0.05)) && (mouseY < Main.screenY - 0.05))) 
    {
      fill(backHover);
    } else
    {
      fill(backButton);
    }
    rect(Main.screenX * 0.1, Main.screenY * ((2.0 / 3.0) + 0.05), Main.screenX * 0.8, Main.screenY * ((1.0 / 3.0) - 0.1), Main.screenY * 0.05); 
    fill(buttonText);
    textAlign(CENTER, CENTER); 
    textSize(35); 
    text("Back to Menu", Main.screenX * 0.5, Main.screenY * (0.5 + (1.0 / 3.0)));
  }

  //These are system functions that every scene must implement 
  void mouseClicked()
  {
     //Check if mouse is over "Create Character"
     if ((new String(text).trim().length() > 0) && ((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((1.0 / 3.0) + 0.05)) && (mouseY < Main.screenY * ((2.0 / 3.0) - 0.05))))
     {
       //Check if a file with that character name already exists, and if so print a message asking to change the name
       if(f.isFile() && !(match(join(loadStrings(f), ' '), new String(text)) == null))
       {
         println("Cannot create character with that name"); 
       }
       //if character does not already exist, create new default character and save it to a new file with the given name. 
       //Also, add name to characters list file
       else 
       {
         Main.p = new Player(new String(text).trim()); 
         savePlayer(); 
       }
     }
     //Check if mouse is over the "Back" button
     else if (((mouseX > Main.screenX * 0.1) && (mouseX < Main.screenX * 0.9)) && ((mouseY > Main.screenY * ((2.0 / 3.0) + 0.05)) && (mouseY < Main.screenY - 0.05)))
     {
       Main.s = new StartMenu(); 
     }
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
    if ((key >= ' ') && (key <= '~')) //If the user types in any text character, add it to the string
    {
      text[charPointer] = key; 
      charPointer++; 
      if (charPointer == maxLength) 
      {
        charPointer = (maxLength - 1);
      }
    }
    if (key == BACKSPACE)// if the user presses backspace, delete the previous entry 
    {
      if(charPointer != 0) charPointer --; 
      text[charPointer] = ' '; 
    } else if (key == CODED)
    {
    }
  } 

  void keyReleased()
  {
    return;
  }
}
