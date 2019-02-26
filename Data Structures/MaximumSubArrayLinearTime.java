import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class MaximumSubArrayLinearTime {

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
		List<Integer> maxSum = maximumSubArrayUsingImprovedDC(inputList);
		if(maxSum == null) {
			System.out.println("Input list is empty!");
			return;
		}
		System.out.println("Maximum Sum :" + maxSum.get(1));
	}
	
	/**
	 * The function uses divide and conquer to find the maximum subarray.
	 * The function takes O(n) to find the same.
	 * It returns totalSum, MaxSum, MaxPrefix,MaxSuffix respectively in each recursion.
	 * 
	 * @param inputList
	 * @return
	 */
	public static List<Integer> maximumSubArrayUsingImprovedDC(List<Integer> inputList) {
		if(inputList.isEmpty()) {
			return null;
		}
		List<Integer> leftHalf = new ArrayList<Integer>(inputList.size());
		List<Integer> rightHalf = new ArrayList<Integer>(inputList.size());
		if(inputList.size() == 1) {
			List<Integer> mssDetails = new ArrayList<Integer>(3);
			mssDetails.add(inputList.get(0));
			mssDetails.add(inputList.get(0));
			mssDetails.add(inputList.get(0));
			mssDetails.add(inputList.get(0));
			return mssDetails;
		}
		int middleIndex = (int)Math.floor(inputList.size()/2);
		leftHalf.addAll(inputList.subList(0, middleIndex));
		rightHalf.addAll(inputList.subList(middleIndex, inputList.size()));
		List<Integer> leftHalfMssDetails  = maximumSubArrayUsingImprovedDC(leftHalf);
		List<Integer> rightHalfMssDetails = maximumSubArrayUsingImprovedDC(rightHalf);
		return maximumSubArrayUsingImprovedDC(leftHalfMssDetails,rightHalfMssDetails);
	}
	
	public static List<Integer> maximumSubArrayUsingImprovedDC(List<Integer> leftHalf, List<Integer> rightHalf) {
		List<Integer> result = new ArrayList<Integer>(4);
		result.add(leftHalf.get(0) + rightHalf.get(0));
		result.add(Arrays.asList(leftHalf.get(1), rightHalf.get(1), leftHalf.get(3) + rightHalf.get(2)).stream().mapToInt(v->v).max().orElseThrow(NoSuchElementException::new));
		result.add(Arrays.asList(leftHalf.get(2), leftHalf.get(0) + rightHalf.get(2)).stream().mapToInt(v->v).max().orElseThrow(NoSuchElementException::new));
		result.add(Arrays.asList(rightHalf.get(3), rightHalf.get(0) + leftHalf.get(3)).stream().mapToInt(v->v).max().orElseThrow(NoSuchElementException::new));
		return result;
	}
}
