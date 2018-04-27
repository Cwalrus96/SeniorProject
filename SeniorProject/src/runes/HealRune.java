package runes;

import seniorproject.Main;

class HealRune extends Rune
{
  
  
  HealRune(String Type, float x, float y, float size, Main main )
  {
     super(Type, x, y, size, main);  
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