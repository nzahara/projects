package IS_Project2;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private List<Integer> state;
	private int pathCost;
	private Node parentNode;
	private List<Node> adjacentNodes;
	private int depth;

	Node(List<Integer> state) {
		this.state = state;
	}

	Node(Node parentNode, List<Integer> state, int pathCost, int depth) {
		this.parentNode = parentNode;
		this.state = state;
		this.pathCost = pathCost;
		this.depth = depth;
	}

	public List<Integer> getState() {
		return state;
	}

	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}

	public List<Node> getAdjacentNodes() {
		if (adjacentNodes == null) {
			return new ArrayList<Node>();
		}
		return adjacentNodes;
	}

	public void setAdjacentNodes(List<Node> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public int getDepth() {
		return depth;
	}

	/**
	 * The function generates the child nodes for the given rootnode by moving the
	 * queens up and down columnwise.
	 */
	public void generateAdjacentNodes() {
		List<Integer> parentState = this.getState();
		int boardIndex = 0;
		while (boardIndex < SteepestAscentHC.boardSize) {
			int tempIndex = boardIndex;
			List<Integer> tempParentState = new ArrayList<Integer>(parentState);
			int tempColIndex = boardIndex;
			int currentQueenIndex = getQueenIndexToBeMoved(tempParentState, tempColIndex);
			int depth = this.getDepth() + 1;
			for (int colIndex = 0; colIndex < SteepestAscentHC.boardSize; colIndex++) {
				tempIndex = (colIndex == 0) ? tempIndex : tempIndex + SteepestAscentHC.boardSize;
				if (tempIndex != currentQueenIndex) {
					List<Integer> adjacentState = new ArrayList<Integer>(tempParentState);
					adjacentState.set(tempIndex, 1);
					Node newNode = new Node(this, adjacentState, calculateStateHeuristic(adjacentState), depth);
					List<Node> adjacentNodes = this.getAdjacentNodes();
					adjacentNodes.add(newNode);
					this.setAdjacentNodes(adjacentNodes);
				}
			}
			boardIndex++;
		}
	}

	/**
	 * The function gives the index of the queen to be moved up and down to generate
	 * child nodes.
	 * 
	 * @param tempParentState
	 * @param colIndex
	 * @return
	 */
	private int getQueenIndexToBeMoved(List<Integer> tempParentState, int colIndex) {
		int tempIndex = colIndex;
		int counter = 0;
		List<Integer> currentState = this.getState();
		while (counter < SteepestAscentHC.boardSize) {
			if (currentState.get(tempIndex) == 1) {
				tempParentState.set(tempIndex, 0);
				return tempIndex;
			}
			tempIndex = tempIndex + SteepestAscentHC.boardSize;
			counter++;
		}
		return -1;
	}

	/**
	 * The function generates the heuristic value for each state i.e. the number of
	 * queens getting eachother.
	 * 
	 * @param state
	 * @return
	 */
	public int calculateStateHeuristic(List<Integer> state) {
		int boardIndex = 0;
		int heuristicCount = 0;
		int boardSize = SteepestAscentHC.boardSize;
		while (boardIndex < boardSize * boardSize) {
			boardIndex = this.getQueenIndexForTheState(state, boardIndex);
			if (boardIndex < 0) {
				return heuristicCount;
			}
			int tempBoardIndex = boardIndex + 1;
			int upperDiagIndex = boardIndex;
			int lowerDiagIndex = boardIndex;
			while (((tempBoardIndex - 1) % boardSize) < (boardSize - 1)) {
				upperDiagIndex = upperDiagIndex - boardSize + 1;
				lowerDiagIndex = lowerDiagIndex + boardSize + 1;
				if (state.get(tempBoardIndex) == 1) {
					heuristicCount++;
				}
				if (upperDiagIndex > 0 && state.get(upperDiagIndex) == 1) {
					heuristicCount++;
				}
				if (lowerDiagIndex < (boardSize * boardSize) && state.get(lowerDiagIndex) == 1) {
					heuristicCount++;
				}
				tempBoardIndex++;
			}
			boardIndex++;
		}
		return heuristicCount;
	}

	/**
	 * The function checks if the queen is present in the given state at the given
	 * index. If yes, returns the index else returns the index where the queen is
	 * actually present.
	 * 
	 * @param state
	 * @param givenIndex
	 * @return
	 */
	private int getQueenIndexForTheState(List<Integer> state, int givenIndex) {
		int boardSize = SteepestAscentHC.boardSize;
		while (givenIndex < (boardSize * boardSize)) {
			if (state.get(givenIndex) == 1) {
				return givenIndex;
			}
			givenIndex++;
		}
		return -1;
	}
}
