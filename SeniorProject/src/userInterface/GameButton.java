package userInterface;

import processing.core.PImage;
import seniorproject.Main;

//This class will be used anytime there is a clickable button on the screen 


public class GameButton {
	float x; //The x coordinate of the button (upper left corner) 
	float y; //the y coordinate of the button (upper left corner) 
	float width;
	float height; 
	public boolean clickable; //Determines whether the button can currently be pressed or not
	String name; 
	String text; 
	int buttonColor;
	int buttonHover;
	int textColor; 
	int unclickableText; 
	int textSize; 
	PImage buttonImage;
	PImage unclickableImage;
	float edgeRadius; //Controls radius of rounded corners when drawing button
	ButtonAction action; 
	Main main; 
	boolean hovering = false; 
	
	//Two types of buttons (for now). One that uses an image, another that simply displays text
	
	//First constructor will be for an image 
	public GameButton(float x, float y, float width, float height, 
			boolean clickable, PImage buttonImage, PImage unclickableImage,ButtonAction action, Main main)
	{
		this.x = x; 
		this.y = y; 
		this.width = width; 
		this.height = height; 
		this.clickable = clickable; 
		this.buttonImage = buttonImage; 
		this.unclickableImage = unclickableImage; 
		this.main = main;
	}
	
	
	//Second constructor will include text and colors 
	public GameButton(float x, float y, float width, float height, 
			boolean clickable, String text,int buttonColor, int buttonHover,
			int textColor, int unclickableText, int textSize,  Main main)
	{
		this.x = x; 
		this.y = y; 
		this.width = width; 
		this.height = height; 
		this.clickable = clickable;
		this.text = text; 
		this.buttonColor = buttonColor; 
		this.buttonHover = buttonHover; 
		this.buttonImage = null; 
		this.unclickableImage = null; 
		edgeRadius = 0; 
		this.textColor = textColor; 
		this.unclickableText = unclickableText; 
		this.textSize = textSize; 
		this.main = main;
	}
	
	//Third constructor is like above, but includes a value for the edge radius
	public GameButton(float x, float y, float width, float height, 
			boolean clickable, String text,int buttonColor, int buttoHover,
			int textColor, int unclickableText, int textSize, float edgeRadius, Main main)
	{
		this.x = x; 
		this.y = y; 
		this.width = width; 
		this.height = height; 
		this.clickable = clickable;
		this.text = text; 
		this.buttonColor = buttonColor; 
		this.buttonHover = buttoHover; 
		this.buttonImage = null; 
		this.unclickableImage = null; 
		this.edgeRadius = edgeRadius; 
		this.textColor = textColor; 
		this.unclickableText = unclickableText; 
		this.textSize = textSize;
		this.main = main; 
	}
	
	/*
	 * This function checks if the button has been clicked by comparing the 
	 *coordinates of the mouse to the button position, and if so performs the button action
	*/
	public boolean isClicked(float mouseX, float mouseY)
	{
		if ((clickable) && ((mouseX > x) && (mouseX < (x + width)))
				&& ((mouseY > y) && (mouseY < (y + height)))) {
			action.clickAction(main); 
			return true;
		}
		else return false; 
	}
	
	/*
	 * This function checks if mouse is hovering over the button
	 */
	public void checkHover(float mouseX, float mouseY)
	{
		if (((mouseX > x) && (mouseX < (x + width)))
				&& ((mouseY > y) && (mouseY < (y + height)))) { 
			hovering = true;
		}
		else hovering = false; 
	}
	
	/*
	 * This function draws the button to the screen
	 */
	public void drawButton()
	{
		if(buttonImage != null)
		{
			if(clickable)
			{
				main.image(buttonImage, x, y, width, height); 
			}
			else
			{
				main.image(unclickableImage,  x,  y, width, height);
			}
		}
		else
		{
			if(hovering)
			{
				main.fill(buttonHover);
				main.rect(x, y, width, height, edgeRadius);
			}
			else
			{
				main.fill(buttonColor);
				main.rect(x, y, width, height, edgeRadius);
			}
			if(clickable)
			{
				
				main.fill(textColor);
				main.textAlign(Main.CENTER, Main.CENTER); 
				main.text(text, x + (width / 2.0f), y + (height / 2.0f));
			}
			else
			{
				main.fill(unclickableText);
				main.textAlign(Main.CENTER, Main.CENTER); 
				main.text(text, x + (width / 2.0f), y + (height / 2.0f));
			}
		}
	}

}
