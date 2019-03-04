import java.util.Arrays;

public class BinarySearch {
	
	static int key = 0;
	
	public static void main(String[] args) {
		int A[] = {2,5,6,8,10,12,13};
		key = 2;
		String inputArray = Arrays.toString(A);
		System.out.println("Input Array :" + inputArray);
		int keyIndex = binarySearch(A, 0, A.length-1);
		if(keyIndex == -1) {
			System.out.println("Key " + key + " not found in the Array :" + inputArray);
			return;
		}
		System.out.println("Key " + key + " found in Array :" + inputArray + " at index " + keyIndex);
	}
	
	
	/**
	 * The function searches for the key in the given sorted array.
	 * It implements Binary Search Algorithm.
	 * Worst Case Time Complexity : O(log(n))
	 * Best Case Time Complexity : O(1)
	 * 
	 * @param A - Input Array
	 * @param lowerIndex 
	 * @param upperIndex
	 * @return
	 */
	public static int binarySearch(int A[],int lowerIndex, int upperIndex) {
		System.out.println("Lower Index : " + lowerIndex);
		System.out.println("Upper Index : " + upperIndex);
		if((lowerIndex == upperIndex)) {
			if(A[lowerIndex] == key) {
				return lowerIndex;
			}
			return -1;
		}
		int middleIndex = (lowerIndex + upperIndex)/2;
		if(key == A[middleIndex]) {
			return middleIndex;
		}
		if(key > A[middleIndex]) {
			lowerIndex = middleIndex+1;
		} else {
			upperIndex = middleIndex - 1;
		}
		return binarySearch(A, lowerIndex, upperIndex);
	}

}
