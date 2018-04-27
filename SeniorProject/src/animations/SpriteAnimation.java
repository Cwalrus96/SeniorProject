package animations;

import processing.core.PImage;
import seniorproject.Main;

public class SpriteAnimation extends Animation {

	float spriteWidth;
	float spriteHeight;
	PImage spriteSheet;
	int frame;
	int numFrames;
	long oldT;
	long newT;
	float x;
	float y;

	SpriteAnimation(float width, float height, float x, float y, int numFrames, String sheetFile, Main main) {
		this.main = main;
		spriteWidth = width;
		spriteHeight = height;
		this.x = x;
		this.y = y;
		this.numFrames = numFrames;
		spriteSheet = main.loadImage(sheetFile);
		oldT = main.millis();
		newT = oldT;
		main.image(spriteSheet, x, y, spriteWidth, spriteHeight);
		frame = 0;
	}

	public boolean animate(long t) {
		newT = main.millis();
		if ((newT - oldT) < 83) {
		} else {
			if (frame == numFrames - 1) {
				frame = 0;
			} else {
				frame = frame + 1;
			}
			oldT = newT;
		}
		main.image(spriteSheet.get(0 + ((int) spriteHeight * frame), 0, (int) spriteHeight, (int) spriteHeight), x,
				y - (3 * spriteHeight), 3 * spriteHeight, 3 * spriteHeight);
		return true;
	}
}