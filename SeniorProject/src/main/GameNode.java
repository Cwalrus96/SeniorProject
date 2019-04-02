package main;

import java.util.ArrayList;

/**This class will represent nodes in the tree. Nodes will have a "node status" of AVAILABLE, UNAVAILABLE, and UNLOCKED
 * Each node will also keep track of it's parent nodes (may have any number) and it's child nodes (may have any number) 
 * Finally, each node will have a name or ID 
 */
public class GameNode {
	String name; 
	ArrayList<GameNode> parentNodes;
	ArrayList<GameNode> childNodes; 
	NodeStatus status; 
}
