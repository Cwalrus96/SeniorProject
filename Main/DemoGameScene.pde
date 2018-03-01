//This scene represents the actual gameplay of the game. For now, will make simplest possible demo version. 

class DemoGameScene implements Scene
{

  //1. Class Variables
  Enemy e; //Keeps track of the enemy that you are currently battling
  Rune[][] r; 
  int gridSize = 9; //Adjusts the size of the grid, and therefore the number of runes
  boolean nextRune = true; //This flag keeps track of when to generate the next rune
  float gridLeftEdge = (Main.screenX * 0.1); //Keeps track of the leftmost edge of the rune grid. Runes should not go beyond this point
  float gridRightEdge = (Main.screenX * 0.9); //Keeps track of the right edge of the rune grid
  float runeSize = ((gridRightEdge - gridLeftEdge) / ((float) gridSize)); //Keeps track of the rune radius 
  Rune currentRune; //Keeps track of the rune that the player currently controls
  int rand; //Used to keep track of random variables
  int squareX;  //Used to track the current rune's X position relative to the rune grid
  int squareY; 
  //The following variables are used to track info about the spellBox
  int boxWidth = 3;  
  int boxHeight = 2;
  int maxNum = 2;
  int start = ((gridSize - boxWidth) / 2);
  int end = start + boxWidth;





