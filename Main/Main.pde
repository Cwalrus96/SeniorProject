//1. Global Variables 
/**TODO today: 
Implement fps rendering (DONE)
Implement runes
Implement rune tracking in the DemoGameScene
Implement rough DemoGameScene layout 
Implement player + enemy health 
Implement button + mouse controls
**/

static Scene s;  //Keeps track of the current screen 
static float screenX = 375;   //Tracks the width of the screen
static float screenY = 667; //Tracks the height of the screen 
static boolean debug = true;   //Flag used to view debug features such as mouse position
static int targetFPS = 60;   //The desired frame-per-second rate 
long oldT; 
long newT; 
long diff;
int frameLength; 



//3. This function will be called once to set up the scene 
void setup() 
{
  /**a. Set the size. This size was chosen because it is the same aspect ratio as an Iphone screen, but at a slightly lower resolution. 
  The box appears on my screen as about the same size as an actual Iphone screen, which will allow me to test if the game works well on that sized screen. 
  TODO: Implement function to adjust display based on size and resolution of the screen, while maintaining aspect ratio 
  **/
  size(375, 667); 
  //b. Initialize Global Variables 
    //i. The first scene should always be the start menu
    s = new StartMenu(); 
    //These variables will be used to track time and keep the game running at the target frames per second
    oldT = millis(); 
    newT = oldT; 
    frameLength = 1000 / targetFPS;
   
}

void draw()
{
  newT = millis(); 
  diff = newT - oldT; 
  if(diff > frameLength)
  {
    s.drawScene(diff); 
    if(debug)
    {
       textAlign(LEFT); 
       textSize(screenY * 0.03); 
       text("MouseX = " + mouseX, 0, screenY * 0.05);
       text("MouseY = " + mouseY, 0, screenY * 0.1); 
    }
  }
}


//This function will be called to deal with mouse clicks. For the most part, it should be used to call the corresponding function in the current scene
//TODO: update mouse functions so that they deal with taps and touches 
void mouseClicked() 
{
  s.mouseClicked(); 
}

//This function will deal with users pressing the mouse button by calling the corresponding function in the current scene
void mousePressed()
{
   s.mousePressed();  
}

void mouseReleased()
{
   s.mouseReleased();  
}

void mouseDragged()
{
  
}

void keyPressed()
{
  s.keyPressed(); 
}

void keyReleased()
{
   s.keyReleased();  
}