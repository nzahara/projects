import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MaximumSubArrayUsingDC {

static List<Integer> inputList = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		List<Integer> inputList = new ArrayList<Integer>();
//		inputList.add(1);
//		inputList.add(-3);
//		inputList.add(2);
//		inputList.add(-5);
//		inputList.add(7);
//		inputList.add(6);//		inputList.add(-1);
//		inputList.add(-4);
//		inputList.add(11);
//		inputList.add(-23);
		
//		inputList.add(3);
//		inputList.add(-2);
//		inputList.add(5);
//		inputList.add(-1);
		
		inputList.add(1);
		inputList.add(2);
		inputList.add(3);
		inputList.add(4);
		System.out.println("Input List :" + inputList);
		List<Integer> maxSubArrayDetails = maximumSubArrayUsingDC(inputList);
		if(maxSubArrayDetails == null) {
			System.out.println("Input is empty!");
			return;
		}
		System.out.println("Maximum Sum :" + maxSubArrayDetails.get(0));
		System.out.println("Start Index :" + maxSubArrayDetails.get(1));
		System.out.println("End Index :" + maxSubArrayDetails.get(2));
		
	}
	
	/**
	 * The function uses divide and conquer to find the maximum subarray.
	 * O(nlogn)
	 * 
	 * @param inputList
	 * @return - {@link List} containing max sum, start and end endex of the subarray.
	 */
	public static List<Integer> maximumSubArrayUsingDC(List<Integer> inputList) {
		if(inputList.isEmpty()) {
			return null;
		}
		List<Integer> leftHalf = new ArrayList<Integer>(inputList.size());
		List<Integer> rightHalf = new ArrayList<Integer>(inputList.size());
		if(inputList.size() == 1) {
			List<Integer> mssDetails = new ArrayList<Integer>(3);
			mssDetails.add(inputList.get(0));
			mssDetails.add(0);
			mssDetails.add(0);
			return mssDetails;
		}
		int middleIndex = (int)Math.floor(inputList.size()/2);
		leftHalf.addAll(inputList.subList(0, middleIndex));
		rightHalf.addAll(inputList.subList(middleIndex, inputList.size()));
		List<Integer> leftHalfMssDetails  = maximumSubArrayUsingDC(leftHalf);
		List<Integer> rightHalfMssDetails = maximumSubArrayUsingDC(rightHalf);
		List<Integer> midSumDetails = maximumSubArrayAcross(leftHalf, rightHalf);
		Integer leftHalfMss = leftHalfMssDetails.get(0);
		Integer rightHalfMss = rightHalfMssDetails.get(0);
		Integer midMss = midSumDetails.get(0);
		Integer maxSum = Collections.max(Arrays.asList(leftHalfMss,rightHalfMss,midMss));
		if( maxSum == leftHalfMss) {
			return leftHalfMssDetails;
		}else if(maxSum == rightHalfMss) {
			int startIndex = rightHalfMssDetails.get(1);
			int endIndex =  rightHalfMssDetails.get(2);
			rightHalfMssDetails.set(1, startIndex + inputList.size()/2);
			rightHalfMssDetails.set(2, endIndex + inputList.size()/2);
			return rightHalfMssDetails;
		} 
		return midSumDetails;
	}
	
	/**
	 * The function finds the maximum subarray across the midpoint.
	 * 
	 * @param leftHalf
	 * @param rightHalf
	 * @return
	 */
	public static List<Integer> maximumSubArrayAcross(List<Integer> leftHalf, List<Integer> rightHalf) {
		int maxLeftSum = leftHalf.get(leftHalf.size()-1);
		int maxRightSum = rightHalf.get(0);
		int leftSum = maxLeftSum;
		int rightSum = maxRightSum;
		int startIndex = leftHalf.size() - 1 ;
		int inputListSize = leftHalf.size() + rightHalf.size();
		int endIndex = 0 + inputListSize/2;
		for(int leftIndex = leftHalf.size()-2; leftIndex >=0; leftIndex--) {
			leftSum  = leftSum + leftHalf.get(leftIndex);
			if(leftSum > maxLeftSum) {
				maxLeftSum = leftSum;
				startIndex = leftIndex;
			}
		}
		for(int rightIndex = 1 ; rightIndex < rightHalf.size(); rightIndex++) {
			rightSum = rightSum + rightHalf.get(rightIndex); 
			if(rightSum > maxRightSum) {
				maxRightSum = rightSum;
				endIndex = rightIndex + inputListSize/2;
			}
		}	
		List<Integer> result = new ArrayList<Integer>(3);
		result.add(maxLeftSum + maxRightSum);
		result.add(startIndex);
		result.add(endIndex);
		return result;
	}
	
}
