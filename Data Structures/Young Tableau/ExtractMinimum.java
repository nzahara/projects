package YoungTableau;

import java.util.Scanner;

public class ExtractMinimum {
	
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
		System.out.println("Tableau entered is ");
		printMatrix(youngHeap);
		extractMinimumElement(youngHeap);
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

	/**
	 * The function extracts the minimum element from young's tableau.
	 * 
	 * @param youngHeap
	 */
	public static void extractMinimumElement(int[][] youngHeap) {
		System.out.print("Smallest element removed :" + youngHeap[0][0] + "\t");
		youngHeap[0][0] = Integer.MAX_VALUE;
		youngifyHeapSort(youngHeap, 0, 0);
		printMatrix(youngHeap);
	}


	/**
	 * The function arranges the elements in young's tableau to satisfy young tableau condition.
	 * 
	 * @param youngHeap
	 * @param row
	 * @param column
	 * @return
	 */
	public static int[][] youngifyHeapSort(int[][] youngHeap, int row, int column) {
		//replace the smallest of right and down to current
		int downVal = (row+1 < rowSize) ? youngHeap[row+1][column] : Integer.MAX_VALUE;
		int rightVal = (column+1 < columnSize) ? youngHeap[row][column+1] : Integer.MAX_VALUE;
		if(downVal == Integer.MAX_VALUE && rightVal == Integer.MAX_VALUE) {
			return youngHeap;
		}
		if(downVal <= rightVal) {
			youngHeap[row+1][column] = youngHeap[row][column];
			youngHeap[row][column] = downVal;
			return youngifyHeapSort(youngHeap, row+1, column);
		} else {
			youngHeap[row][column+1] = youngHeap[row][column];
			youngHeap[row][column] = rightVal;
			return youngifyHeapSort(youngHeap, row, column+1);
		}
	}


}
