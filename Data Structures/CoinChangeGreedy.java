package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CoinChangeGreedy {

	static Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>() {
		{
			put(1, 0);
			put(5, 0);
			put(10, 0);
			put(25, 0);
		}

		@Override
		public String toString() {
			return "Pennies:" + resultMap.get(1) + "\n" + "Dimes:" + resultMap.get(10) + "\n" + "Nickels:"
					+ resultMap.get(5) + "\n" + "Quarters:" + resultMap.get(25);
		}
	};

	static int centValue = 0;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the cent value:\n");
		centValue = input.nextInt();
		int[][] dynamicArray = new int[5][centValue + 1];
		// quarters , dimes,nickels,pennies
		Map<Integer, Integer> resultMap = MinCoinChangeUsingGreedyAlg(centValue);
		System.out.println("Minimum Coins Required for " + centValue + ": \n" + resultMap);
	}

	/**
	 * The function gives minimum coin change using greedy algorithm.
	 * 
	 * @param centValue
	 * @return
	 */
	public static Map<Integer, Integer> MinCoinChangeUsingGreedyAlg(int centValue) {
		int[] denominationArr = { 1, 5, 10, 25 };
		for (int i = denominationArr.length - 1; i >= 0; i--) {
			int coinDenomination = denominationArr[i];
			while (centValue >= coinDenomination) {
				int coinDenominationCounter = resultMap.get(coinDenomination);
				coinDenominationCounter++;
				resultMap.put(coinDenomination, coinDenominationCounter);
				centValue = centValue - coinDenomination;
			}
		}

		return resultMap;
	}

}
