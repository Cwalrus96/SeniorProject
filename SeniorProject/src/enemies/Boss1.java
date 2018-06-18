package enemies;

import main.Main;

public class Boss1 extends Enemy {

	public Boss1(Main main)
	  {
		super(main);
	    maxHealth = 150;
	    health = maxHealth;  
	    x = Main.screenX * 0.2f;
	    y = Main.screenY;
	    newT = main.millis(); 
	    oldT = newT;
	    attackNum = 4;
	    this.main = main;
	  }

	// Draws the dummy enemy to the screen
	public void drawEnemy() {
		/**main.fill(0, 0, 0);
		main.triangle(x + Main.screenX * 0.25f, y, x, y - (Main.screenX * 0.6f), x + (Main.screenX * 0.5f),
				y - (Main.screenX * 0.6f));
		main.triangle(x + Main.screenX * 0.15f, y - (Main.screenX * 0.6f), x + Main.screenX * 0.35f,
				y - Main.screenX * 0.6f, x + Main.screenX * 0.25f, y - Main.screenX * 0.8f);
				**/
		newT = main.millis();
		if (newT - oldT > 1000) {
			oldT = newT;
			if (attackNum == 0) {
				Main.p.health -= 12;
				attackNum = 4;
			} else
				attackNum--;
			if (burnNum > 0) {
				health -= 6;
				burnNum--;
			}
		}
	}
}