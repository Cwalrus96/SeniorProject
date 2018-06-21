package scenes;

import animations.SpriteAnimation;
import main.CharacterStatus;
import main.Main;
import main.Player;
import userInterface.ButtonAction;
import userInterface.GameButton;

/*This function is used to allow the player to choose the level that they want to play. Different levels 
 * will appear as nodes on the map, and new nodes will be unlocked as the player completes levels. 
 */
public class MapScene extends Scene {
	GameButton startSpot;
	GameButton level1;
	GameButton level2;
	GameButton bossLevel;
	SpriteAnimation characterSprite; 
	GameButton currentButton = null; 
	GameButton clickedButton = null;
	public MapScene(Main main) {
		super(main);
		startSpot = new GameButton(Main.screenX * 0.1f, Main.screenX * 0.8f, Main.screenX * 0.2f, Main.screenX * 0.2f,
				false, "Start", main.color(255, 255, 100), main.color(255, 255, 100), 0, 0, 25, Main.screenX * 0.1f,
				main);
		
		level1 = new GameButton(Main.screenX * 0.7f, Main.screenX * 0.8f, Main.screenX * 0.2f, Main.screenX * 0.2f,
				main.stages.findNode("Start").clickable(), "1", main.color(100, 255, 100), main.color(55, 255, 55), 0, 100, 25, Main.screenX * 0.1f, main);
		
		level2 = new GameButton(Main.screenX * 0.1f, Main.screenX * 0.2f, Main.screenX * 0.2f, Main.screenX * 0.2f,
				main.stages.findNode("Stage1A").clickable(), "2", main.color(100, 255, 100), main.color(55, 255, 55), 0, 100, 25, Main.screenX * 0.1f, main);
		
		bossLevel = new GameButton(Main.screenX * 0.65f, Main.screenX * 0.15f, Main.screenX * 0.3f, Main.screenX * 0.3f,
				main.stages.findNode("Stage1B").clickable(), "Boss", main.color(255, 100, 100), main.color(255, 55, 55), 0, 100, 25, Main.screenX * 0.15f,
				main);
		
		level1.action = new ButtonAction() {
			public void clickAction(Main main) {
				currentButton = level1; 
				clickedButton = level1; 
			}
		};
		
		level2.action = new ButtonAction() {
			public void clickAction(Main main) {
				currentButton = level2; 
				clickedButton = level2; 
			}
		};
		
		bossLevel.action = new ButtonAction() {
			public void clickAction(Main main) {
				currentButton = bossLevel; 
				clickedButton = bossLevel;
			}
		};
				
		Main.p.status = CharacterStatus.IDLE;
		if(Main.p.currentStage == 1)
		{
			currentButton = level1; 
		}
		else if(Main.p.currentStage == 2)
		{
			currentButton = level2;
		}
		else if(Main.p.currentStage == 3)
		{
			currentButton = bossLevel;
		}
		else
		{
			currentButton = startSpot;	
		}
		characterSprite = new SpriteAnimation(32 * 9, 32, currentButton.x, currentButton.y, 9, Main.p.getSpriteFile(), main);
	}

	@Override
	public void drawScene(long diff) {
		/**
		 * TODO - Draw map background Draw levels as nodes on the map 
		 * Draw connections between nodes 
		 * Draw player sprite (in "Idle" state) on the currently selected node 
		 * Keep track of which nodes have/haven't been unlocked.
		 */
		// 1. Draw Map background
		main.clear();
		main.background(255); // Replace with background image?
		// 3. Draw connections between nodes (moved in front so things get drawn in correct order
		main.fill(255, 200, 100);
		main.rect(Main.screenX * 0.15f, Main.screenX * 0.9f, Main.screenX * 0.1f, Main.screenX * (-0.6f));
		main.rect(Main.screenX * 0.75f, Main.screenX * 0.9f, Main.screenX * 0.1f, Main.screenX * (-0.6f));
		main.rect(Main.screenX * 0.2f, Main.screenX * 0.85f, Main.screenX * 0.6f, Main.screenX * 0.1f);
		main.rect(Main.screenX * 0.2f, Main.screenX * 0.25f, Main.screenX * 0.6f, Main.screenX * 0.1f);
		// 2. Draw nodes on the map - for now, nodes can be represented as round buttons
		// Want four nodes - starting node that doesn't do anything, two "normal" level
		// nodes, and 1 "boss" node
		startSpot.checkHover(main.mouseX, main.mouseY);
		startSpot.drawButton();
		level1.checkHover(main.mouseX, main.mouseY);
		level1.drawButton();
		level2.checkHover(main.mouseX, main.mouseY);
		level2.drawButton();
		bossLevel.checkHover(main.mouseX, main.mouseY);
		bossLevel.drawButton();
		//4 Draw the character, and move them to the clicked button;
		float xDistance = characterSprite.x - currentButton.x;
		float yDistance = characterSprite.y - currentButton.y;
		if((Math.abs(xDistance) > 2) || (Math.abs(yDistance) > 2))
		{
			float magnitude = (float) Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
			characterSprite.y -= 2.0f * (yDistance / magnitude); 
			characterSprite.x -= 2.0f * (xDistance / magnitude);	
		}
		else if(clickedButton != null)
		{
			if(currentButton == level1)
			{
				Main.p.currentStage = 1;
			}
			else if(currentButton == level2)
			{
				Main.p.currentStage = 2; 
			}
			else if(currentButton == bossLevel)
			{
				Main.p.currentStage = 3;
			}
			if(clickedButton == level1 || clickedButton == level2)
			{
				Main.s = new DemoGameScene(main, Main.p); 
			}
			else if(clickedButton == bossLevel)
			{
				Main.s = new BossLevel1(main);
			}
		}
		//characterSprite.x = currentButton.x;
		//characterSprite.y = currentButton.y;
		characterSprite.animate(diff);
	}
	
	//This function will be called when the player clicks a button
	public void mouseClicked() {
		if (level1.isClicked(main.mouseX, main.mouseY))
		{
			return;
		}
		// Check if mouse is over the "Create Character" button
		else if (level2.isClicked(main.mouseX, main.mouseY))
		{
			return;
		}
		else if(bossLevel.isClicked(main.mouseX, main.mouseY))
		{
			return;
		}
	}

}
