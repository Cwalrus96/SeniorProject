import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {

/**TODO: 
Add button class to ease addition of buttons
fine better IDE so I don't have to use tabs? 
Allow user to click on the "load game" button, and use the character file to populate the character 
create 2 different character classes (at least) - Knight and Mage? 
Track user levels and experience points
Create 4 different levels, and a map to move back and forth between. 3 normal enemies and 1 boss 
Add animations for enemies and attacks 
Adjust animations for clearing runes + add combos? 
Turn runes into sprites 
Add backgrounds + spruce up user interface 
replace buttons with sprites? 
**/ 



//1. Global Variables 

static Scene s;  //Keeps track of the current screen 
static float screenX = 375;   //Tracks the width of the screen
static float screenY = 667; //Tracks the height of the screen 
static boolean debug = false;   //Flag used to view debug features such as mouse position
static int targetFPS = 60;   //The desired frame-per-second rate 
long oldT; //This variable, along with newT, will be used to keep track of the amount of time between frames 
long newT; 
long diff; //This will hold the difference from oldT to newT
int frameLength; //This variable holds the desired amount of time between frames rendered 
static Player p; //Holds information about the current player. For now, simply create a blank player. In the future, player information will be loaded from a save file 


//3. This function will be called once to set up the scene 
public void setup() 
{
  /**a. Set the size. This size was chosen because it is the same aspect ratio as an Iphone screen, but at a slightly lower resolution. 
   The box appears on my screen as about the same size as an actual Iphone screen, which will allow me to test if the game works well on that sized screen. 
   TODO: Implement function to adjust display based on size and resolution of the screen, while maintaining aspect ratio 
   **/
   
  //b. Initialize Global Variables 
  //i. The first scene should always be the start menu
  s = new StartMenu(); 
  p = new Player();
  //These variables will be used to track time and keep the game running at the target frames per second
  oldT = millis(); 
  newT = oldT; 
  frameLength = 1000 / targetFPS;
}

public void draw() //This function draws everything to the screen. For the most part, will simply draw the current scene. Has potential for some debugging information 
{
  newT = millis(); 
  diff = newT - oldT; //Only draws the scene if enough time has passed 
  if (diff > frameLength)
  {
    s.drawScene(diff); 
    if (debug) //If the debug flag is set, draws additional information to the screen 
    {
      textAlign(LEFT); 
      textSize(screenY * 0.03f); 
      text("MouseX = " + mouseX, 0, screenY * 0.05f);
      text("MouseY = " + mouseY, 0, screenY * 0.1f);
    }
  }
}

public void savePlayer() //This function is used to save the player to a file
{
  if (Main.p != null)
  {
    File listFile = new File("Data/Characters.txt"); 
    File characterFile = new File("Data/" + p.name + ".json");
    JSONObject savePlayer = new JSONObject(); 
    savePlayer.setString("name", p.name); 
    savePlayer.setFloat("maxHealth", p.maxHealth); 
    savePlayer.setFloat("health", p.health); 
    savePlayer.setFloat("maxEnergy", p.maxEnergy); 
    savePlayer.setFloat("energy", p.energy);
    savePlayer.setFloat("x", p.x); 
    savePlayer.setFloat("y", p.y); 
    JSONArray unlockedRunes = new JSONArray(); 
    int i = 0; 
    for(String r : p.unlockedRunes)
    {
        unlockedRunes.setString(i, r); 
        i++; 
    }
    savePlayer.setJSONArray("unlockedRunes", unlockedRunes); 
    if (!listFile.isFile()) //if listFile doesn't exist create it, add the character's name, and create a file for the character 
    {
      println("list file doesn't exist"); 
      PrintWriter writeList = createWriter(listFile); 
      writeList.println(p.name); 
      writeList.flush(); 
      writeList.close(); 
      PrintWriter writeCharacter = createWriter(characterFile);
      //saveJSONObject(savePlayer, "Data/" + p.name + ".json"); 
      writeCharacter.print(savePlayer); 
      writeCharacter.flush();
      writeCharacter.close(); 
    }
    else 
    {
      println(listFile.getAbsolutePath()); 
      PrintWriter writeCharacter = createWriter(characterFile);
      //saveJSONObject(savePlayer, "Data/" + p.name + ".json"); 
      writeCharacter.print(savePlayer); 
      writeCharacter.flush();
      writeCharacter.close();
      print(savePlayer); 
    }
  }
}

//For all input event functions, simply call the corresponding function in the current scene

public void mouseClicked() 
{
  s.mouseClicked();
}

public void mousePressed()
{
  s.mousePressed();
}

public void mouseReleased()
{
  s.mouseReleased();
}

public void mouseDragged()
{
  s.mouseDragged();
}

public void keyPressed()
{
  s.keyPressed();
}

public void keyReleased()
{
  s.keyReleased();
}
abstract class Animation
{
    long oldT; 
    long newT; 
    public abstract boolean animate(long t);
    
}
//This scene represents the actual gameplay of the game. For now, will make simplest possible demo version. 
/**TODO 
Implement mouse controls
Implement on-screen buttons? 
Make "GameState" enum, and convert all state checks to use the enum
Figure out a different way to handle attacks besides hardcoding them. Should use some combination of the player/enemy's stats 
Replace RuneAnimation with SpriteAnimations? Also, 
**/
class DemoGameScene implements Scene
{

  //1. Class Variables
  Enemy e; //Keeps track of the enemy that you are currently battling
  Rune[][] r; 
  int gridSize = 9; //Adjusts the size of the grid, and therefore the number of runes
  boolean nextRune = true; //This flag keeps track of when to generate the next rune
  float gridLeftEdge = (Main.screenX * 0.1f); //Keeps track of the leftmost edge of the rune grid. Runes should not go beyond this point
  float gridRightEdge = (Main.screenX * 0.9f); //Keeps track of the right edge of the rune grid
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
  String status; //Keeps track of the current state of the program. Possible states are - ON, PAUSED, WIN, and LOSE
  ArrayList<Animation> animations;  //This will keep track of all the animations currently going on the screen





  //Basic constructor that is going to create a new dummy character and dummy enemy for demo 
  DemoGameScene()
  {
    p = new Player(); 
    e = new Enemy(); 
    r = new Rune[gridSize][gridSize];
    animations = new ArrayList<Animation>(); 
    animations.add(new SpriteAnimation(32 * 9, 32,  Main.p.x, Main.p.y, 9, "../Animations/LittleSpriteGuy.png")); 
    for (int i = 0; i < gridSize; i++) 
    {
      for (int j = 0; j < gridSize; j++)
      {
        //Fills the grid with null runes, which are nothing but placeholders 
        r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * i), runeSize);
      }
    }
    status = "ON";
  }





  //Draw the playing area, player + enemy health-bars, and all of the runes to the screen
  public void drawScene(long diff) //Diff represents the amount of time that has passed since the previous frame, and is used to control the speed of moving objects to make everything move smoothly
  {
    if (status.equals("ON"))
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
      //Main.p.drawPlayer();
      e.drawEnemy();
      //2. Draw enemy + player health bars
      //health bars should change size and color based on health
      drawHealthBars(); 
      if (e.health <= 0)
      {
        status = "WIN";
      }
      if (Main.p.health <= 0)
      {
        status = "LOSE";
      }
      animateAll(diff);
      //runes that don't combine dissappear, pressing attack clears spellbox + creates effects
    } else if (status.equals("PAUSED"))
    {
      fill(0, 0, 0, 100);
      rect(0, 0, Main.screenX, Main.screenY);
      fill(255, 255, 255); 
      textSize(30);
      text("PAUSED", Main.screenX * 0.5f, Main.screenY * 0.5f);
    } else if (status.equals("WIN"))
    {
      drawWin(); 
    } else if (status.equals("LOSE"))
    {
      drawLose(); 
    }
    //println(status);
  }
  
  //This function is called to run through all animations currently happening in the scene
  public void animateAll(long diff)
  {
    ArrayList<Animation> toRemove = new ArrayList<Animation>(); 
    for(Animation a: animations) 
    {
       boolean keep = a.animate(diff); 
       if(keep == false) 
       {
          toRemove.add(a);   
       }
    }
    animations.removeAll(toRemove); 
  }
  
  //This function is called when the state is "WIN"
  public void drawWin()
  {
      fill(0, 0, 0, 100);
      rect(0, 0, Main.screenX, Main.screenY);
      fill(255, 255, 255); 
      textSize(30);
      text("YOU WIN", Main.screenX * 0.5f, Main.screenY * 0.33f);
      fill(100, 200, 100); 
      rect(Main.screenX * 0.25f, Main.screenY * 0.4f, Main.screenX * 0.5f, Main.screenY * 0.2f, Main.screenY * 0.01f); 
      fill(255, 255, 255);
      textAlign(CENTER); 
      textSize(35); 
      text("Play Again", Main.screenX * 0.5f, Main.screenY * 0.5f);
      fill(230, 230, 90); 
      rect(Main.screenX * 0.25f, Main.screenY * 0.66f, Main.screenX * 0.5f, Main.screenY * 0.2f, Main.screenY * 0.01f); 
      fill(255, 255, 255);
      textAlign(CENTER); 
      textSize(35); 
      text("Main Menu", Main.screenX * 0.5f, Main.screenY * 0.76f); 
  }
  
  
  //This function is called when the state is "LOSE"
  public void drawLose()
  {
    fill(0, 0, 0, 100);
      rect(0, 0, Main.screenX, Main.screenY);
      fill(255, 255, 255); 
      textSize(30);
      text("YOU LOSE", Main.screenX * 0.5f, Main.screenY * 0.33f);
      fill(255, 200, 100); 
      rect(Main.screenX * 0.25f, Main.screenY * 0.4f, Main.screenX * 0.5f, Main.screenY * 0.2f, Main.screenY * 0.01f); 
      fill(255, 255, 255);
      textAlign(CENTER); 
      textSize(35); 
      text("Play Again", Main.screenX * 0.5f, Main.screenY * 0.5f);
      fill(230, 230, 90); 
      rect(Main.screenX * 0.25f, Main.screenY * 0.66f, Main.screenX * 0.5f, Main.screenY * 0.2f, Main.screenY * 0.01f); 
      fill(255, 255, 255);
      textAlign(CENTER); 
      textSize(35); 
      text("Main Menu", Main.screenX * 0.5f, Main.screenY * 0.76f);
  }





  public void drawHealthBars()
  {
    fill(0, 0, 0);
    rect(0, 0, Main.screenX * 0.1f, Main.screenY); //background for the player's health bar
    rect(Main.screenX * 0.9f, 0, Main.screenX * 0.1f, Main.screenY); //background for the enemy's health bar
    //Get color and size information for the player's health bar
    float healthRatio = Main.p.health / Main.p.maxHealth;
    float playerGreen = 255;
    if (healthRatio < 0.5f)
    {
      playerGreen = 255 * (healthRatio * 2);
    }
    float playerRed = 255;
    if (healthRatio > 0.5f)
    {
      playerRed = 255 - 255 * ((healthRatio - 0.5f) * 2);
    }
    //Get color and size information for the enemy's health bar
    float enemyHealthRatio = e.health / e.maxHealth;
    float enemyGreen = 255;
    if (enemyHealthRatio < 0.5f)
    {
      enemyGreen = 255 * (enemyHealthRatio * 2);
    }
    float enemyRed = 255;
    if (enemyHealthRatio > 0.5f)
    {
      enemyRed = 255 - 255 * ((enemyHealthRatio - 0.5f) * 2);
    }
    //println("playerRed = " + playerRed + ", playerGreen = " + playerGreen, ", health percentage = " + healthRatio);
    fill(playerRed, playerGreen, 0);
    rect(0, 0, Main.screenX * 0.1f, Main.screenY * healthRatio, Main.screenX * 0.05f); //Player's health bar
    fill(enemyRed, enemyGreen, 0);
    rect(Main.screenX * 0.9f, 0, Main.screenX * 0.1f, Main.screenY * enemyHealthRatio, Main.screenX * 0.05f);
    //3. Draw energy bar 
    //attacking reduces energy, clearing runes fills it
    fill(0, 0, 0); //background for the energy bar 
    rect(gridLeftEdge, Main.screenY - (gridRightEdge - gridLeftEdge) - Main.screenX * 0.1f, gridRightEdge - gridLeftEdge, Main.screenX * 0.1f); 
    fill(200, 200, 255); //draw the energy bar
    rect(gridLeftEdge, Main.screenY - (gridRightEdge - gridLeftEdge) - Main.screenX * 0.1f, (gridRightEdge - gridLeftEdge) * (Main.p.energy / Main.p.maxEnergy), Main.screenX * 0.1f, Main.screenX * 0.05f);
    //4. Draw spellbox
    noFill();
    rect(gridLeftEdge + (runeSize * start), Main.screenY - (runeSize * boxHeight), runeSize * boxWidth, runeSize * boxHeight);
  }





  //There should always be a rune falling from the top of the screen. If there is not, the game should create one
  public void generateRune()
  {
    rand = (int) random(p.unlockedRunes.size()); //Generates a random rune based on what runes the player currently has unlocked 
    String label = p.unlockedRunes.get(rand); //Check label to know what kind of rune to generate 
    //Whatever the rune type, draws it at the top of the grid, and in the horizontal center 
    if (label.equals("Fire"))
    {
      currentRune = new FireRune("Fire", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge, (screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize);  
      //println("Created a new Fire Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    } else if (label.equals("Heal"))
    {
      currentRune = new HealRune("Heal", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge, (screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize);
      //println("Created a new Healing Rune. X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    } else if (label.equals("Slash"))
    {
      currentRune = new SlashRune("Slash", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge, (screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize);
      //println("Created a new Slash Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    } else
    {
      currentRune = new FireRune("Fire", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge, (screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize);
      //println("Label not recognized =" + Main.screenX * 0.5 + ", y = 0.0, size = " + runeSize);
    }
    currentRune.speedY = 1; 
    nextRune = false;
  }





  //This function will be used to track the position of the currentRune in terms of the game grid, and look for collisions
  public void checkCollisions()
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
    //println("SquareX = " + squareX + ", SquareY = " + squareY + " X = " + currentRune.x + ", Y = " + currentRune.y + ", floor = " + floor + ", leftEdge = " + leftEdge); 
    //Prevents the rune from going any lower than the bottom of the screen. If rune gets that far, add it to the grid and create new rune
    if ((currentRune.y + (currentRune.size / 2) + currentRune.speedY) >= floor) 
    {
      r[squareY][squareX] = currentRune; 
      currentRune.x = (Main.screenX * 0.1f) + (runeSize / 2) + (runeSize * squareX);
      currentRune.y = (Main.screenY) - (runeSize / 2) - (runeSize * squareY);
      currentRune.speedX = 0; 
      currentRune.speedY = 0;
      currentRune = new NullRune("Null", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge, (screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize);
      nextRune = true;
      clearRunes(); //new rune added to the grid - clear any duplicates
    }
    //Prevents runes from moving beyond the left edge
    if ((currentRune.x - (runeSize / 2.0f) + currentRune.speedX) < leftEdge) 
    {
      currentRune.x = leftEdge + (runeSize / 2.0f) - currentRune.speedX;
    }
    //prevents rune from moving beyond right edge
    if ((currentRune.x + (runeSize / 2.0f) + currentRune.speedX) > rightEdge)
    {
      currentRune.x = rightEdge - (runeSize / 2.0f) - currentRune.speedX;
    }
  }





  //This function will be called whenever a new rune is added to the grid. Will clear away any connections, and lower the runes to their proper places.
  //Will also remove any runes in the upper areas of the grid and damage the player for going too high. Finally, will clear runes in the spellbox if they are not a proper combination 
  //Every time a set of runes is cleared (except in the upper rows or the spellbox), fill some of the player's action bar
  public void clearRunes()
  {  
    //Loop until no runes are removed. Removing a rune will trigger the "runes cleared" flag.
    boolean runesCleared = true;
    //first, check for vertical and horizontal chains. removing a chain may create a new one, so loop until no new chains are created
    while (runesCleared)
    {  
      runesCleared = false;
      boolean vert = checkVertical();
      boolean horiz = checkHorizontal();
      if ((vert == true) || (horiz == true)) runesCleared = true;
    }
    //next, check the upper two rows. Runes here will damage the player
    checkUpperRows();
    //Finally, if there are runes in the spellbox that don't mix, remove them all. For now, just assume that if they aren't the same type, they don't mix. Also, cannot mix more than 2 of same type
    checkSpellbox();
  }




  //This function will check for any vertical chains 
  public boolean checkVertical()
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
              r[l][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * l), runeSize);
            }
            for (int l = k; l < gridSize - 1; l++)
            {
              r[l - chain][j] = r[l][j]; //Drop the runes down to fill in the missing space
              animations.add(new RuneAnimation(r[l - chain][j], r[l - chain][j].x, r[l - chain][j].y, 0, 1, 0, r[l - chain][j].y + runeSize * chain)); 
              //r[l - chain][j].y += runeSize * chain; 
              //replace the missing rune
              r[l][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * l), runeSize);
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
  public boolean checkHorizontal()
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
                animations.add(new RuneAnimation(r[m][l], r[m][l].x, r[m][l].y, 0, 1, 0, r[m][l].y + runeSize)); 
                r[m][l].y += runeSize;
                //Replace the removed rune with a null rune 
                r[m + 1][l] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * l), (screenY) - (runeSize / 2) - (runeSize * (m + 1)), runeSize);
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
  public void checkUpperRows()
  {
    for (int i = gridSize - 2; i < gridSize; i++)
    {
      for (int j = 0; j < gridSize; j++)
      {
        if (!r[i][j].type.equals("Null"))
        {
          r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * i), runeSize);
          Main.p.health -= 5;
        }
      }
    }
  }




  //This function will check runes in the spellbox, and remove them if they don't mix. 
  //For now, the spellbox will be a 3 * 2 box at the bottom center of the rune grid. Runes don't mix if they are different types, 
  //and grid currently can only hold 2 of the same type
  public void checkSpellbox()
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
  public int getSpellCount(String type)
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
  public String getSpellType()
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
  public void clearSpellBox()
  {
    for (int i = 0; i < boxHeight; i++)
    {
      for (int j = start; j < end; j++)
      {
        r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j), (screenY) - (runeSize / 2) - (runeSize * i), runeSize);
      }
    }
  }

  public void castSpell()
  {
    String type = getSpellType();
    int count = getSpellCount(type);
    if ((count == 1) && (Main.p.energy >= 5))
    {
      if (type.equals("Fire"))
      {
        e.burnNum += 2; 
        Main.p.energy -= 5;
      } else if (type.equals("Slash"))
      {
        e.health -= 10;
        Main.p.energy -= 5;
      } else if (type.equals("Heal"))
      {
        Main.p.health += 10;
        if (Main.p.health > Main.p.maxHealth)
        {
          Main.p.health = Main.p.maxHealth;
        }
        Main.p.energy -= 5;
      }
      clearSpellBox();
    } else if ((count == 2) && (Main.p.energy >= 8))
    {
      if (type.equals("Fire"))
      {
        e.burnNum += 5; 
        Main.p.energy -= 8;
      } else if (type.equals("Slash"))
      {
        e.health -= 25;
        Main.p.energy -= 8;
      } else if (type.equals("Heal"))
      {
        Main.p.health += 25;
        if (Main.p.health > Main.p.maxHealth)
        {
          Main.p.health = Main.p.maxHealth;
        }
        Main.p.energy -= 8;
      } 
      clearSpellBox();
    }
  }



  //This function will be used to draw all previous runes that have not been cleared yet. 
  public void drawRuneGrid() 
  {
    for (Rune[] row : r) 
    {
      for (Rune rune : row) 
      {
        rune.drawRune();
      }
    }
  }



  public char mouseOverButton()
  {
    if (((mouseX > Main.screenX * 0.25f) && (mouseX < Main.screenX * 0.75f)) && ((mouseY > Main.screenY * 0.4f) && (mouseY < Main.screenY * 0.6f)))
    {
      return 'r';
    }
    else if(((mouseX > Main.screenX * 0.25f) && (mouseX < Main.screenX * 0.75f)) && ((mouseY > Main.screenY * 0.66f) && (mouseY < Main.screenY * 0.86f)))
    {
       return 'm'; 
    }
    
    else return ' ';
  }


  //This function can be used to check if the user pressed any of the on-screen controls (to be implemented later).  
  public void mouseClicked()
  {
    if(status.equals("LOSE") || status.equals("WIN"))
    {
      char button = mouseOverButton();
      if(button == 'r')
      {
         Main.s = new DemoGameScene(); 
      }
      else if(button == 'm')
      {
         Main.s = new StartMenu(); 
      }
    }
  }

  //This function has two main uses - to see if the user is dragging current gem, and to see if the user dragged up for an attack
  public void mouseDragged()
  {
    return;
  }

  //When the player presses a mouse button, the game will check two things. Did the player click on the currently falling rune? Or did they click on the spell-box?
  public void mousePressed()
  {
    return;
  }

  /**When the player releases the mouse, check if anything was selected. 
   If a rune was selected, release the rune. 
   If the spell-box was selected and the mouse is currently above the spell-box, perform an attack
   **/
  public void mouseReleased()
  {
    return;
  }

  public void keyPressed()
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
    } else if (key == ' ')
    {
      castSpell();
    } else if ((key == 'p') || (key == 'P'))
    {
      if (status.equals("ON"))
      {
        status = "PAUSED";
      } else if (status.equals("PAUSED"))
      {
        status = "ON";
      }
    }
  }

  public void keyReleased()
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
/* TODO 
Add enemy + boss subclasses for different enemy types 
Figure out different types of enemy attacks 
Implement enemy animations
Different types of enemies have different weaknesses 
Add additional stats
*/ 

