import java.util.ArrayList;
import java.util.List;

public class MaximumSubArrayUsingBruteForce {

static List<Integer> inputList = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		List<Integer> inputList = new ArrayList<Integer>();
//		inputList.add(1);
//		inputList.add(-3);
//		inputList.add(2);
//		inputList.add(-5);
//		inputList.add(7);
//		inputList.add(6);
//		inputList.add(-1);
//		inputList.add(-4);
//		inputList.add(11);
//		inputList.add(-23);
		
		inputList.add(3);
		inputList.add(-2);
		inputList.add(5);
		inputList.add(-1);
		System.out.println("Input List :" + inputList);
		int maximumSum = findMaximumSubArrayUsingImprovedBF(inputList);
		System.out.println("Maximum Sum :" + maximumSum);
		
	}
	
	/**
	 * This function uses brute force to find the maximum subarray.
	 * O(n^3)
	 * @param inputList
	 * @return
	 */
	public static Integer findMaximumSubArrayUsingBruteForce(List<Integer> inputList){
		int index = 0;
		int maxSum = 0;
		int startIndex = 0;
		int endIndex = 0;
		while(index < inputList.size()) {
			for ( int sumIndex = 0 ; sumIndex < inputList.size() ;sumIndex++ ) {
				int sum = findSum(inputList, sumIndex, index);
				if(sum < maxSum) {
					continue;
				}
					startIndex = sumIndex;
					endIndex = sumIndex + index;
					maxSum = sum;
			}
			index++;
		}
		System.out.println("Start Index : " + startIndex);
		System.out.println("End Index :" + endIndex);
		return maxSum;
	}
	
	/**
	 * The function uses brute force to find the maximum subarray.
	 * O(n^2)
	 * @param inputList
	 * @return
	 */
	public static int findMaximumSubArrayUsingImprovedBF(List<Integer> inputList){
		int index = 0;
		int maxSum = inputList.get(0);
		int startIndex = 0;
		int endIndex = 0;
		while(index < inputList.size()-1) {
			int sum = inputList.get(index);
			int sumIndex = index + 1;
			do {
				if(sum  > maxSum) {
					startIndex = index;
					endIndex = sumIndex - 1;
					maxSum = sum;
				}
				sum = sum + inputList.get(sumIndex);
				sumIndex++;
			}while (sumIndex < inputList.size());
			index++;
		}
		System.out.println("Start Index : " + startIndex);
		System.out.println("End Index :" + endIndex);
		return maxSum;
	}
	
	public static int findSum(List<Integer> inputList, int fromIndex, int size) {
		int listSum = 0;
		while(size >= 0 && fromIndex < inputList.size()) {
			listSum = listSum + inputList.get(fromIndex);
			fromIndex++;
			size--;
		}
		return listSum ;
	}
}
