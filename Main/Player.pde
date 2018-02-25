class Player 
{
  //1. Global Variables - will expand over time. For right now, need health and runes. 
  //TODO: Expand player - allow customization, keep track of unlocked skills and abilities, 
  //Track stats, experience, level, appearance, inventory, equipment, etc. 
  
  int health; 
  ArrayList<String> unlockedRunes;
  
  Player()
  {
     health = 100; 
     unlockedRunes = new ArrayList<String>(); 
     unlockedRunes.add("Fire"); 
     unlockedRunes.add("Slash"); 
     unlockedRunes.add("Heal"); 
  }
  
  
  
  
  
  
  
  
  
  
}