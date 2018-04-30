class NullRune extends Rune
{
  
  
  NullRune(String Type, float x, float y, float size )
  {
     super(Type, x, y, size);  
  }
  
  //For now, the rune will just be drawn as a red circle
  void drawRune()
  {
      /**noFill(); 
      x = x + speedX; 
      y = y + speedY; 
      ellipse(x,y,size,size); **/
  }
  
  //The rune's attack will be a triangle-shaped gradient from red to yellow, and will deal 10 magic damage to the enemy
  void attack()
  {
     return;   
  }
  
  
  
  
}
