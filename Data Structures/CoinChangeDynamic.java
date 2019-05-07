package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CoinChangeDynamic {

	static HashMap<Integer, Integer> indexCoinMap = new HashMap<Integer, Integer>() {
		{
			put(1, 1);
			put(2, 5);
			put(3, 10);
			put(4, 25);
		}
	};

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
		int[][] dynamicArray = new int[resultMap.size()+1][centValue + 1];
		// quarters , dimes,nickels,pennies
		Map<Integer, Integer> resultMap = getMinimumCoinChange(dynamicArray, centValue);
		System.out.println("Minimum Coins Required for " + centValue + ": \n" + resultMap);
	}

	/**
	 * The function uses dynamic programming to find the minim coin change for the
	 * given cent value.
	 * 
	 * @param dynamicArray
	 * @param centValue
	 * @return
	 */
	public static Map<Integer, Integer> getMinimumCoinChange(int[][] dynamicArray, int centValue) {

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j <= centValue; j++) {
				Integer coinDenomination = indexCoinMap.get(i);
				if (i == 0) {
					dynamicArray[i][j] = j;
					continue;
				}
				if (j < coinDenomination) {
					dynamicArray[i][j] = dynamicArray[i - 1][j];
					continue;
				}
				int aboveValue = dynamicArray[i - 1][j];
				int subractedValue = dynamicArray[i][j - coinDenomination] + 1;
				if (aboveValue >= subractedValue) {
					dynamicArray[i][j] = subractedValue;
				} else {
					dynamicArray[i][j] = aboveValue;
				}
			}
		}

		printMatrix(dynamicArray, centValue);
		findRequiredCoinDenominations(dynamicArray, centValue);
		return resultMap;

	}

	/**
	 * The function finds the minimum coin change required for the given cent value
	 * from the dynamic matrix formed.
	 * 
	 * @param dynamicArray
	 * @param centValue
	 */
	private static void findRequiredCoinDenominations(int[][] dynamicArray, int centValue) {
		int columnIndex = centValue;
		int rowIndex = 4;
		int initialValue = dynamicArray[4][columnIndex];
		while (columnIndex != 0) {
			if (rowIndex == 1) {
				int coinDenominationCounter = resultMap.get(rowIndex);
				coinDenominationCounter = dynamicArray[rowIndex][columnIndex];
				resultMap.put(rowIndex, coinDenominationCounter);
				break;
			}
			if (dynamicArray[rowIndex - 1][columnIndex] == initialValue) {
				rowIndex = rowIndex - 1;
				continue;
			}
			int coinDenomination = indexCoinMap.get(rowIndex);
			int noOFCoins = dynamicArray[rowIndex][columnIndex - coinDenomination];
			initialValue = noOFCoins;
			int coinDenominationCounter = resultMap.get(coinDenomination);
			coinDenominationCounter++;
			resultMap.put(coinDenomination, coinDenominationCounter);
			columnIndex = columnIndex - coinDenomination;
		}
	}

	/**
	 * The function prints the given dynamic matrix.
	 * 
	 * @param dynamicArray
	 * @param centValue
	 */
	private static void printMatrix(int[][] dynamicArray, int centValue) {
		System.out.print("[");
		for (int rowIndex = 0; rowIndex < 5; rowIndex++) {
			for (int colIndex = 0; colIndex <= centValue; colIndex++) {
				System.out.print(dynamicArray[rowIndex][colIndex] + "\t");
			}
			System.out.println("\n");
		}
		System.out.print("]");
	}

}
