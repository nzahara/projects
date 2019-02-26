import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class QuickSort {

	public static void main(String[] args) {
		List<Integer> unsortedList = new ArrayList<Integer>();
		unsortedList.add(10);
		unsortedList.add(7);
		unsortedList.add(8);
		unsortedList.add(9);
		unsortedList.add(1);
		unsortedList.add(5);
		System.out.println("Unsorted List:" + unsortedList);
		quickSortUsingLomuto(unsortedList);
		System.out.println("Sorted List:" + unsortedList);
	}
	
	/**
	 * This function uses Hoar's partition algorithm where the 
	 * first element is chosen as pivot.
	 * 
	 * @param inputList
	 */
	public static void quickSortUsingHoars(List<Integer> inputList) {
		if(inputList.size() <= 1) {
			return;
		}
		System.out.println("Input list:" + inputList);
		int partitionIndex = hoarsPartition(inputList);
		System.out.println("List:" +inputList);
		System.out.println("Partition Index:" + partitionIndex);
		if(partitionIndex+1 > inputList.size() || inputList.size() <= 1) {
			return;
		}
		List<Integer> leftHalfList = inputList.subList(0, partitionIndex);
		List<Integer> rightHalfList = inputList.subList(partitionIndex+1, inputList.size());
		System.out.println("Left Half :" + leftHalfList);
		System.out.println("Right Half :" + rightHalfList);
		quickSortUsingHoars(leftHalfList);
		quickSortUsingHoars(rightHalfList);
	}
	
	public static int hoarsPartition(List<Integer> inputList) {
		int pivot = inputList.get(0);
		System.out.println("Pivot :" + pivot);
		int startIndex = -1;
		int endIndex = inputList.size();
		while(true) {
			do {
				startIndex++;
			}while(inputList.get(startIndex) < pivot);
			do {
				endIndex--;
			}while(inputList.get(endIndex) > pivot);
			if(startIndex >= endIndex) {
				return endIndex;
			}
			Integer endIndexValue = inputList.get(endIndex);
			inputList.set(endIndex, inputList.get(startIndex));
			inputList.set(startIndex, endIndexValue);
		}
	}
	
	/**
	 * This function uses Lomuto's partition algorithm where the 
	 * last element is chosen as pivot.
	 * 
	 * @param inputList
	 */
	public static void quickSortUsingLomuto(List<Integer> inputList) {
		Deque<Integer> a = new ArrayDeque<>();
		if(inputList.size() <= 0) {
			return;
		}
		System.out.println("Input list:" + inputList);
		int partitionIndex = lomutosPartition(inputList);
		System.out.println("List:" +inputList);
		System.out.println("Partition Index:" + partitionIndex);
		System.out.println("Input SIze : "+ inputList.size());
		if(partitionIndex+1 > inputList.size() || inputList.size() == 1) {
			return;
		}
		List<Integer> leftHalfList = inputList.subList(0, partitionIndex);
		List<Integer> rightHalfList = inputList.subList(partitionIndex+1, inputList.size());
		System.out.println("Left Half :" + leftHalfList);
		System.out.println("Right Half :" + rightHalfList);
		quickSortUsingLomuto(leftHalfList);
		quickSortUsingLomuto(rightHalfList);
	}
	
	public static int lomutosPartition(List<Integer> inputList) {
		int pivot = inputList.get(inputList.size() -1);
		int partitionIndex = 0;
		for(int index = 0 ; index < inputList.size() - 1;index++) {
			if(inputList.get(index) >= pivot) {
				continue;
			}
			int tempElement = inputList.get(index);
			inputList.set(index, inputList.get(partitionIndex));
			inputList.set(partitionIndex, tempElement);
			partitionIndex++;
		}
		inputList.set(inputList.size()-1, inputList.get(partitionIndex));
		inputList.set(partitionIndex, pivot);
		return partitionIndex;
	}
}
