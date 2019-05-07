package IS_Project2;

import java.util.List;

interface Utility {

	/**
	 * This function checks if the two given lists are equal.
	 * 
	 * @param listA
	 * @param listB
	 * @return
	 */
	public static <T> boolean checkEquality(List<T> listA, List<T> listB) {
		return listA.equals(listB);
	}

	/**
	 * The function finds the index of the element in the given list.
	 * 
	 * @param listA
	 * @param element
	 * @return
	 */
	public static <T> int findIndex(List<T> listA, T element) {
		if (listA.isEmpty()) {
			return -1;
		}
		for (int index = 0; index < listA.size(); index++) {
			if (listA.get(index).equals(element)) {
				return index;
			}
		}
		return -1;
	}

}
