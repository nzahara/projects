package YoungTableau;

import java.util.Arrays;
import java.util.Scanner;

public class Sort  {
	static int rowSize = 0;
	static int columnSize = 0;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the size of array to be inserted ");
		int arraySize = input.nextInt();
		int[] inputArray = new int[arraySize];
		System.out.println("Enter the input elements to be sorted");
		for (int index = 0; index < arraySize; index++) {
			inputArray[index] = input.nextInt();
		}
		rowSize = (int) Math.sqrt(arraySize);
		System.out.println("Input :" + Arrays.toString(inputArray));
		sortAndInsert(inputArray);
	}

	/**
	 * The function sorts the given elements in the array using young tableau.
	 * 
	 * @param inputArray
	 */
	public static void sortAndInsert(int[] inputArray) {
		columnSize = rowSize;
		int[][] youngHeap = new int[rowSize][columnSize];
		for (int[] row : youngHeap) {
			Arrays.fill(row, Integer.MAX_VALUE);
		}
		int index = 0;
		for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
			for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
				//System.out.println("element:" + inputArray[index]);
				youngHeap[rowSize - 1][columnSize - 1] = inputArray[index];
				youngHeapifyAfterInsert(youngHeap, rowSize - 1, columnSize - 1);
				printMatrix(youngHeap);
				index++;
			}
		}
		printMatrix(youngHeap);
		System.out.println("Sorted list:");
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < columnSize; j++) {
				System.out.println(youngHeap[0][0]);
				youngHeap[0][0] = Integer.MAX_VALUE;
				youngifyHeapSort(youngHeap, 0, 0);
			}
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
