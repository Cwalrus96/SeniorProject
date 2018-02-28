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
  
  //Basic constructor that is going to create a new dummy character and dummy enemy for demo 
  DemoGameScene()
  {
    p = new Player(); 
    e = new Enemy(); 
    r = new Rune[gridSize][gridSize];
    for(int i = 0; i < gridSize; i++) 
    {
       for(int j = 0; j < gridSize; j++)
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
    background(255,255,255); 
    if(nextRune == true) //Generates the next rune if necessary
    {
       generateRune();  
    } 
    checkCollisions(); 
    currentRune.drawRune();
    //println("Current rune x = " + currentRune.x + ", Current rune y = " + currentRune.y); 
    drawRuneGrid();
    //1. Draw enemy and player 
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
    if(label.equals("Fire"))
    {
       currentRune = new FireRune("Fire", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge , (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);  
       println("Created a new Fire Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize); 
    }
    else if(label.equals("Heal"))
    {
        currentRune = new HealRune("Heal", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge, (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);
        println("Created a new Healing Rune. X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize); 
    }
    else if(label.equals("Slash"))
    {
        currentRune = new SlashRune("Slash", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge, (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);
        println("Created a new Slash Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    }
    else
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
      for(int i = gridSize - 1; i >= 0; i--) 
      {
        if(!(r[i][squareX].type.equals("Null")))
        {
           floor = Main.screenY - (runeSize * (i + 1)); 
           break;
        }
      } 
      //left represents the leftmost edge that the rune should not pass
      float leftEdge = gridLeftEdge; 
      for(int i = squareX; i >= 0; i--) 
      {
          if(!(r[squareY][i].type.equals("Null")))
          {
             leftEdge = gridLeftEdge + (runeSize * (i + 1)); 
             break;
          }
      }
      float rightEdge = gridRightEdge; 
      for(int i = squareX; i < gridSize; i++)
      {
         if(!(r[squareY][i].type.equals("Null")))
         {
            rightEdge = gridLeftEdge + (runeSize * i);
            break;
         }
      }
      println("SquareX = " + squareX + ", SquareY = " + squareY + " X = " + currentRune.x + ", Y = " + currentRune.y + ", floor = " + floor + ", leftEdge = " + leftEdge); 
      //Prevents the rune from going any lower than the bottom of the screen. If rune gets that far, add it to the grid and create new rune
        if((currentRune.y + (currentRune.size / 2) + currentRune.speedY) >= floor) 
        {
           r[squareY][squareX] = currentRune; 
           currentRune.x = (Main.screenX * 0.1) + (runeSize / 2) + (runeSize * squareX);
           currentRune.y = (Main.screenY) - (runeSize / 2) - (runeSize * squareY);
           currentRune.speedX = 0; 
           currentRune.speedY = 0;
           currentRune = new NullRune("Null", ((gridRightEdge - gridLeftEdge) / 2.0) + gridLeftEdge , (screenY) - (runeSize / 2.0) - (runeSize * (gridSize - 1)), runeSize);
           nextRune = true;
        }
      //Prevents runes from moving beyond the left edge
      if((currentRune.x - (runeSize / 2.0) + currentRune.speedX) < leftEdge) 
      {
         currentRune.x = leftEdge + (runeSize / 2.0) - currentRune.speedX;
      }
      //prevents rune from moving beyond right edge
      if((currentRune.x + (runeSize / 2.0) + currentRune.speedX) > rightEdge)
      {
         currentRune.x = rightEdge - (runeSize / 2.0) - currentRune.speedX;  
      }
  }
  
  //This function will be called whenever a new rune is added to the grid. Will clear away any connections, and lower the runes to their proper places.
  //Will also remove any runes in the upper areas of the grid and damage the player for going too high. Finally, will clear runes in the spellbox if they are not a proper combination 
  //Every time a set of runes is cleared (except in the upper rows or the spellbox), fill some of the player's action bar
  void clearRunes()
  {
     //First, clear all horizontal segments
       //Loop through all runes, and if three (or more) connect in a row then clear them all
       //Once they are cleared, lower all runes above them to the lowest empty spot 
     
     //Secondly, clear all vertical segments 
     
     //After moving the segments, new combinations may have been created. Go loop until no combinations are left
    
     //Third, if there are still runes in the upper two rows after all combinations have been cleared, remove them and damage the player
  }
  
  //This function will be used to draw all previous runes that have not been cleared yet. 
  void drawRuneGrid() 
  {
      for (Rune[] row: r) 
      {
          for(Rune rune: row) 
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
     if(key == CODED) 
     {
        if(keyCode == LEFT)
        {
           currentRune.speedX = -3;  
        }
        else if(keyCode == RIGHT)
        {
           currentRune.speedX = 3;  
        }
        else if(keyCode == DOWN)
        {
           currentRune.speedY = 3;  
        }
     }
  }
  
  void keyReleased()
  {
      if(key == CODED) 
     {
        if(keyCode == LEFT)
        {
           currentRune.speedX = 0;  
        }
        else if(keyCode == RIGHT)
        {
           currentRune.speedX = 0;  
        }
        else if(keyCode == DOWN)
        {
           currentRune.speedY = 1;  
        }
     }
  }
  
  
  
  
}