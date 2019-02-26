import java.util.List;

public class BubbleSort {
	

	public int bubbleSort(List<Integer> unsortedList) throws Exception{
		validateUnsortedList(unsortedList);
		int counter = 0;
		for(int index = 0 ;index<unsortedList.size();index++) {
			int swapCount = 0 ;
			for(int traversingIndex = 1;traversingIndex<unsortedList.size()-index;traversingIndex++) {
				counter++;
				Integer currentInteger = unsortedList.get(traversingIndex);
				Integer previousInteger = unsortedList.get(traversingIndex-1);
				if(currentInteger > previousInteger) {
					continue;
				}
				swapCount++;
				unsortedList.set(traversingIndex, previousInteger);
				unsortedList.set(traversingIndex-1, currentInteger);
			}
			if(swapCount == 0) {
				break;
			}
		}
		return counter;
	}
	
	private void validateUnsortedList(List<Integer> unsortedList) throws Exception {
		if(unsortedList.isEmpty() || unsortedList.equals(null) ) {
			throw new Exception("List is empty!");
		}
	}

}
