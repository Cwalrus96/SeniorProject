package scenes;

import seniorproject.Main;

public abstract class Scene {
	// 1. Standard Variables that will be inherited by all scenes
	Main main; 
	
	Scene(Main main)
	{
		this.main = main; 
	}

	// This function will be implemented by all Scenes, and will be used to draw the
	// scene itself to the screen.
	// Inputs: diff - an int that represents the amount of milliseconds that have
	// elapsed since the last time this function was called
	public abstract void drawScene(long diff);

	// These are system functions that every scene must implement
	public void mouseClicked() {
		return;
	}

	public void mouseDragged() {
		return;
	}

	public void mousePressed() {
		return;
	}

	public void mouseReleased() {
		return;
	}

	public void keyPressed() {
		return;
	}

	public void keyReleased() {
		return;
	}

}
