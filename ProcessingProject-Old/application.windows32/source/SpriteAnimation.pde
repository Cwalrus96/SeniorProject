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

  boolean animate(long t)
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