class Enemy 
{
  float maxHealth;
  float health; //Keeps track of the enemy's current health
  float x; //Keeps track of the x coordinate of the enemy's lower left corner
  float y; //Keeps track of the y coordinate of the enemy's lower left corner
  int burnNum; //Keeps track of the number of burn stacks on the enemy
  int attackNum; //A timer that allows the enemy to attack every 5 seconds
  long oldT; 
  long newT;

  //A basic constructor that creates a dummy enemy
  Enemy()
  {
    maxHealth = 100;
    health = maxHealth;  
    x = Main.screenX * 0.4f;
    y = Main.screenY - Main.screenX;
    newT = millis(); 
    oldT = newT;
    attackNum = 5;
  }





  //Draws the dummy enemy to the screen
  public void drawEnemy()
  {
    fill(155, 55, 55);
    triangle(x + Main.screenX * 0.25f, y, x, y - (Main.screenX * 0.6f), x + (Main.screenX * 0.5f), y - (Main.screenX * 0.6f));
    triangle(x + Main.screenX * 0.15f, y - (Main.screenX * 0.6f), x + Main.screenX * 0.35f, y - Main.screenX * 0.6f, x + Main.screenX * 0.25f, y - Main.screenX * 0.8f);
    newT = millis();
    if (newT - oldT > 1000)
    {
      oldT = newT;
      if (attackNum == 0)
      {
        Main.p.health -= 10; 
        attackNum = 5;
      } else attackNum --;
      if (burnNum > 0)
      {
        health -= 6;
        burnNum --;
      }
    }
  }
}
class FireRune extends Rune
{
  
  
  FireRune(String Type, float x, float y, float size )
  {
     super(Type, x, y, size);  
  }
  
