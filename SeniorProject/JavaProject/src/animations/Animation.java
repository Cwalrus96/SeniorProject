package animations;

import seniorproject.Main;

public abstract class Animation
{
    long oldT; 
    long newT; 
    Main main;
    public abstract boolean animate(long t);
    
}