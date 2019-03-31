package scenes;

import java.util.ArrayList;

import animations.Animation;
import animations.RuneAnimation;
import animations.SpriteAnimation;
import enemies.Enemy;
import main.Main;
import main.Player;
import runes.NullRune;
import runes.FireRune;
import runes.HealRune; 
import runes.SlashRune;
import runes.Rune;
import userInterface.ButtonAction;
import userInterface.GameButton;

//This scene represents the actual gameplay of the game. For now, will make simplest possible demo version. 

public class DemoGameScene extends Scene {

	// 1. Class Variables
	Enemy e; // Keeps track of the enemy that you are currently battling
	Rune[][] r;
	int gridSize = 9; // Adjusts the size of the grid, and therefore the number of runes
	boolean nextRune = true; // This flag keeps track of when to generate the next rune
	float gridLeftEdge = (Main.screenX * 0.1f); // Keeps track of the leftmost edge of the rune grid. Runes should not
												// go beyond this point
	float gridRightEdge = (Main.screenX * 0.9f); // Keeps track of the right edge of the rune grid
	float runeSize = ((gridRightEdge - gridLeftEdge) / ((float) gridSize)); // Keeps track of the rune radius
	Rune currentRune; // Keeps track of the rune that the player currently controls
	int rand; // Used to keep track of random variables
	int squareX; // Used to track the current rune's X position relative to the rune grid
	int squareY;
	// The following variables are used to track info about the spellBox
	int boxWidth = 3;
	int boxHeight = 2;
	int maxNum = 2;
	int start = ((gridSize - boxWidth) / 2);
	int end = start + boxWidth;
	float startingEnergy; 
	float startingHealth;
	GameStatus status; // Keeps track of the current state of the program. Possible states are - ON,
					// PAUSED, WIN, and LOSE
	ArrayList<Animation> animations; // This will keep track of all the animations currently going on the screen
	SpriteAnimation characterAnimation; 
	SpriteAnimation enemyAnimation;
	ArrayList<GameButton> buttons; 

