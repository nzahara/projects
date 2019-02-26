import java.util.List;

public class SelectionSort {
	

	public long selectionSort(List<Integer> unsortedList) throws Exception {
		validateUnsortedList(unsortedList);
		long counter = 0;
		Integer smallestEle = null;
		int indexKey = 0;
		for(int index = 0;index < unsortedList.size(); index++) {
			smallestEle = unsortedList.get(index);
			for(int compIndex = index+1; compIndex < unsortedList.size(); compIndex++) {
				counter++;
				if(unsortedList.get(compIndex) < smallestEle) {
					smallestEle = unsortedList.get(compIndex);
					indexKey = compIndex;
				}
			}
			if(smallestEle != unsortedList.get(index)) {
				unsortedList.set(indexKey,unsortedList.get(index));
			}
			unsortedList.set(index, smallestEle);
		}
		return counter;
	}

	private void validateUnsortedList(List<Integer> unsortedList) throws Exception {
		if(unsortedList.isEmpty() || unsortedList.equals(null) ) {
			throw new Exception("List is empty!");
		}
	}
}
