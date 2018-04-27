package runes;

import seniorproject.Main;

public abstract class Rune
{   
  //Rune Variables 
  String type; //Keeps track of the type of rune
  public float x; //Keeps track of the rune's x position 
  public float y; //Keeps track of the rune's y position 
  float size; //Keeps track of the rune's radius - depends on the grid size
  float speedX; 
  float speedY; 
  Main main; 
  
  Rune(String type, float x, float y, float size, Main main)
  {
     this.type = type; 
     this.x = x; 
     this.y = y; 
     this.size = size; 
     this.main = main;
  }
  
  //This method will define how to draw the rune to the screen
  public abstract void drawRune(); 
  
  //This method will define how to draw the attack generated by the rune, as well as it's effects
  public abstract void attack(); 
   
}