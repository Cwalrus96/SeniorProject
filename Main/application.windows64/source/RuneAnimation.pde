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
  
   boolean animate(long t) 
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
