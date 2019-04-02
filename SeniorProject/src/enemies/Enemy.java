package enemies;

import seniorproject.Main;

/* TODO 
Add enemy + boss subclasses for different enemy types 
Figure out different types of enemy attacks 
Implement enemy animations
Different types of enemies have different weaknesses 
Add additional stats
*/ 

public class Enemy 
{
  public float maxHealth;
  public float health; //Keeps track of the enemy's current health
  public float x; //Keeps track of the x coordinate of the enemy's lower left corner
  public float y; //Keeps track of the y coordinate of the enemy's lower left corner
  public int burnNum; //Keeps track of the number of burn stacks on the enemy
  int attackNum; //A timer that allows the enemy to attack every 5 seconds
  long oldT; 
  long newT;
  Main main;

  //A basic constructor that creates a dummy enemy
  public Enemy(Main main)
  {
    maxHealth = 100;
    health = maxHealth;  
    x = Main.screenX * 0.4f;
    y = Main.screenY - Main.screenX;
    newT = main.millis(); 
    oldT = newT;
    attackNum = 5;
    this.main = main;
  }





  //Draws the dummy enemy to the screen
  public void drawEnemy()
  {
	  /**
    main.fill(155, 55, 55);
    main.triangle(x + Main.screenX * 0.25f, y, x, y - (Main.screenX * 0.6f), x + (Main.screenX * 0.5f), y - (Main.screenX * 0.6f));
    main.triangle(x + Main.screenX * 0.15f, y - (Main.screenX * 0.6f), x + Main.screenX * 0.35f, y - Main.screenX * 0.6f, x + Main.screenX * 0.25f, y - Main.screenX * 0.8f);
    **/
    newT = main.millis();
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