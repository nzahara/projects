import java.util.ArrayList;
import java.util.List;

public class MergeSort {
	
	long counter = 0 ;

	public long mergeSort(List<Integer> unsortedList) throws Exception{
		 validateUnsortedList(unsortedList);
		 List<Integer> firstHalf = new ArrayList<>(unsortedList.size());
		 List<Integer> secondHalf = new ArrayList<>(unsortedList.size());
		if(unsortedList.size() == 1) {
			return counter;//unsortedList;
		}
		int middleIndex = Math.round(((float)unsortedList.size())/2);
		firstHalf.addAll(unsortedList.subList(0, middleIndex));
		secondHalf.addAll(unsortedList.subList(middleIndex, unsortedList.size()));
		 mergeSort(firstHalf);
		 mergeSort(secondHalf);
		 mergeList(firstHalf,secondHalf,unsortedList);
		 return counter;
	}
	
	
	private  List<Integer> mergeList(List<Integer> leftList, List<Integer> rightList, List<Integer> mergedList) throws Exception{
		int mergedListIndex = 0;
		while(leftList.size() > 0  && rightList.size() > 0 ) {
			counter++;
			if(leftList.get(0) <= rightList.get(0)) {
				mergedList.set(mergedListIndex, leftList.get(0));
				leftList.remove(0);
			} else {
				mergedList.set(mergedListIndex, rightList.get(0));
				rightList.remove(0);
			}
			mergedListIndex++;
		}
		while(mergedListIndex < mergedList.size()) {
			mergedList.remove(mergedListIndex);
		}
		mergedList.addAll(rightList);
		mergedList.addAll(leftList);
		return mergedList;
	}
	
	
//	private List<Integer> mergeList0(List<Integer> leftList, List<Integer> rightList){
//		int rightListSize = rightList.size();
//		int leftListSize = leftList.size();
//		for(int rightIndex = 0; rightIndex < rightListSize;rightIndex ++) {
//			int keyElement = rightList.get(rightIndex);
//			for(int leftIndex = leftListSize-1; leftIndex >= 0;leftIndex--) {
//				if(leftList.get(leftIndex) < keyElement || keyElement == leftList.get(leftIndex)) {
//					continue;
//				}
//				try {
//					leftList.set(leftIndex+1, leftList.get(leftIndex));
//				}catch (Exception e) {
//					leftList.add(leftIndex+1, leftList.get(leftIndex));	
//					}
//				leftList.set(leftIndex, keyElement);
//				if(rightList.isEmpty() || rightIndex < 0) {
//					continue;
//				}
//					rightList.remove(rightIndex);
//					rightIndex = rightIndex-1;
//			}
//			leftListSize = leftList.size();
//			rightListSize = rightList.size();
//		}
//		leftList.addAll(rightList);
//		return leftList;
//	}
	
	private void validateUnsortedList(List<Integer> unsortedList) throws Exception {
		if(unsortedList.isEmpty() || unsortedList.equals(null) ) {
			throw new Exception("List is empty!");
		}
	}
}