  //For now, the rune will just be drawn as a red circle
  public void drawRune()
  {
      fill(255,0,0); 
      x = x + speedX; 
      y = y + speedY;
      ellipse(x,y,size,size); 
  }
  
  //The rune's attack will be a triangle-shaped gradient from red to yellow, and will deal 10 magic damage to the enemy
  public void attack()
  {
    
    
  }
  
  
  
  
}
class HealRune extends Rune
{
  
  
  HealRune(String Type, float x, float y, float size )
  {
     super(Type, x, y, size);  
  }
  
  //For now, the rune will just be drawn as a red circle
  public void drawRune()
  {
      fill(255,255,0); 
      x = x + speedX; 
      y = y + speedY; 
      ellipse(x,y,size,size); 
  }
  
  //The rune's attack will be a triangle-shaped gradient from red to yellow, and will deal 10 magic damage to the enemy
  public void attack()
  {
    
    
  }
  
  
  
  
}
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

  public void drawScene(long diff)
  {
    clear(); 
    background(255); 
    for (int i = 0; i < characters.length; i++)
    {
      fill(200); 
      rect(Main.screenX * 0.1f + (Main.screenX * 0.3f * i), Main.screenY * 0.1f, Main.screenX * 0.8f, Main.screenX * 0.2f, Main.screenX * 0.05f);
      fill(0);
      textAlign(CENTER, CENTER); 
      text(characters[i], Main.screenX * 0.5f, Main.screenY * (0.5f + (1.0f / 3.0f)));
    }
  }

  //These are system functions that every scene must implement 
  public void mouseClicked()
  {
  }

  public void mouseDragged()
  {
  }

  public void mousePressed()
  {
  }

  public void mouseReleased()
  {
  }

  public void keyPressed()
  {
  }

  public void keyReleased()
  {
  }
}
//This scene is called when the player wants to create a new character. 
class NewCharacter implements Scene
{
  int maxLength = 30;
  char text[] = new char[maxLength];  
  int charPointer = 0; 
  boolean addedTextField = false; 
  int characterButton = color(100, 255, 100);
  int characterHover = color(55, 200, 55); 
  int backButton = color(255, 100, 100); 
  int backHover = color(200, 55, 55); 
  int buttonText = 0; 
  int grayedText = 100;
  File f = new File("/Data/Characters.txt");
  

