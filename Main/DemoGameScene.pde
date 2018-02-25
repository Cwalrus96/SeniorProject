//This scene represents the actual gameplay of the game. For now, will make simplest possible demo version. 

class DemoGameScene implements Scene
{
  
  //1. Class Variables
  Player p;
  ArrayList<Enemy> e; 
  Rune[][] r; 
  int gridSize = 7;
  boolean nextRune = true; 
  float runeSize =((float) (Main.screenX)) * (0.8 * (1.0 / (float) gridSize));
  Rune currentRune;
  int rand; 
  int squareX; 
  int squareY; 
  
  //Basic constructor that is going to create a new dummy character and dummy enemy for demo 
  DemoGameScene()
  {
    p = new Player(); 
    e = new ArrayList<Enemy>(); 
    r = new Rune[gridSize][gridSize];
    for(int i = 0; i < gridSize; i++) 
    {
       for(int j = 0; j < gridSize; j++)
       {
          r[i][j] = new NullRune("Null", (Main.screenX * 0.1) + (runeSize / 2) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * i), runeSize); 
       }
    }
  }

  //Draw the playing area, player + enemy health-bars, and all of the runes to the screen
  void drawScene(long diff)
  {
    clear(); 
    background(255,255,255); 
    if(nextRune == true) 
    {
       generateRune();  
    } 
    checkCollisions(); 
    currentRune.drawRune();
    println("Current rune x = " + currentRune.x + ", Current rune y = " + currentRune.y); 
    drawRuneGrid(); 
  }
  
  //There should always be a rune falling from the top of the screen. If there is not, the game should create one
  void generateRune()
  {
    rand = (int) random(p.unlockedRunes.size());
    String label = p.unlockedRunes.get(rand); 
    if(label.equals("Fire"))
    {
       currentRune = new FireRune("Fire", Main.screenX * 0.5, (screenY) - (runeSize / 2) - (runeSize * gridSize), runeSize);  
       println("Created a new Fire Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize); 
    }
    else if(label.equals("Heal"))
    {
        currentRune = new HealRune("Heal", Main.screenX * 0.5, (screenY) - (runeSize / 2) - (runeSize * gridSize), runeSize);
        println("Created a new Healing Rune. X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize); 
    }
    else if(label.equals("Slash"))
    {
        currentRune = new SlashRune("Slash", Main.screenX * 0.5, (screenY) - (runeSize / 2) - (runeSize * gridSize), runeSize);
        println("Created a new Slash Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    }
    else
    {
        currentRune = new FireRune("Fire", Main.screenX * 0.5, (screenY) - (runeSize / 2) - (runeSize * gridSize), runeSize);
        println("Created a new Fire Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    }
    currentRune.speedY = 1; 
    nextRune = false;
    
  }
  
  //This function will be used to track the position of the currentRune in terms of the game grid, and look for collisions
  void checkCollisions()
  {
      squareX = ((int) ((currentRune.x - (Main.screenX * 0.1)) / runeSize));
      squareY = ((int) ((Main.screenY - currentRune.y) / runeSize));
      float floor = (Main.screenY - (((squareY - 1) * runeSize) + (runeSize / 2)));
      println("SquareX = " + squareX + ", SquareY = " + squareY + " X = " + currentRune.x + ", Y = " + currentRune.y + ", floor = " + floor); 
      if(squareY == 0)
      {
        if((currentRune.y + currentRune.size / 2) >= Main.screenY)
        {
           r[squareY][squareX] = currentRune; 
           currentRune.x = (Main.screenX * 0.1) + (runeSize / 2) + (runeSize * squareX);
           currentRune.y = (Main.screenY) - (runeSize / 2) - (runeSize * squareY);
           currentRune.speedX = 0; 
           currentRune.speedY = 0;
           nextRune = true;
        }
      }
      else if((!(r[squareY - 1][squareX].type.equals("Null"))) && (currentRune.y >= (Main.screenY - ((squareY + 1) * runeSize) - (runeSize / 2))))
      {
          r[squareY][squareX] = currentRune; 
           currentRune.x = (Main.screenX * 0.1) + (runeSize / 2) + (runeSize * squareX);
           currentRune.y = (Main.screenY) - (runeSize / 2) - (runeSize * squareY);
           currentRune.speedX = 0; 
           currentRune.speedY = 0;
           nextRune = true;
      }
      
      
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
           currentRune.speedX = -1;  
        }
        else if(keyCode == RIGHT)
        {
           currentRune.speedX = 1;  
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