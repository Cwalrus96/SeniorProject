interface Scene
{
//1. Standard Variables that will be inherited by all scenes 

//This function will be implemented by all Scenes, and will be used to draw the scene itself to the screen. 
//Inputs: diff - an int that represents the amount of milliseconds that have elapsed since the last time this function was called
void drawScene(long diff); 

//These are system functions that every scene must implement 
void mouseClicked(); 

void mouseDragged(); 

void mousePressed(); 

void mouseReleased(); 

void keyPressed(); 

void keyReleased();

    
}
