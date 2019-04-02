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
		if(n.getStatus() == NodeStatus.AVAILABLE)
		{
			n.setStatus(NodeStatus.UNLOCKED);
			for(GameNode n2 : n.getChildNodes()) //Goes through every child of n to see if it is now available
			{
				if(n2.getStatus() == NodeStatus.UNAVAILABLE) { //only go through process if node is not available or unlocked
					if(n2.getUnlockType() == UnlockType.OR) {
						for(GameNode n3 : n2.getParentNodes())//check the child node's unlock type to see whether it will be available or not 
						{
							if(n3.getStatus() != NodeStatus.UNLOCKED)//If type is OR, only need one parent to be unlocked
							{
								n2.setStatus(NodeStatus.AVAILABLE);
								break;
							}
						}
					}
					else if(n2.getUnlockType() == UnlockType.AND) { //if unlockType is AND, need all parents to be unlocked 
						boolean unlocked = true; 
						for(GameNode n3 : n2.getParentNodes())
						{
							if(n3.getStatus() != NodeStatus.UNLOCKED)
							{
								unlocked = false;
							}
						}
						if(unlocked == true) {
							n2.setStatus(NodeStatus.AVAILABLE);
						}
					}
				}
			}
		}
	}
	
	public void addNode(String name, String[] parents, String[] children, NodeStatus status) 
	{
		if(findNode(name) != null) {
			GameNode n = new GameNode(name);
			for(String p : parents) {
				GameNode pNode = findNode(p);
				if(pNode == null) {
					return;
				}
				else {
					n.addParent(pNode);
				}
			}
			for(String c : children) {
				GameNode cNode = findNode(c);
				if(cNode == null) {
					return;
				}
				else {
					n.addChild(cNode);
				}
			}
			n.setStatus(status);
			nodes.add(n);
		}
	}

	public GameNode findNode(String name) {
		GameNode returnNode = null; 
		for(GameNode n: nodes) {
			if(n.getName().equals(name)) {
				returnNode = n; 
			}
		}
		return returnNode;
	}
}