  public void drawScene(long diff)
  {
    clear(); 
    background(255); 
    fill(0); 
    textAlign(CENTER, CENTER); 
    textSize(22); 
    text("Type Your Character's Name Here", Main.screenX * 0.5f, Main.screenY * 0.1f);
    fill(220); 
    rect(Main.screenX * 0.1f, Main.screenY * 0.2f, Main.screenX * 0.8f, Main.screenY * 0.1f, Main.screenX * 0.05f); 
    fill(0); 
    textAlign(LEFT, CENTER); 
    String textString = new String(text).trim(); 
    text(textString, Main.screenX * 0.1f, Main.screenY * 0.25f);
    //Two buttons - "Create Character" is green, "Back to Menu" is red 
    //Both buttons change color on hover 
    //"Create Character" button on top, clickable if text is non-empty 
    //Create Character button
    //Check if the mouse is over the button
    if (((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f)))) 
    {
      fill(characterHover);
    } else
    {
      fill(characterButton);
    }
    rect(Main.screenX * 0.1f, Main.screenY * ((1.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
    //Check if character button is clickable 
    if(textString.length() == 0)
    {
      fill(grayedText);
    }
    else fill(buttonText); 
    textAlign(CENTER, CENTER); 
    textSize(35); 
    text("Create Character", Main.screenX * 0.5f, Main.screenY * 0.5f); 
    //Create Character Button
    //Check if mouse is over the button 
    if (((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY - 0.05f))) 
    {
      fill(backHover);
    } else
    {
      fill(backButton);
    }
    rect(Main.screenX * 0.1f, Main.screenY * ((2.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
    fill(buttonText);
    textAlign(CENTER, CENTER); 
    textSize(35); 
    text("Back to Menu", Main.screenX * 0.5f, Main.screenY * (0.5f + (1.0f / 3.0f)));
  }

  //These are system functions that every scene must implement 
  public void mouseClicked()
  {
     //Check if mouse is over "Create Character"
     if ((new String(text).trim().length() > 0) && ((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f))))
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
     else if (((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY - 0.05f)))
     {
       Main.s = new StartMenu(); 
     }
  }

  public void mouseDragged()
  {
    return;
  }

  public void mousePressed()
  {
    return;
  } 

  public void mouseReleased()
  {
    return;
  }

  public void keyPressed()
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

  public void keyReleased()
  {
    return;
  }
}
class NullRune extends Rune
{
  
  
  NullRune(String Type, float x, float y, float size )
  {
     super(Type, x, y, size);  
  }
  
  //For now, the rune will just be drawn as a red circle
  public void drawRune()
  {
      /**noFill(); 
      x = x + speedX; 
      y = y + speedY; 
      ellipse(x,y,size,size); **/
  }
  
  //The rune's attack will be a triangle-shaped gradient from red to yellow, and will deal 10 magic damage to the enemy
  public void attack()
  {
     return;   
  }
  
  
  
  
}
class Player 
{
  //1. Global Variables - will expand over time. For right now, need health and runes. 
  //TODO: Expand player - allow customization, keep track of unlocked skills and abilities, 
  //Track stats, experience, level, appearance, inventory, equipment, etc. 
  
  String name; 
  float maxHealth; //Keeps track of the player's maximum health value
  float health; //Tracks the player's health value
  float maxEnergy; //Keeps track of the player's maximum energy value
  float energy; //Tracks the player's energy value (gets reduced when player attacks, fills from clearing runes) 
  float x; //represents the x position of the player's bottom left corner
  float y; //represents the y position of the player's bottom left corner
  ArrayList<String> unlockedRunes; //Keeps track of what runes the player currently has available
  
  Player() //Used to create a null or default character
  {
     maxHealth = 100.0f;
     health = maxHealth;
     maxEnergy = 50; 
     energy = maxEnergy;
     unlockedRunes = new ArrayList<String>(); 
     unlockedRunes.add("Fire"); 
     unlockedRunes.add("Slash"); 
     unlockedRunes.add("Heal"); 
     x = Main.screenX * 0.2f;
     y = Main.screenY - Main.screenX;
     name = "NULL"; 
  }
  
  Player(String name) //Used to create a default character with the given name 
  {
     maxHealth = 100.0f;
     health = maxHealth;
     maxEnergy = 50; 
     energy = maxEnergy;
     unlockedRunes = new ArrayList<String>(); 
     unlockedRunes.add("Fire"); 
     unlockedRunes.add("Slash"); 
     unlockedRunes.add("Heal"); 
     x = Main.screenX * 0.2f;
     y = Main.screenY - Main.screenX;
     this.name = name;
  }
  
  
  
  public void drawPlayer()
  {
    //This blue triangle represents the player's body
    fill(200, 200, 255);
    triangle(x, y, x + (Main.screenX * 0.2f), y, x + (Main.screenX * 0.1f), y - (Main.screenX * 0.2f));
    //this (hopefully) peach colored circle represents the player's head
    fill(255, 245, 200);
    ellipse(x + (Main.screenX * 0.1f), y - (Main.screenX * 0.3f), Main.screenX * 0.2f, Main.screenX * 0.2f);
  }
  
  
  
  
  
  
  
  
  
  
}
abstract class Rune
{   
  //Rune Variables 
  String type; //Keeps track of the type of rune
  float x; //Keeps track of the rune's x position 
  float y; //Keeps track of the rune's y position 
  float size; //Keeps track of the rune's radius - depends on the grid size
  float speedX; 
  float speedY; 
  
  Rune(String type, float x, float y, float size)
  {
     this.type = type; 
     this.x = x; 
     this.y = y; 
     this.size = size; 
  }
  
  //This method will define how to draw the rune to the screen
  public abstract void drawRune(); 
  
  //This method will define how to draw the attack generated by the rune, as well as it's effects
  public abstract void attack(); 
  
  
  
  
  
  
}
class RuneAnimation extends Animation
//TODO: Replace with Sprite animations? 
{
  Rune r; 
  float x; 
  float y; 
  float speedX; 
  float speedY;
  float endX; 
  float endY; 
  
  
  RuneAnimation(Rune r, float x, float y, float speedX, float speedY, float endX, float endY)
  {
    this.r = r; 
    this.x = x; 
    this.y = y; 
    this.speedX = speedX; 
    this.speedY = speedY; 
    this.endX = endX; 
    this.endY = endY; 
  }
  
   public boolean animate(long t) 
   {
     boolean keepGoing = true; 
     if((speedX > 0) && (x >= endX)) 
     {  
       keepGoing =  false;
     }
     else if((speedX < 0) && (x <= endX))
     {
        keepGoing = false;  
     }
     if((speedY > 0) && (y >= endY))
     {
        keepGoing = false;  
     }
     else if((speedY < 0) && (y <= endY ))
     {
        keepGoing = false;  
     }
     if(keepGoing)
     {
        r.x += speedX; 
        r.y += speedY; 
        x = r.x; 
        y = r.y; 
     }
     
     
       return keepGoing;
   }
}
interface Scene
{
//1. Standard Variables that will be inherited by all scenes 

//This function will be implemented by all Scenes, and will be used to draw the scene itself to the screen. 
//Inputs: diff - an int that represents the amount of milliseconds that have elapsed since the last time this function was called
public void drawScene(long diff); 

//These are system functions that every scene must implement 
public void mouseClicked(); 

public void mouseDragged(); 

public void mousePressed(); 

public void mouseReleased(); 

public void keyPressed(); 

public void keyReleased();

    
}
//This class will be used to logically represent the skill tree
class SlashRune extends Rune
{
  
  
  SlashRune(String Type, float x, float y, float size )
  {
     super(Type, x, y, size);  
  }
  
  //For now, the rune will just be drawn as a red circle
  public void drawRune()
  {
      fill(155,155,155); 
      x = x + speedX; 
      y = y + speedY; 
      ellipse(x,y,size,size); 
  }
  
  //The rune's attack will be a triangle-shaped gradient from red to yellow, and will deal 10 magic damage to the enemy
  public void attack()
  {
    
    
  }
  
  
  
  
}
class SpriteAnimation extends Animation
{

  float spriteWidth; 
  float spriteHeight; 
  PImage spriteSheet; 
  int frame;
  int numFrames; 
  long oldT; 
  long newT; 
  float x; 
  float y; 

  SpriteAnimation(float width, float height, float x, float y, int numFrames, String sheetFile)
  {
    spriteWidth = width;
    spriteHeight = height; 
    this.x = x; 
    this.y = y; 
    this.numFrames = numFrames; 
    spriteSheet = loadImage(sheetFile); 
    oldT = millis(); 
    newT = oldT; 
    image(spriteSheet, x, y, spriteWidth, spriteHeight);
    frame = 0;
  }

  public boolean animate(long t)
  { 
    newT = millis(); 
    if ((newT - oldT) < 83 )
    {
    } 
    else
    {
      if(frame == numFrames - 1)
      {
        frame = 0; 
      }
      else 
      { 
        frame = frame + 1;
      }
      oldT = newT;
    }
    image(spriteSheet.get( 0 + ((int) spriteHeight * frame), 0, (int) spriteHeight, (int) spriteHeight), x, y - (3 * spriteHeight), 3 * spriteHeight, 3 * spriteHeight);
    return true;
  }
}
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
  int button = 200; 
  int buttonHover = 155; 
  int buttonText = 0; 
  int grayedText = 100; 
  File f = new File("/Data/Characters.txt"); 
  boolean canLoad; 

  //1. These functions are inherited from the Scene interface 
  public void drawScene(long diff)
  {
    clear(); 
    background(255, 255, 255);
    //Write the title
    text("Rune Battle", Main.screenX * 0.5f, Main.screenY * (1.0f / 6.0f)); 
    //Two buttons - "Load Game" and "Create Character" 
    //Both buttons change color on hover 
    //Load game is on top, greyed out unless there is at least one character file 
    //Load Game button
    //Check if the mouse is over the button
    if (((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f)))) 
    {
      fill(buttonHover);
    } else
    {
      fill(button);
    }
    rect(Main.screenX * 0.1f, Main.screenY * ((1.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
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
    text("Load Game", Main.screenX * 0.5f, Main.screenY * 0.5f); 
    //Create Character Button
    //Check if mouse is over the button 
    if (((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY - 0.05f))) 
    {
      fill(buttonHover);
    } else
    {
      fill(button);
    }
    rect(Main.screenX * 0.1f, Main.screenY * ((2.0f / 3.0f) + 0.05f), Main.screenX * 0.8f, Main.screenY * ((1.0f / 3.0f) - 0.1f), Main.screenY * 0.05f); 
    fill(buttonText);
    textAlign(CENTER, CENTER); 
    textSize(35); 
    text("Create \n Character", Main.screenX * 0.5f, Main.screenY * (0.5f + (1.0f / 3.0f)));
  }

  //2. These functions will be called by the main function if the appropriate event occurs
  //This function will get called from the main function if there is a mouse click. Checks if the user pressed a button, and if so changes to the appropriate scene
  public void mouseClicked()
  {
     //Check if mouse is over "Load Game Button". If so, and button is clickable, go to "Load Game" scene
     if (canLoad && ((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((1.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY * ((2.0f / 3.0f) - 0.05f))))
     {
       Main.s = new LoadGame(); 
     }
     //Check if mouse is over the "Create Character" button
     else if (((mouseX > Main.screenX * 0.1f) && (mouseX < Main.screenX * 0.9f)) && ((mouseY > Main.screenY * ((2.0f / 3.0f) + 0.05f)) && (mouseY < Main.screenY - 0.05f)))
     {
       Main.s = new NewCharacter(); 
     }
  }

  //For this particular scene, this function does nothing 
  public void mouseDragged()
  {
    return;
  }

  //If the mouse is pressed within the area of the button, change the color of the button to show it is pressed and change the "buttonPressed" flag to 1
  public void mousePressed()
  {
    return; 
  }

  //If the "buttonPressed" flag is 1, change it back to zero and change the color of the button back 
  public void mouseReleased()
  {
    return; 
  }

  public void keyPressed()
  {
    return;
  }

  public void keyReleased()
  {
    return;
  }
}
  public void settings() {  size(375, 667); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
