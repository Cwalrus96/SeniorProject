package runes;

import seniorproject.Main;

//This class will be used to logically represent the skill tree
class SlashRune extends Rune {

	SlashRune(String Type, float x, float y, float size, Main main) {
		super(Type, x, y, size, main);
	}

	// For now, the rune will just be drawn as a red circle
	public void drawRune() {
		main.fill(155, 155, 155);
		x = x + speedX;
		y = y + speedY;
		main.ellipse(x, y, size, size);
	}

	// The rune's attack will be a triangle-shaped gradient from red to yellow, and
	// will deal 10 magic damage to the enemy
	public void attack() {

	}

}