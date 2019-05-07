package YoungTableau;

import java.util.Scanner;

public class InsertNewElement {
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
		insertElement(youngHeap);
	}

	/**
	 * The function inserts a new element to young's tableau if the tableau
	 * has empty index and restores the young tableau's condition.
	 * 
	 * @param youngHeap
	 */
	public static void insertElement(int[][] youngHeap) {
		System.out.println("Enter a new element");
		Scanner inputElement = new Scanner(System.in);
		int newElement = inputElement.nextInt();
		System.out.println("Element being inserted :" + newElement);
		if (youngHeap[rowSize - 1][columnSize - 1] != Integer.MAX_VALUE) {
			System.out.println("Tableau is full");
			return;
		}
		youngHeap[rowSize - 1][columnSize - 1] = newElement;
		printMatrix(youngHeap);
		youngHeapifyAfterInsert(youngHeap, rowSize - 1, columnSize - 1);
		printMatrix(youngHeap);

	}

	/**
	 * The function arranges the elements in young's tableau to satisfy young tableau condition after an element 
	 * has been inserted .
	 * 
	 * @param youngHeap
	 * @param row
	 * @param column
	 */
	public static void youngHeapifyAfterInsert(int[][] youngHeap, int row, int column) {

		if ((row - 1) >= 0 && (youngHeap[row - 1][column] >= youngHeap[row][column])) {
			int temp = youngHeap[row][column];
			youngHeap[row][column] = youngHeap[row - 1][column];
			youngHeap[row - 1][column] = temp;
			printMatrix(youngHeap);
			youngHeapifyAfterInsert(youngHeap, row - 1, column);
		}
		if ((column - 1) >= 0 && (youngHeap[row][column - 1] >= youngHeap[row][column])) {
			int temp = youngHeap[row][column];
			youngHeap[row][column] = youngHeap[row][column - 1];
			youngHeap[row][column - 1] = temp;
			printMatrix(youngHeap);
			youngHeapifyAfterInsert(youngHeap, row, column - 1);
		}
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
