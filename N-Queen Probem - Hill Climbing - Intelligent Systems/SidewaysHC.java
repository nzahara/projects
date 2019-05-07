package IS_Project2;

import java.util.Scanner;

public class SidewaysHC extends SteepestAscentHC {

	static int sidewaysCountLimit = 100;
	static int initialSidewaysCount = 0;
	static PriorityQueue queue = null;

	public static void main(String[] args) {
		System.out.println("Enter the size of the N Queen problem : \n");
		Scanner inputState = new Scanner(System.in);
		boardSize = inputState.nextInt();
		if (boardSize < 4) {
			System.out.println("N Queen problem cannot be solved for the size: " + boardSize);
			return;
		}
		for (int outerIterationLoop = iterationNumber; outerIterationLoop <= iterationLimit; outerIterationLoop = outerIterationLoop
				+ 100) {
			int totalSuccessCount = 0;
			int totalDepthForSuccessfulState = 0;
			int totalDepthForFailureState = 0;
			System.out.println("Iteration Number : " + outerIterationLoop);
			for (int innerIterationLoop = 0; innerIterationLoop < outerIterationLoop; innerIterationLoop++) {
				initialState = getRandomConfiguration();
//				System.out.println("Initial State : \n");
//				int index = 0;
//				System.out.print("[");
//				while (index < initialState.size()) {
//					for (int i = 0; i < boardSize; i++) {
//						System.out.print(initialState.get(index) + "\t");
//						index++;
//					}
//					System.out.println("\n");
//				}
//				System.out.print("]");
				initialSidewaysCount = 0;
				queue = new PriorityQueue();
				Node goalNode = sidewaysHillClimbSearch(new Node(initialState));
				// System.out.println("Goal Reached :" + isGoal);
				if (goalNode.getPathCost() == 0) {
					totalSuccessCount ++ ;
					totalDepthForSuccessfulState = totalDepthForSuccessfulState + goalNode.getDepth();
				} else {
					totalDepthForFailureState = totalDepthForFailureState + goalNode.getDepth();
				}
				// printTracePath(goalNode);
			}
			float successRate = ((float) totalSuccessCount / outerIterationLoop) * 100;
			float averageDepthOfSuccessfulState = ((float) totalDepthForSuccessfulState / totalSuccessCount);
			float averageDepthOfFailureState = ((float) totalDepthForFailureState
					/ (outerIterationLoop - totalSuccessCount));
			System.out.println("Success Rate :" + successRate);
			System.out.println("Failure Rate:" + (100 - successRate));
			System.out.println("Average steps - sucess:" + averageDepthOfSuccessfulState);
			System.out.println("Average steps - fail :" + averageDepthOfFailureState);
			System.out.println("***************************************");
		}
	}

	/**
	 * The function implements hill climbing with sideways move upto 100 steps.
	 * 
	 * @param initialState
	 * @return
	 */
	public static Node sidewaysHillClimbSearch(Node initialState) {
		int pathCost = initialState.calculateStateHeuristic(initialState.getState());
		initialState.setPathCost(pathCost);
		initialState.generateAdjacentNodes();
		for (int index = 0; index < initialState.getAdjacentNodes().size(); index++) {
			Node adjacentNode = initialState.getAdjacentNodes().get(index);
			if (!checkForDuplicateStates(adjacentNode, initialState)) {
				queue.push(adjacentNode, adjacentNode.getPathCost());
			}
		}
		Node highestPriorityNode = (Node) queue.pop();
		if ((initialSidewaysCount > sidewaysCountLimit)
				&& (initialState.getPathCost() == highestPriorityNode.getPathCost())) {
			return highestPriorityNode;
		}
		if ((initialSidewaysCount <= sidewaysCountLimit)
				&& (initialState.getPathCost() == highestPriorityNode.getPathCost())) {
			initialSidewaysCount++;
			return sidewaysHillClimbSearch(highestPriorityNode);
		}
		if (initialState.getPathCost() < highestPriorityNode.getPathCost()) {
			return initialState;
		}
		initialSidewaysCount = 0;
		return sidewaysHillClimbSearch(highestPriorityNode);
	}

	/**
	 * The function checks if the current Node was already generated previously.
	 * 
	 * @param currentNode
	 * @param parentNode
	 * @return
	 */
	private static boolean checkForDuplicateStates(Node currentNode, Node parentNode) {
		if (parentNode == null) {
			return false;
		}
		if (currentNode.getState().equals(parentNode.getState())) {
			return true;
		}
		return checkForDuplicateStates(currentNode, parentNode.getParentNode());
	}

}