  //Basic constructor that is going to create a new dummy character and dummy enemy for demo 
  DemoGameScene()
  {
    p = new Player(); 
    e = new Enemy(); 
    r = new Rune[gridSize][gridSize];
    for (int i = 0; i < gridSize; i++) 
    {
      for (int j = 0; j < gridSize; j++)
      {
        //Fills the grid with null runes, which are nothing but placeholders 
        r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * i), runeSize);
      }
    }
  }





  //Draw the playing area, player + enemy health-bars, and all of the runes to the screen
  void drawScene(long diff) //Diff represents the amount of time that has passed since the previous frame, and is used to control the speed of moving objects to make everything move smoothly
  {
    clear(); 
    background(255, 255, 255); 
    if (nextRune == true) //Generates the next rune if necessary
    {
      generateRune();
    } 
    checkCollisions(); 
    currentRune.drawRune();
    //println("Current rune x = " + currentRune.x + ", Current rune y = " + currentRune.y); 
    drawRuneGrid();
    //1. Draw enemy and player 
    Main.p.drawPlayer();
    //2. Draw enemy + player health bars
    //health bars should change size and color based on health
    //3. Draw energy bar 
    //attacking reduces energy, clearing runes fills it
    //4. Draw spellbox
    //runes that don't combine dissappear, pressing attack clears spellbox + creates effects
  }





  //There should always be a rune falling from the top of the screen. If there is not, the game should create one
  void generateRune()
  {
    rand = (int) random(p.unlockedRunes.size()); //Generates a random rune based on what runes the player currently has unlocked 
    String label = p.unlockedRunes.get(rand); //Check label to know what kind of rune to generate 
    //Whatever the rune type, draws it at the top of the grid, and in the horizontal center 
    if (label.equals("Fire"))
    {
      currentRune = new FireRune("Fire", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge, (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);  
      println("Created a new Fire Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    } else if (label.equals("Heal"))
    {
      currentRune = new HealRune("Heal", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge, (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);
      println("Created a new Healing Rune. X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    } else if (label.equals("Slash"))
    {
      currentRune = new SlashRune("Slash", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge, (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);
      println("Created a new Slash Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    } else
    {
      currentRune = new FireRune("Fire", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge, (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);
      println("Label not recognized =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    }
    currentRune.speedY = 1; 
    nextRune = false;
  }





  //This function will be used to track the position of the currentRune in terms of the game grid, and look for collisions
  void checkCollisions()
  {
    squareX = ((int) ((currentRune.x - gridLeftEdge) / runeSize)); //squareX and squareY represent the current rune's position in the rune grid 
    squareY = ((int) ((Main.screenY - currentRune.y) / runeSize));
    //Floor represents a bottom edge that the rune should not go below
    float floor = Main.screenY;
    for (int i = gridSize - 1; i >= 0; i--) 
    {
      if (!(r[i][squareX].type.equals("Null")))
      {
        floor = Main.screenY - (runeSize * (i + 1)); 
        break;
      }
    } 
    //left represents the leftmost edge that the rune should not pass
    float leftEdge = gridLeftEdge; 
    for (int i = squareX; i >= 0; i--) 
    {
      if (!(r[squareY][i].type.equals("Null")))
      {
        leftEdge = gridLeftEdge + (runeSize * (i + 1)); 
        break;
      }
    }
    float rightEdge = gridRightEdge; 
    for (int i = squareX; i < gridSize; i++)
    {
      if (!(r[squareY][i].type.equals("Null")))
      {
        rightEdge = gridLeftEdge + (runeSize * i);
        break;
      }
    }
    println("SquareX = " + squareX + ", SquareY = " + squareY + " X = " + currentRune.x + ", Y = " + currentRune.y + ", floor = " + floor + ", leftEdge = " + leftEdge); 
    //Prevents the rune from going any lower than the bottom of the screen. If rune gets that far, add it to the grid and create new rune
    if ((currentRune.y + (currentRune.size / 2) + currentRune.speedY) >= floor) 
    {
      r[squareY][squareX] = currentRune; 
      currentRune.x = (Main.screenX * 0.1) + (runeSize / 2) + (runeSize * squareX);
      currentRune.y = (Main.screenY) - (runeSize / 2) - (runeSize * squareY);
      currentRune.speedX = 0; 
      currentRune.speedY = 0;
      currentRune = new NullRune("Null", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge, (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);
      nextRune = true;
      clearRunes(); //new rune added to the grid - clear any duplicates
    }
    //Prevents runes from moving beyond the left edge
    if ((currentRune.x - (runeSize / 2.0) + currentRune.speedX) < leftEdge) 
    {
      currentRune.x = leftEdge + (runeSize / 2.0) - currentRune.speedX;
    }
    //prevents rune from moving beyond right edge
    if ((currentRune.x + (runeSize / 2.0) + currentRune.speedX) > rightEdge)
    {
      currentRune.x = rightEdge - (runeSize / 2.0) - currentRune.speedX;
    }
  }





  //This function will be called whenever a new rune is added to the grid. Will clear away any connections, and lower the runes to their proper places.
  //Will also remove any runes in the upper areas of the grid and damage the player for going too high. Finally, will clear runes in the spellbox if they are not a proper combination 
  //Every time a set of runes is cleared (except in the upper rows or the spellbox), fill some of the player's action bar
  void clearRunes()
  {  
    //Loop until no runes are removed. Removing a rune will trigger the "runes cleared" flag.
    boolean runesCleared = true;
    //first, check for vertical and horizontal chains. removing a chain may create a new one, so loop until no new chains are created
    while (runesCleared)
    {  
      runesCleared = (checkVertical() && checkHorizontal());
    }
    //next, check the upper two rows. Runes here will damage the player
    checkUpperRows();
    //Finally, if there are runes in the spellbox that don't mix, remove them all. For now, just assume that if they aren't the same type, they don't mix. Also, cannot mix more than 2 of same type
    checkSpellbox();
  }




  //This function will check for any vertical chains 
  boolean checkVertical()
  {
    boolean runesCleared = false;
    //Secondly, clear all vertical segments 
    for (int i = 0; i < gridSize - 2; i++) 
    {
      for (int j = 0; j < gridSize; j++) //Don't go all the way to prevent going out of bounds.
      {
        if (!(r[i][j].type.equals("Null")))
        {
          int chain = 1; //Keeps track of how long the chain is 
          int k = i + 1; //K is a new variable to keep track of the position in the chain
          while ((k < gridSize) && (r[k][j].type.equals(r[i][j].type))) //If k is the same type as i, keep the chain going
          {
            chain ++; 
            k++;
          }
          if (chain > 2) //if the chain is at least 3, remove all runes in the chain.
          {
            runesCleared = true;
            for (int l = i; l < k; l++) //replace all the runes with null runes
            { 
              r[l][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * l), runeSize);
            }
            for (int l = k; l < gridSize - 1; l++)
            {
              r[l - chain][j] = r[l][j]; //Drop the runes down to fill in the missing space
              r[l - chain][j].y += runeSize * chain; 
              //replace the missing rune
              r[l][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * l), runeSize);
            }
            Main.p.energy += chain; //Add cleared runes to the player's energy
            if (Main.p.energy > Main.p.maxEnergy)
            {
              Main.p.energy = Main.p.maxEnergy;
            }
          }
        }
      }
    }
    return runesCleared;
  }




  //This function will check for any horizontal chains
  boolean checkHorizontal()
  {
    boolean runesCleared = false;
    //First, clear all horizontal segments
    //Loop through all runes, and if three (or more) connect in a row then clear them all
    for (int i = 0; i < gridSize; i++) 
    {
      for (int j = 0; j < gridSize - 2; j++) //Don't go all the way to prevent going out of bounds.
      {
        if (!(r[i][j].type.equals("Null")))
        {
          int chain = 1; //Keeps track of how long the chain is 
          int k = j + 1; //K is a new variable to keep track of the position in the chain
          while ((k < gridSize) && (r[i][k].type.equals(r[i][j].type))) //If k is the same type as J, keep the chain going
          {
            chain ++; 
            k++;
          }
          if (chain > 2) //if the chain is at least 3, remove all runes in the chain.
          {
            runesCleared = true;
            for (int l = j; l < k; l++) 
            {
              //Clear the runes by lowering everything above them down 1 
              for (int m = i; m < gridSize - 1; m++) //Go up to gridSize - 2 because the upper rows of the grid should remain empty
              {
                r[m][l] = r[m + 1][l]; //Replaces the selected rune with the rune above it
                r[m][l].y += runeSize;
                //Replace the removed rune with a null rune 
                r[m + 1][l] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0) + (runeSize * l), (screenY) - (runeSize / 2) - (runeSize * (m + 1)), runeSize);
              }
            }
            Main.p.energy += chain; //Add cleared runes to the player's energy
            if (Main.p.energy > Main.p.maxEnergy)
            {
              Main.p.energy = Main.p.maxEnergy;
            }
          }
        }
      }
    }
    return runesCleared;
  }



  //This function checks the top two rows of the rune grid for uncleared runes
  void checkUpperRows()
  {
    for (int i = gridSize - 2; i < gridSize; i++)
    {
      for (int j = 0; j < gridSize; j++)
      {
        if (!r[i][j].type.equals("Null"))
        {
          r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * i), runeSize);
          Main.p.health -= 5;
        }
      }
    }
  }




  //This function will check runes in the spellbox, and remove them if they don't mix. 
  //For now, the spellbox will be a 3 * 2 box at the bottom center of the rune grid. Runes don't mix if they are different types, 
  //and grid currently can only hold 2 of the same type
  void checkSpellbox()
  { 
    String type = getSpellType();
    if (!type.equals("Null"))
    {
      int count = getSpellCount(type);
      if ((count == -1) || (count > maxNum))
      {
        clearSpellBox();
      }
    }
  }



  //Returns the number of runes of a particular type in the spellBox
  int getSpellCount(String type)
  {
    int count = 0;
    boolean clear = false;
    for (int i = 0; i < boxHeight; i++)
    {
      for (int j = start; j < end; j++)
      {
        if (r[i][j].type.equals(type))
        {
          count ++;
        } else if (!r[i][j].type.equals("Null"))
        {
          clear = true;
        }
      }
    }
    if (clear)
    {
      return -1;
    } else return count;
  }






  //This function returns the type of the first rune found in the spellBox
  String getSpellType()
  {
    String type = "Null";
    for (int i = 0; i < boxHeight; i++) //First, figure out what kind of runes are in the spellbox
    {
      for (int j = start; j < end; j++)
      {
        if (!(r[i][j].type.equals("Null")))
        {
          type = r[i][j].type;
          break;
        }
      }
    }
    return type;
  }





  //This function is called to clear away all runes in the spellBox;
  void clearSpellBox()
  {
    for (int i = 0; i < boxHeight; i++)
    {
      for (int j = start; j < end; j++)
      {
        r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * i), runeSize);
      }
    }
  }





  //This function will be used to draw all previous runes that have not been cleared yet. 
  void drawRuneGrid() 
  {
    for (Rune[] row : r) 
    {
      for (Rune rune : row) 
      {
        rune.drawRune();
      }
    }
  }

  //This function can be used to check if the user pressed any of the on-screen controls (to be implemented later).  
  void mouseClicked()
  {
    return;
  }

  //This function has two main uses - to see if the user is dragging current gem, and to see if the user dragged up for an attack
  void mouseDragged()
  {
    return;
  }

  //When the player presses a mouse button, the game will check two things. Did the player click on the currently falling rune? Or did they click on the spell-box?
  void mousePressed()
  {
    return;
  }

  /**When the player releases the mouse, check if anything was selected. 
   If a rune was selected, release the rune. 
   If the spell-box was selected and the mouse is currently above the spell-box, perform an attack
   **/
  void mouseReleased()
  {
    return;
  }

  void keyPressed()
  {
    if (key == CODED) 
    {
      if (keyCode == LEFT)
      {
        currentRune.speedX = -3;
      } else if (keyCode == RIGHT)
      {
        currentRune.speedX = 3;
      } else if (keyCode == DOWN)
      {
        currentRune.speedY = 3;
      }
    }
  }

  void keyReleased()
  {
    if (key == CODED) 
    {
      if (keyCode == LEFT)
      {
        currentRune.speedX = 0;
      } else if (keyCode == RIGHT)
      {
        currentRune.speedX = 0;
      } else if (keyCode == DOWN)
      {
        currentRune.speedY = 1;
      }
    }
  }
}