	// Basic constructor that is going to create a new dummy character and dummy
	// enemy for demo
	public DemoGameScene(Main main) {
		super(main);
		Main.p = new Player(main);
		e = new Enemy(main);
		r = new Rune[gridSize][gridSize];
		animations = new ArrayList<Animation>();
		characterAnimation = new SpriteAnimation(32 * 9, 32, Main.p.x, Main.p.y, 9, "Resources/WizardIdle.png", main);
		enemyAnimation = new SpriteAnimation(64 * 9, 64, e.x, e.y, 9, "Resources/EnemyIdle.png", main);
		animations.add(characterAnimation);
		animations.add(enemyAnimation);
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				// Fills the grid with null runes, which are nothing but placeholders
				r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j),
						(Main.screenY) - (runeSize / 2) - (runeSize * i), runeSize, main);
			}
		}
		status = GameStatus.ON;
		buttons = new ArrayList<GameButton>();
		startingEnergy = Main.p.energy;
		startingHealth = Main.p.health;
	}
	
	//This creates a demo game scene with the chosen player
	public DemoGameScene(Main main, Player p) {
		super(main);
		e = new Enemy(main);
		r = new Rune[gridSize][gridSize];
		animations = new ArrayList<Animation>();
		characterAnimation = new SpriteAnimation(32 * 9, 32, Main.p.x, Main.p.y, 9, Main.p.getSpriteFile(), main);
		enemyAnimation = new SpriteAnimation(64 * 9, 64, e.x, e.y, 9, "Resources/EnemyIdle.png", main);
		animations.add(characterAnimation);
		animations.add(enemyAnimation);
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				// Fills the grid with null runes, which are nothing but placeholders
				r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j),
						(Main.screenY) - (runeSize / 2) - (runeSize * i), runeSize, main);
			}
		}
		status = GameStatus.ON;
		buttons = new ArrayList<GameButton>();
		startingEnergy = Main.p.energy;
		startingHealth = Main.p.health;
	}

	// Draw the playing area, player + enemy health-bars, and all of the runes to
	// the screen
	public void drawScene(long diff) // Diff represents the amount of time that has passed since the previous frame,
										// and is used to control the speed of moving objects to make everything move
										// smoothly
	{
		if (status == GameStatus.ON) {
			main.clear();
			main.background(255, 255, 255);
			if (nextRune == true) // Generates the next rune if necessary
			{
				generateRune();
			}
			checkCollisions();
			currentRune.drawRune();
			// println("Current rune x = " + currentRune.x + ", Current rune y = " +
			// currentRune.y);
			drawRuneGrid();
			// 1. Draw enemy and player
			// Main.p.drawPlayer();
			e.drawEnemy(); //For right now this actually only controls the enemy behavior - actually drawing is done by the animations
			// 2. Draw enemy + player health bars
			// health bars should change size and color based on health
			drawHealthBars();
			if (e.health <= 0) {
				if(status != GameStatus.WIN)
				{
					status = GameStatus.WIN;
					main.stages.unlockNode(main.stages.findNode("Boss1")); 
					main.savePlayer(); 
					//Add buttons
					buttons.clear();
					GameButton continueButton = new GameButton(Main.screenX * 0.25f, Main.screenY * 0.4f, 
							Main.screenX * 0.5f, Main.screenY * 0.2f, true, "Continue", 
							main.color(100, 200, 100), main.color(55, 155, 55), 255, 0, 35, 
							Main.screenY * 0.1f, main);
					GameButton menuButton = new GameButton(Main.screenX * 0.25f, Main.screenY * 0.66f, 
							Main.screenX * 0.5f, Main.screenY * 0.2f, true, "Main Menu", 
							main.color(230, 230, 90), main.color(200, 200, 55), 255, 0, 35, 
							Main.screenY * 0.1f, main);
					continueButton.action = new ButtonAction() {
						public void clickAction(Main main) {
							Main.s = new MapScene(main);
						}
					};
					menuButton.action = new ButtonAction() {
						public void clickAction(Main main) {
							Main.s = new StartMenu(main);
						}
					};
					buttons.add(continueButton);
					buttons.add(menuButton);
				}
			}
			if (Main.p.health <= 0) {
				if(status != GameStatus.LOSE)
				{
					status = GameStatus.LOSE;
					//Add buttons
					buttons.clear();
					GameButton continueButton = new GameButton(Main.screenX * 0.25f, Main.screenY * 0.4f, 
							Main.screenX * 0.5f, Main.screenY * 0.2f, true, "Try Again", 
							main.color(100, 200, 100), main.color(55, 155, 55), 255, 0, 35, 
							Main.screenY * 0.1f, main);
					GameButton menuButton = new GameButton(Main.screenX * 0.25f, Main.screenY * 0.66f, 
							Main.screenX * 0.5f, Main.screenY * 0.2f, true, "Main Menu", 
							main.color(230, 230, 90), main.color(200, 200, 55), 255, 0, 35, 
							Main.screenY * 0.1f, main);
					continueButton.action = new ButtonAction() {
						public void clickAction(Main main) {
							Main.p.health = startingHealth; 
							Main.p.energy = startingEnergy;
							Main.s = new DemoGameScene(main, Main.p);
						}
					};
					menuButton.action = new ButtonAction() {
						public void clickAction(Main main) {
							Main.p.health = startingHealth; 
							Main.p.energy = startingEnergy;
							main.savePlayer(); 
							Main.s = new StartMenu(main);
						}
					};
					buttons.add(continueButton);
					buttons.add(menuButton);
				}
			}
			animateAll(diff);
			// runes that don't combine dissappear, pressing attack clears spellbox +
			// creates effects
		} else if (status == GameStatus.PAUSED) {
			main.fill(0, 0, 0, 100);
			main.rect(0, 0, Main.screenX, Main.screenY);
			main.fill(255, 255, 255);
			main.textSize(30);
			main.text("PAUSED", Main.screenX * 0.5f, Main.screenY * 0.5f);
		} else if (status == GameStatus.WIN) {
			drawWin();
		} else if (status == GameStatus.LOSE) {
			drawLose();
		}
		// println(status);
	}

	// This function is called to run through all animations currently happening in
	// the scene
	public void animateAll(long diff) {
		if (Main.p.statusChange)
		{
			Main.p.statusChange = false;
			characterAnimation = new SpriteAnimation(32 * 9, 32, Main.p.x, Main.p.y, 9, Main.p.getSpriteFile(), main);
		}
		ArrayList<Animation> toRemove = new ArrayList<Animation>();
		for (Animation a : animations) {
			boolean keep = a.animate(diff);
			if (keep == false) {
				toRemove.add(a);
			}
		}
		animations.removeAll(toRemove);
	}

	// This function is called when the state is "WIN"
	public void drawWin() {
		main.clear(); 
		main.background(0);
		main.textSize(35);
		main.text("You Win", Main.screenX * 0.5f, Main.screenY * 0.2f);
		for(GameButton b: buttons)
		{
			b.checkHover(main.mouseX, main.mouseY);
			b.drawButton();
		}	
	}

	// This function is called when the state is "LOSE"
	public void drawLose() {
		main.clear(); 
		main.background(0);
		main.fill(255); 
		main.textSize(35);
		main.text("You Lose", Main.screenX * 0.5f, Main.screenY * 0.2f);
		for(GameButton b: buttons)
		{
			b.checkHover(main.mouseX, main.mouseY);
			b.drawButton();
		}
	}

	public void drawHealthBars() {
		main.fill(0, 0, 0);
		main.rect(0, 0, Main.screenX * 0.1f, Main.screenY); // background for the player's health bar
		main.rect(Main.screenX * 0.9f, 0, Main.screenX * 0.1f, Main.screenY); // background for the enemy's health bar
		// Get color and size information for the player's health bar
		float healthRatio = Main.p.health / Main.p.maxHealth;
		float playerGreen = 255;
		if (healthRatio < 0.5f) {
			playerGreen = 255 * (healthRatio * 2);
		}
		float playerRed = 255;
		if (healthRatio > 0.5f) {
			playerRed = 255 - 255 * ((healthRatio - 0.5f) * 2);
		}
		// Get color and size information for the enemy's health bar
		float enemyHealthRatio = e.health / e.maxHealth;
		float enemyGreen = 255;
		if (enemyHealthRatio < 0.5f) {
			enemyGreen = 255 * (enemyHealthRatio * 2);
		}
		float enemyRed = 255;
		if (enemyHealthRatio > 0.5f) {
			enemyRed = 255 - 255 * ((enemyHealthRatio - 0.5f) * 2);
		}
		// println("playerRed = " + playerRed + ", playerGreen = " + playerGreen, ",
		// health percentage = " + healthRatio);
		main.fill(playerRed, playerGreen, 0);
		main.rect(0, 0, Main.screenX * 0.1f, Main.screenY * healthRatio, Main.screenX * 0.05f); // Player's health bar
		main.fill(enemyRed, enemyGreen, 0);
		main.rect(Main.screenX * 0.9f, 0, Main.screenX * 0.1f, Main.screenY * enemyHealthRatio, Main.screenX * 0.05f);
		// 3. Draw energy bar
		// attacking reduces energy, clearing runes fills it
		main.fill(0, 0, 0); // background for the energy bar
		main.rect(gridLeftEdge, Main.screenY - (gridRightEdge - gridLeftEdge) - Main.screenX * 0.1f,
				gridRightEdge - gridLeftEdge, Main.screenX * 0.1f);
		main.fill(200, 200, 255); // draw the energy bar
		main.rect(gridLeftEdge, Main.screenY - (gridRightEdge - gridLeftEdge) - Main.screenX * 0.1f,
				(gridRightEdge - gridLeftEdge) * (Main.p.energy / Main.p.maxEnergy), Main.screenX * 0.1f,
				Main.screenX * 0.05f);
		// 4. Draw spellbox
		main.noFill();
		main.rect(gridLeftEdge + (runeSize * start), Main.screenY - (runeSize * boxHeight), runeSize * boxWidth,
				runeSize * boxHeight);
	}

	// There should always be a rune falling from the top of the screen. If there is
	// not, the game should create one
	public void generateRune() {
		rand = (int) main.random(Main.p.unlockedRunes.size()); // Generates a random rune based on what runes the player currently
														// has unlocked
		String label = Main.p.unlockedRunes.get(rand); // Check label to know what kind of rune to generate
		// Whatever the rune type, draws it at the top of the grid, and in the
		// horizontal center
		if (label.equals("Fire")) {
			currentRune = new FireRune("Fire", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge,
					(Main.screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize, main);
			// println("Created a new Fire Rune X =" + Main.screenX * 0.5 + ", y = 0.0, size
			// = " + runeSize);
		} else if (label.equals("Heal")) {
			currentRune = new HealRune("Heal", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge,
					(Main.screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize, main);
			// println("Created a new Healing Rune. X =" + Main.screenX * 0.5 + ", y = 0.0,
			// size = " + runeSize);
		} else if (label.equals("Slash")) {
			currentRune = new SlashRune("Slash", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge,
					(Main.screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize, main);
			// println("Created a new Slash Rune X =" + Main.screenX * 0.5 + ", y = 0.0,
			// size = " + runeSize);
		} else {
			currentRune = new FireRune("Fire", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge,
					(Main.screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize, main);
			// println("Label not recognized =" + Main.screenX * 0.5 + ", y = 0.0, size = "
			// + runeSize);
		}
		currentRune.speedY = 1;
		nextRune = false;
	}

	// This function will be used to track the position of the currentRune in terms
	// of the game grid, and look for collisions
	public void checkCollisions() {
		squareX = ((int) ((currentRune.x - gridLeftEdge) / runeSize)); // squareX and squareY represent the current
																		// rune's position in the rune grid
		squareY = ((int) ((Main.screenY - currentRune.y) / runeSize));
		// Floor represents a bottom edge that the rune should not go below
		float floor = Main.screenY;
		for (int i = gridSize - 1; i >= 0; i--) {
			if (!(r[i][squareX].type.equals("Null"))) {
				floor = Main.screenY - (runeSize * (i + 1));
				break;
			}
		}
		// left represents the leftmost edge that the rune should not pass
		float leftEdge = gridLeftEdge;
		for (int i = squareX; i >= 0; i--) {
			if (!(r[squareY][i].type.equals("Null"))) {
				leftEdge = gridLeftEdge + (runeSize * (i + 1));
				break;
			}
		}
		float rightEdge = gridRightEdge;
		for (int i = squareX; i < gridSize; i++) {
			if (!(r[squareY][i].type.equals("Null"))) {
				rightEdge = gridLeftEdge + (runeSize * i);
				break;
			}
		}
		// println("SquareX = " + squareX + ", SquareY = " + squareY + " X = " +
		// currentRune.x + ", Y = " + currentRune.y + ", floor = " + floor + ", leftEdge
		// = " + leftEdge);
		// Prevents the rune from going any lower than the bottom of the screen. If rune
		// gets that far, add it to the grid and create new rune
		if ((currentRune.y + (currentRune.size / 2) + currentRune.speedY) >= floor) {
			r[squareY][squareX] = currentRune;
			currentRune.x = (Main.screenX * 0.1f) + (runeSize / 2) + (runeSize * squareX);
			currentRune.y = (Main.screenY) - (runeSize / 2) - (runeSize * squareY);
			currentRune.speedX = 0;
			currentRune.speedY = 0;
			currentRune = new NullRune("Null", ((gridRightEdge - gridLeftEdge) / 2.0f) + gridLeftEdge,
					(Main.screenY) - (runeSize / 2.0f) - (runeSize * (gridSize - 1)), runeSize, main);
			nextRune = true;
			clearRunes(); // new rune added to the grid - clear any duplicates
		}
		// Prevents runes from moving beyond the left edge
		if ((currentRune.x - (runeSize / 2.0f) + currentRune.speedX) < leftEdge) {
			currentRune.x = leftEdge + (runeSize / 2.0f) - currentRune.speedX;
		}
		// prevents rune from moving beyond right edge
		if ((currentRune.x + (runeSize / 2.0f) + currentRune.speedX) > rightEdge) {
			currentRune.x = rightEdge - (runeSize / 2.0f) - currentRune.speedX;
		}
	}

	// This function will be called whenever a new rune is added to the grid. Will
	// clear away any connections, and lower the runes to their proper places.
	// Will also remove any runes in the upper areas of the grid and damage the
	// player for going too high. Finally, will clear runes in the spellbox if they
	// are not a proper combination
	// Every time a set of runes is cleared (except in the upper rows or the
	// spellbox), fill some of the player's action bar
	public void clearRunes() {
		// Loop until no runes are removed. Removing a rune will trigger the "runes
		// cleared" flag.
		boolean runesCleared = true;
		// first, check for vertical and horizontal chains. removing a chain may create
		// a new one, so loop until no new chains are created
		while (runesCleared) {
			runesCleared = false;
			boolean vert = checkVertical();
			boolean horiz = checkHorizontal();
			if ((vert == true) || (horiz == true))
				runesCleared = true;
		}
		// next, check the upper two rows. Runes here will damage the player
		checkUpperRows();
		// Finally, if there are runes in the spellbox that don't mix, remove them all.
		// For now, just assume that if they aren't the same type, they don't mix. Also,
		// cannot mix more than 2 of same type
		checkSpellbox();
	}

	// This function will check for any vertical chains
	public boolean checkVertical() {
		boolean runesCleared = false;
		// Secondly, clear all vertical segments
		for (int i = 0; i < gridSize - 2; i++) {
			for (int j = 0; j < gridSize; j++) // Don't go all the way to prevent going out of bounds.
			{
				if (!(r[i][j].type.equals("Null"))) {
					int chain = 1; // Keeps track of how long the chain is
					int k = i + 1; // K is a new variable to keep track of the position in the chain
					while ((k < gridSize) && (r[k][j].type.equals(r[i][j].type))) // If k is the same type as i, keep
																					// the chain going
					{
						chain++;
						k++;
					}
					if (chain > 2) // if the chain is at least 3, remove all runes in the chain.
					{
						runesCleared = true;
						for (int l = i; l < k; l++) // replace all the runes with null runes
						{
							r[l][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j),
									(Main.screenY) - (runeSize / 2) - (runeSize * l), runeSize, main);
						}
						for (int l = k; l < gridSize - 1; l++) {
							r[l - chain][j] = r[l][j]; // Drop the runes down to fill in the missing space
							animations.add(new RuneAnimation(r[l - chain][j], r[l - chain][j].x, r[l - chain][j].y, 0,
									1, 0, r[l - chain][j].y + runeSize * chain));
							// r[l - chain][j].y += runeSize * chain;
							// replace the missing rune
							r[l][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j),
									(Main.screenY) - (runeSize / 2) - (runeSize * l), runeSize, main);
						}
						Main.p.energy += chain; // Add cleared runes to the player's energy
						if (Main.p.energy > Main.p.maxEnergy) {
							Main.p.energy = Main.p.maxEnergy;
						}
					}
				}
			}
		}
		return runesCleared;
	}

	// This function will check for any horizontal chains
	public boolean checkHorizontal() {
		boolean runesCleared = false;
		// First, clear all horizontal segments
		// Loop through all runes, and if three (or more) connect in a row then clear
		// them all
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize - 2; j++) // Don't go all the way to prevent going out of bounds.
			{
				if (!(r[i][j].type.equals("Null"))) {
					int chain = 1; // Keeps track of how long the chain is
					int k = j + 1; // K is a new variable to keep track of the position in the chain
					while ((k < gridSize) && (r[i][k].type.equals(r[i][j].type))) // If k is the same type as J, keep
																					// the chain going
					{
						chain++;
						k++;
					}
					if (chain > 2) // if the chain is at least 3, remove all runes in the chain.
					{
						runesCleared = true;
						for (int l = j; l < k; l++) {
							// Clear the runes by lowering everything above them down 1
							for (int m = i; m < gridSize - 1; m++) // Go up to gridSize - 2 because the upper rows of
																	// the grid should remain empty
							{
								r[m][l] = r[m + 1][l]; // Replaces the selected rune with the rune above it
								animations.add(new RuneAnimation(r[m][l], r[m][l].x, r[m][l].y, 0, 1, 0,
										r[m][l].y + runeSize));
								r[m][l].y += runeSize;
								// Replace the removed rune with a null rune
								r[m + 1][l] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * l),
										(Main.screenY) - (runeSize / 2) - (runeSize * (m + 1)), runeSize, main);
							}
						}
						Main.p.energy += chain; // Add cleared runes to the player's energy
						if (Main.p.energy > Main.p.maxEnergy) {
							Main.p.energy = Main.p.maxEnergy;
						}
					}
				}
			}
		}
		return runesCleared;
	}

	// This function checks the top two rows of the rune grid for uncleared runes
	public void checkUpperRows() {
		for (int i = gridSize - 2; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (!r[i][j].type.equals("Null")) {
					r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j),
							(Main.screenY) - (runeSize / 2) - (runeSize * i), runeSize, main);
					Main.p.health -= 5;
				}
			}
		}
	}

	// This function will check runes in the spellbox, and remove them if they don't
	// mix.
	// For now, the spellbox will be a 3 * 2 box at the bottom center of the rune
	// grid. Runes don't mix if they are different types,
	// and grid currently can only hold 2 of the same type
	public void checkSpellbox() {
		String type = getSpellType();
		if (!type.equals("Null")) {
			int count = getSpellCount(type);
			if ((count == -1) || (count > maxNum)) {
				clearSpellBox();
			}
		}
	}

	// Returns the number of runes of a particular type in the spellBox
	public int getSpellCount(String type) {
		int count = 0;
		boolean clear = false;
		for (int i = 0; i < boxHeight; i++) {
			for (int j = start; j < end; j++) {
				if (r[i][j].type.equals(type)) {
					count++;
				} else if (!r[i][j].type.equals("Null")) {
					clear = true;
				}
			}
		}
		if (clear) {
			return -1;
		} else
			return count;
	}

	// This function returns the type of the first rune found in the spellBox
	public String getSpellType() {
		String type = "Null";
		for (int i = 0; i < boxHeight; i++) // First, figure out what kind of runes are in the spellbox
		{
			for (int j = start; j < end; j++) {
				if (!(r[i][j].type.equals("Null"))) {
					type = r[i][j].type;
					break;
				}
			}
		}
		return type;
	}

	// This function is called to clear away all runes in the spellBox;
	public void clearSpellBox() {
		for (int i = 0; i < boxHeight; i++) {
			for (int j = start; j < end; j++) {
				r[i][j] = new NullRune("Null", gridLeftEdge + (runeSize / 2.0f) + (runeSize * j),
						(Main.screenY) - (runeSize / 2) - (runeSize * i), runeSize, main);
			}
		}
	}

	public void castSpell() {
		String type = getSpellType();
		int count = getSpellCount(type);
		if ((count == 1) && (Main.p.energy >= 5)) {
			if (type.equals("Fire")) {
				e.burnNum += 2;
				Main.p.energy -= 5;
			} else if (type.equals("Slash")) {
				e.health -= 10;
				Main.p.energy -= 5;
			} else if (type.equals("Heal")) {
				Main.p.health += 10;
				if (Main.p.health > Main.p.maxHealth) {
					Main.p.health = Main.p.maxHealth;
				}
				Main.p.energy -= 5;
			}
			clearSpellBox();
		} else if ((count == 2) && (Main.p.energy >= 8)) {
			if (type.equals("Fire")) {
				e.burnNum += 5;
				Main.p.energy -= 8;
			} else if (type.equals("Slash")) {
				e.health -= 25;
				Main.p.energy -= 8;
			} else if (type.equals("Heal")) {
				Main.p.health += 25;
				if (Main.p.health > Main.p.maxHealth) {
					Main.p.health = Main.p.maxHealth;
				}
				Main.p.energy -= 8;
			}
			clearSpellBox();
		}
	}

	// This function will be used to draw all previous runes that have not been
	// cleared yet.
	public void drawRuneGrid() {
		for (Rune[] row : r) {
			for (Rune rune : row) {
				rune.drawRune();
			}
		}
	}

	public char mouseOverButton() {
		if (((main.mouseX > Main.screenX * 0.25f) && (main.mouseX < Main.screenX * 0.75f))
				&& ((main.mouseY > Main.screenY * 0.4f) && (main.mouseY < Main.screenY * 0.6f))) {
			return 'r';
		} else if (((main.mouseX > Main.screenX * 0.25f) && (main.mouseX < Main.screenX * 0.75f))
				&& ((main.mouseY > Main.screenY * 0.66f) && (main.mouseY < Main.screenY * 0.86f))) {
			return 'm';
		}

		else
			return ' ';
	}

	// This function can be used to check if the user pressed any of the on-screen
	// controls (to be implemented later).
	public void mouseClicked() {
		if (status == GameStatus.LOSE || status == GameStatus.WIN) {
			for(GameButton b : buttons)
			{
				if(b.isClicked(main.mouseX, main.mouseY))
				{
					return;
				}
			}
		}
	}


	public void keyPressed() {
		if (main.key == Main.CODED) {
			if (main.keyCode == Main.LEFT) {
				currentRune.speedX = -3;
			} else if (main.keyCode == Main.RIGHT) {
				currentRune.speedX = 3;
			} else if (main.keyCode == Main.DOWN) {
				currentRune.speedY = 3;
			}
		} else if (main.key == ' ') {
			castSpell();
		} else if ((main.key == 'p') || (main.key == 'P')) {
			if (status == GameStatus.ON) {
				status = GameStatus.ON; 
			} else if (status == GameStatus.PAUSED) {
				status = GameStatus.ON; 
			}
		}
	}

	public void keyReleased() {
		if (main.key == Main.CODED) {
			if (main.keyCode == Main.LEFT) {
				currentRune.speedX = 0;
			} else if (main.keyCode == Main.RIGHT) {
				currentRune.speedX = 0;
			} else if (main.keyCode == Main.DOWN) {
				currentRune.speedY = 1;
			}
		}
	}
}