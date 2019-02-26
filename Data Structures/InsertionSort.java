import java.util.List;

public class InsertionSort {
	

	public long insertionSort(List<Integer> unsortedList) throws Exception {
		validateUnsortedList(unsortedList);
		long counter = 0;
		for (int unsortedIndex = 1; unsortedIndex < unsortedList.size(); unsortedIndex++) {
			int sortedListKey = 0;
			int unsortedListKey = unsortedList.get(unsortedIndex);
			for (int sortedIndex = unsortedIndex - 1; sortedIndex >= 0; sortedIndex--) {
				counter++;
				if(unsortedListKey > unsortedList.get(sortedIndex)) {
					break;
				}
					sortedListKey = unsortedList.get(sortedIndex);
					unsortedList.set(sortedIndex+1, sortedListKey);
					unsortedList.set(sortedIndex, unsortedListKey);
			}
		}
//		System.out.println("count = " + counter);
		return counter;
		//return unsortedList;
	}
	
	private void validateUnsortedList(List<Integer> unsortedList) throws Exception {
		if(unsortedList.isEmpty() || unsortedList.equals(null) ) {
			throw new Exception("List is empty!");
		}
	}
	
}
