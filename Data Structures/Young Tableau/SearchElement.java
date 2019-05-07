package YoungTableau;

import java.util.Scanner;

public class SearchElement {

	static int rowSize = 0;
	static int columnSize = 0;

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		System.out.println("Enter the row size");
		rowSize = input.nextInt();
		System.out.println("Enter the column size");
		columnSize = input.nextInt();
		int[][] youngHeap = new int[rowSize][columnSize];
		System.out.println("Enter the young matrix elements");
		for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
			for (int colIndex = 0; colIndex < columnSize; colIndex++) {
				youngHeap[rowIndex][colIndex] = input.nextInt();
			}
		}
		printMatrix(youngHeap);
		System.out.println("Enter the element to search");
		int key = input.nextInt();
		boolean foundElement = searchElementInTableau(youngHeap, rowSize - 1, 0, key);
		System.out.println("Element found:" + foundElement);

	}

	/**
	 * The function searches for the given element in the young's tableau.
	 * 
	 * @param youngHeap
	 * @param row
	 * @param column
	 * @param key
	 * @return
	 */
	public static boolean searchElementInTableau(int[][] youngHeap, int row, int column, int key) {
		if ((row - 1) < 0 || (column + 1 > columnSize)) {
			return false;
		}
		if (key == youngHeap[row][column]) {
			return true;
		}
		if (key > youngHeap[row][column]) {
			return searchElementInTableau(youngHeap, row, column + 1, key);
		}
		return searchElementInTableau(youngHeap, row - 1, column, key);
	}
	
	/**
	 * The function prints the young tableau matrix.
	 * 
	 * @param youngHeap
	 */
	public static void printMatrix(int[][] youngHeap) {
		System.out.print("[");
		for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
			for (int colIndex = 0; colIndex < columnSize; colIndex++) {
				System.out.print(youngHeap[rowIndex][colIndex] + "\t");
			}
			System.out.println("\n");
		}
		System.out.print("]");
	}

}
