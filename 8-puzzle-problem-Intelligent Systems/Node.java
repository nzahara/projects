package IS_Project;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private List<Integer> state;
	private Node parentNode;
	private int pathCost;
	private int actualGoalCost;
	private int totalPathCost;
	private String movement = "None";
	private List<Node> adjacentNodes;

	Node(List<Integer> state) {
		this.state = state;
	}

	Node(Node parentNode, List<Integer> state, int pathCost, int actualGoalCost,String movement) {
		this.parentNode = parentNode;
		this.state = state;
		this.pathCost = pathCost;
		this.actualGoalCost = actualGoalCost;
		this.totalPathCost = this.totalPathCost + pathCost + actualGoalCost;
		this.movement = movement;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}

	public int getActualGoalCost() {
		return actualGoalCost;
	}

	public void setActualGoalCost(int actualGoalCost) {
		this.actualGoalCost = actualGoalCost;
	}

	public int getTotalPathCost() {
		return totalPathCost;
	}

	public void setTotalPathCost(int totalPathCost) {
		this.totalPathCost = totalPathCost;
	}

	public List<Integer> getState() {
		return state;
	}

	public List<Node> getAdjacentNodes() {

		if (this.adjacentNodes == null) {
			return new ArrayList<Node>();
		}
		return this.adjacentNodes;
	}

	public void setAdjacentNodes(List<Node> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public String getMovement() {
		return movement;
	}

	/**
	 * The function checks if there is a right move that is possible. If yes, moves
	 * the blank space /0 to right.
	 * 
	 * @param pathCost
	 */
	public void moveRight(int pathCost) {
		List<Integer> exploringNodeState = this.getState();
		List<Integer> tempState = new ArrayList<>(exploringNodeState);
		int blankIndex = Utility.findIndex(exploringNodeState, 0);
		if ((blankIndex % EightPuzzle.columnCount) >= 2) {
			return;
		}
		Integer elementToBeMoved = tempState.get(blankIndex + 1);
		tempState.set(blankIndex, elementToBeMoved);
		tempState.set(blankIndex + 1, 0);
		int edgeCost = (EightPuzzle.heuristicType == 1) ? calculateMisplacedTiles(tempState, EightPuzzle.goalState)
				: calculateManhattanDistance(tempState, EightPuzzle.goalState);
		List<Node> adjacentNodes = this.getAdjacentNodes();
		adjacentNodes.add(new Node(this, tempState, pathCost, edgeCost,"right"));
		this.setAdjacentNodes(adjacentNodes);
	}

	/**
	 * The function checks if there is a left move that is possible. If yes, moves
	 * the blank space /0 to left.
	 * 
	 * @param pathCost
	 */
	public void moveLeft(int pathCost) {
		List<Integer> exploringNodeState = this.getState();
		List<Integer> tempState = new ArrayList<>(exploringNodeState);
		int blankIndex = Utility.findIndex(exploringNodeState, 0);
		if ((blankIndex % EightPuzzle.columnCount) <= 0) {
			return;
		}
		Integer elementToBeMoved = tempState.get(blankIndex - 1);
		tempState.set(blankIndex, elementToBeMoved);
		tempState.set(blankIndex - 1, 0);
		int edgeCost = (EightPuzzle.heuristicType == 1) ? calculateMisplacedTiles(tempState, EightPuzzle.goalState)
				: calculateManhattanDistance(tempState, EightPuzzle.goalState);
		List<Node> adjacentNodes = this.getAdjacentNodes();
		adjacentNodes.add(new Node(this, tempState, pathCost, edgeCost,"left"));
		this.setAdjacentNodes(adjacentNodes);
	}

	/**
	 * The function checks if there is a up move that is possible. If yes, moves the
	 * blank space /0 to up.
	 * 
	 * @param pathCost
	 */
	public void moveUp(int pathCost) {
		List<Integer> exploringNodeState = this.getState();
		List<Integer> tempState = new ArrayList<>(exploringNodeState);
		int blankIndex = Utility.findIndex(exploringNodeState, 0);
		if ((blankIndex - EightPuzzle.columnCount) < 0) {
			return;
		}
		Integer elementToBeMoved = tempState.get(blankIndex - 3);
		tempState.set(blankIndex, elementToBeMoved);
		tempState.set(blankIndex - EightPuzzle.columnCount, 0);
		int edgeCost = (EightPuzzle.heuristicType == 1) ? calculateMisplacedTiles(tempState, EightPuzzle.goalState)
				: calculateManhattanDistance(tempState, EightPuzzle.goalState);
		List<Node> adjacentNodes = this.getAdjacentNodes();
		adjacentNodes.add(new Node(this, tempState, pathCost, edgeCost,"up"));
		this.setAdjacentNodes(adjacentNodes);
	}

	/**
	 * The function checks if there is a down move that is possible. If yes, moves
	 * the blank space /0 to down.
	 * 
	 * @param pathCost
	 */
	public void moveDown(int pathCost) {
		List<Integer> exploringNodeState = this.getState();
		List<Integer> tempState = new ArrayList<>(exploringNodeState);
		int blankIndex = Utility.findIndex(exploringNodeState, 0);
		if ((blankIndex + EightPuzzle.columnCount) >= exploringNodeState.size()) {
			return;
		}
		Integer elementToBeMoved = tempState.get(blankIndex + 3);
		tempState.set(blankIndex, elementToBeMoved);
		tempState.set(blankIndex + EightPuzzle.columnCount, 0);
		int edgeCost = (EightPuzzle.heuristicType == 1) ? calculateMisplacedTiles(tempState, EightPuzzle.goalState)
				: calculateManhattanDistance(tempState, EightPuzzle.goalState);
		List<Node> adjacentNodes = this.getAdjacentNodes();
		adjacentNodes.add(new Node(this, tempState, pathCost, edgeCost,"down"));
		this.setAdjacentNodes(adjacentNodes);
	}

	/**
	 * The function calculates the number of mismatched tiles of current state as
	 * compared to that of goal state excluding blank space/0.
	 * 
	 * @param currentState
	 * @param goalState
	 * @return
	 */
	public int calculateMisplacedTiles(List<Integer> currentState, List<Integer> goalState) {
		int edgeCost = 0;
		for (int index = 0; index < currentState.size(); index++) {
			if(currentState.get(index).equals(0)) {
				continue;
			}
			if (!currentState.get(index).equals(goalState.get(index))) {
				edgeCost++;
			}
		}
		return edgeCost;
	}

	/**
	 * This function finds the manhattan distance of the current state with respect
	 * to the goal state.
	 * 
	 * 
	 * @param currentState
	 * @param goalState
	 * @return
	 */
	public int calculateManhattanDistance(List<Integer> currentState, List<Integer> goalState) {
		int edgeCost = 0;
		for (int index = 0; index < currentState.size(); index++) {
			int manhattanDistance = 0;
			Integer currentStateElement = currentState.get(index);
			Integer goalStateElement = goalState.get(index);
			if (currentStateElement.equals(0)) {
				continue;
			}
			if (!currentStateElement.equals(goalStateElement)) {
				int goalStateElementIndex = Utility.findIndex(goalState, currentStateElement);
				int currentStatexCo = index / (EightPuzzle.columnCount);
				int currentStateyCo = index % EightPuzzle.columnCount;
				int goalStatexCo = goalStateElementIndex / (EightPuzzle.columnCount);
				int goalStateyCo = goalStateElementIndex % EightPuzzle.columnCount;

				manhattanDistance = Math.abs((goalStatexCo - currentStatexCo))
						+ Math.abs(goalStateyCo - currentStateyCo);
				edgeCost = edgeCost + manhattanDistance;
			}
		}
		return edgeCost;
	}

}
