package IS_Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EightPuzzle {

	static List<Integer> goalState = new ArrayList<Integer>(9);
	static List<Integer> initialState = new ArrayList<Integer>(9);
	static int columnCount = 3;
	static int heuristicType;
	static PriorityQueue queue;
	static PriorityQueue visitedQueue;
	static int nodesGenerated = 1;
	static int nodesExpanded = 0;
	static int pathCost = 0;

	public static void main(String[] args) {
		System.out.println("Enter the initial state:\n");
		Scanner inputState = new Scanner(System.in);
		while (initialState.size() < 9) {
			initialState.add(inputState.nextInt());
		}

		System.out.println("Enter the goal state :\n");

		while (goalState.size() < 9) {
			goalState.add(inputState.nextInt());
		}

		System.out.println("Enter the heuristic type to be applied : \n" + "1 being misplaced heuristic \n"
				+ "2 being manhattan heuristic.");

		heuristicType = inputState.nextInt();

		System.out.println("Initial State : \n");
		int index = 0;
		while (index < initialState.size()) {
			for (int i = 0; i <= 2; i++) {
				System.out.print(initialState.get(index) + "\t");
				index++;
			}
			System.out.println("\n");
		}
		System.out.println("Goal State : \n");
		index = 0;
		while (index < goalState.size()) {
			for (int i = 0; i <= 2; i++) {
				System.out.print(goalState.get(index) + "\t");
				index++;
			}
			System.out.println("\n");
		}
		long start = System.currentTimeMillis();
		Node goalState = aStarSearch();
		System.out.println("Goal state has been reached. \n");
		printTracePath(goalState);
		System.out.println("Number of Nodes generated:" + nodesGenerated);
		System.out.println("Number of Nodes expanded :" + nodesExpanded);
		System.out.println("Time taken to reach the goal state :" + (System.currentTimeMillis() - start) + "ms");
	}

	/**
	 * This function implements A* algorithm to solve the 8 puzzle problem using
	 * misplaced/ manhattan heuristics. The step cost considered is 1.
	 * 
	 * @return - goalNode ( {@link Node} ) if goal has reached else null
	 */
	public static Node aStarSearch() {
		Node rootNode = new Node(initialState);
		queue = new PriorityQueue();
		visitedQueue = new PriorityQueue();
		int rootPriority = (heuristicType == 1) ? rootNode.calculateMisplacedTiles(initialState, goalState)
				: rootNode.calculateManhattanDistance(initialState, goalState);
		rootNode.setActualGoalCost(rootPriority);
		rootNode.setTotalPathCost(rootPriority);
		queue.push(rootNode, rootPriority);
		while (queue.size() > 0) {
			Node firstElement = (Node) queue.pop();
			nodesExpanded++;
			visitedQueue.push(firstElement, firstElement.getTotalPathCost());
			List<Integer> elementState = firstElement.getState();
			if (Utility.checkEquality(elementState, goalState)) {
				nodesExpanded--;
				return firstElement;
			}
			pathCost = firstElement.getPathCost() + 1;
			// explore the states
			firstElement.moveUp(pathCost);
			firstElement.moveDown(pathCost);
			firstElement.moveLeft(pathCost);
			firstElement.moveRight(pathCost);
			for (int childNodeIndex = 0; childNodeIndex < firstElement.getAdjacentNodes().size(); childNodeIndex++) {
				Node childNode = firstElement.getAdjacentNodes().get(childNodeIndex);
				int totalPathCost = childNode.getTotalPathCost();
				if (!queue.containState(childNode) && !visitedQueue.containState(childNode)) { 
					nodesGenerated++;
					queue.push(childNode, totalPathCost);
				}
			}
		}
		return null;
	}

	/**
	 * This function prints the path from the goal reached to the initial state
	 * along with the path cost
	 * 
	 * @param goalNode
	 */
	public static void printTracePath(Node goalNode) {
		if (goalNode.getParentNode() != null) {
			printTracePath(goalNode.getParentNode());
		}
		List<Integer> state = goalNode.getState();
		int index = 0;
		System.out.println("Path Cost (g(n)):"+goalNode.getPathCost());
		System.out.println("Goal Cost (h(n)):" +goalNode.getActualGoalCost());
		System.out.println("Total Path Cost (f(n)):" + goalNode.getTotalPathCost());
		System.out.println("Operator:" + goalNode.getMovement());
		System.out.println("[");
		while (index < state.size()) {
			for (int i = 0; i < columnCount; i++) {
				System.out.print(state.get(index) + "\t");
				index++;
			}

			System.out.print("\n");
		}
		System.out.println("]");
		System.out.println("********************");
		return;
	}

}
