package IS_Project2;

import java.util.Scanner;

public class RandomRestartWithSidewaysHC extends SidewaysHC {

	/**
	 * Hill climbing with random restart is been implemented allowing sideways move.
	 * 
	 */
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
			int totalNumberOfRestarts = 0;
			int totalDepthForFailureState = 0;
			System.out.println("Iteration Number :" + outerIterationLoop);
			for (int innerIterationLoop = 0; innerIterationLoop <= outerIterationLoop; innerIterationLoop++) {
				Node goalState = null;
				do {
					initialState = getRandomConfiguration();
					initialSidewaysCount = 0;
					queue = new PriorityQueue();
					goalState = sidewaysHillClimbSearch(new Node(initialState));
					totalNumberOfRestarts++;
					totalDepthForFailureState = totalDepthForFailureState + goalState.getDepth();
				} while (goalState.getPathCost() != 0);
			}
			float averageNumberOfRestarts = ((float) totalNumberOfRestarts) / outerIterationLoop;
			float averageDepthOfFailureState = ((float) totalDepthForFailureState / outerIterationLoop);
			System.out.println("Average number of Random restarts :" + averageNumberOfRestarts);
			System.out.println("Average number of steps :" + averageDepthOfFailureState);
			System.out.println("**************************************");
		}
	}
}
