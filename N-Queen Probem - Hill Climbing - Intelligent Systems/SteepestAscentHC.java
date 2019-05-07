package IS_Project2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SteepestAscentHC {

	static int boardSize;
	static List<Integer> initialState = new ArrayList<Integer>(boardSize*boardSize);
	static int iterationNumber = 300;
	static int iterationLimit = 1000;
	static PriorityQueue queue = null;

	public static void main(String[] args) {
		System.out.println("Enter the size of the N Queen problem : \n");
		Scanner inputState = new Scanner(System.in);
		boardSize = inputState.nextInt();
		if (boardSize < 4) {
			System.out.println("N Queen problem cannot be solved for the size: " + boardSize);
			return;
		}
		for (int outerIterationLoop = iterationNumber; outerIterationLoop <= iterationLimit; outerIterationLoop = outerIterationLoop + 100) {
			int totalSuccessCount = 0;
			int totalDepthForSuccessfulState = 0;
			int totalDepthForFailureState = 0;
			System.out.println("Iteration Number : " + outerIterationLoop);
			for (int innerIterationLoop = 0; innerIterationLoop < outerIterationLoop; innerIterationLoop++) {
				initialState = getRandomConfiguration();
//			System.out.println("Initial State : \n");
//			int index = 0;
//			System.out.print("[");
//			while (index < initialState.size()) {
//				for (int i = 0; i < boardSize; i++) {
//					System.out.print(initialState.get(index) + "\t");
//					index++;
//				}
//				System.out.println("\n");
//			}
//			System.out.print("]");
				queue = new PriorityQueue();
				Node goalNode = steepestAscentHillClimbSearch(new Node(initialState));
				if (goalNode.getPathCost() == 0) {
					totalSuccessCount++;
					totalDepthForSuccessfulState = totalDepthForSuccessfulState + goalNode.getDepth();
				} else {
					totalDepthForFailureState = totalDepthForFailureState + goalNode.getDepth();
				}
//			System.out.println("Goal Reached : " + isGoal);
//			printTracePath(goalNode);
//			System.out.println("************************************************");
			}
			System.out.println("Number: " + totalSuccessCount);
			float successRate = ((float) totalSuccessCount / outerIterationLoop) * 100;
			float averageDepthOfSuccessfulState = ((float) totalDepthForSuccessfulState / totalSuccessCount);
			float averageDepthOfFailureState = ((float) totalDepthForFailureState / (outerIterationLoop - totalSuccessCount));
			System.out.println("Success Rate :" + successRate);
			System.out.println("Failure Rate:" + (100 - successRate));
			System.out.println("Average steps - sucess:" + averageDepthOfSuccessfulState);
			System.out.println("Average steps - fail :" + averageDepthOfFailureState);
			System.out.println("***************************************");
		}
	}

	/**
	 * The function implements steepest ascent hill climbing algorithm by choosing
	 * the best successor.
	 * 
	 * @param initialState
	 * @return
	 */
	public static Node steepestAscentHillClimbSearch(Node initialState) {
		int pathCost = initialState.calculateStateHeuristic(initialState.getState());
		initialState.setPathCost(pathCost);
		initialState.generateAdjacentNodes();
		for (int adjacentNodeindex = 0; adjacentNodeindex < initialState.getAdjacentNodes().size(); adjacentNodeindex++) {
			Node adjacentNode = initialState.getAdjacentNodes().get(adjacentNodeindex);
			queue.push(adjacentNode, adjacentNode.getPathCost());
		}
		Node bestSuccessor = (Node) queue.pop();
		if (initialState.getPathCost() <= bestSuccessor.getPathCost()) {
			return initialState;
		}
		return steepestAscentHillClimbSearch(bestSuccessor);
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
		List<Integer> goalState = goalNode.getState();
		int index = 0;
		System.out.println("Path Cost (h(n)):" + goalNode.getPathCost());
		System.out.println("Depth d :" + goalNode.getDepth());
		System.out.println("[");
		while (index < goalState.size()) {
			for (int i = 0; i < boardSize; i++) {
				System.out.print(goalState.get(index) + "\t");
				index++;
			}

			System.out.print("\n");
		}
		System.out.println("]");
		System.out.println("********************");
		return;
	}

	/**
	 * The function generates a random initial state.
	 * 
	 * @return
	 */
	public static List<Integer> getRandomConfiguration() {
		List<Integer> randomColumnList = Arrays
				.stream(IntStream.generate(() -> new Random().nextInt(boardSize)).limit(boardSize).toArray()).boxed()
				.collect(Collectors.toList());
		int randomListindex = 0;
		List<Integer> initialState = IntStream.of(new int[boardSize * boardSize]).boxed().collect(Collectors.toList());
		while (randomListindex < boardSize) {
			int randomIndexTobeSet = randomColumnList.get(randomListindex);
			int tempRandomListIndex = randomListindex;
			for (int colIndex = 0; colIndex < boardSize; colIndex++) {
				tempRandomListIndex = (colIndex == 0) ? tempRandomListIndex : tempRandomListIndex + boardSize;
				if (colIndex == randomIndexTobeSet) {
					initialState.set(tempRandomListIndex, 1);
				}
			}
			randomListindex++;
		}
		return initialState;
	}

}
