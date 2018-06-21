package main;

import java.util.ArrayList;

/**This class will represent nodes in the tree. Nodes will have a "node status" of AVAILABLE, UNAVAILABLE, and UNLOCKED
 * Each node will also keep track of it's parent nodes (may have any number) and it's child nodes (may have any number) 
 * Finally, each node will have a name or ID 
 */
public class GameNode {
	private String name; 
	private ArrayList<GameNode> parentNodes;
	private ArrayList<GameNode> childNodes; 
	private NodeStatus status; 
	private UnlockType unlockType; 
	
	public NodeStatus getStatus() {
		return status;
	}

	public void setStatus(NodeStatus status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public ArrayList<GameNode> getParentNodes() {
		return parentNodes;
	}

	public ArrayList<GameNode> getChildNodes() {
		return childNodes;
	}

	GameNode(String name) {
		this.name = name; 
		parentNodes = new ArrayList<GameNode>(); 
		childNodes = new ArrayList<GameNode>(); 
		status = NodeStatus.UNAVAILABLE;
		setUnlockType(UnlockType.OR);
	}

	public void addParent(GameNode p) {
		parentNodes.add(p);
	}

	public void addChild(GameNode c) {
		childNodes.add(c);
	}

	public UnlockType getUnlockType() {
		return unlockType;
	}

	public void setUnlockType(UnlockType unlockType) {
		this.unlockType = unlockType;
	}

	public boolean clickable() {
		if(status == NodeStatus.UNAVAILABLE) {
			return false; 
		}
		else if((status == NodeStatus.AVAILABLE) || (status == NodeStatus.UNLOCKED)) {
			return true;
		}
		return false;
	}
}
