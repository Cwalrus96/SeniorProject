package main;

import java.util.ArrayList;

public class GameTree {
	ArrayList<GameNode> nodes;
	
	/*This function will be called when an element (either a skill or level) is unlocked. 
	* sets any applicable child nodes to "available", and sets the nodes status to "unlocked" 
	* (if it is currently available)
	*/
	public void unlockNode(GameNode n) 
	{
		if(n.status == NodeStatus.AVAILABLE)
		{
			n.status = NodeStatus.UNLOCKED;
			for(GameNode n2 : n.childNodes) //Goes through every child of n to see if it is now available
			{
				if(n2.status == NodeStatus.UNAVAILABLE) { //only go through process if node is not available or unlocked
					boolean unlocked = true;
					for(GameNode n3 : n2.parentNodes)//child is only available if all parent nodes have been unlocked 
					{
						if(n3.status != NodeStatus.UNLOCKED)
						{
							unlocked = false;
						}
					}
					if(unlocked == true) {
						n2.status = NodeStatus.AVAILABLE;
					}
				}
			}
		}
	}
	
	
	//CONTINUE WORKING ON THIS FUNCTION
	public void addNode(String name, String[] parents, String[] children, NodeStatus status) 
	{
		
	}
}
