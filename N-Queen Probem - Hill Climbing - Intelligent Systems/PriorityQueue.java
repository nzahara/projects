package IS_Project2;

public class PriorityQueue {

	Node head = null;
	long listSize = 0;


	class Node<T> {
		private T value;
		private int priority;
		private Node nextNode;

		private Node(T data, int priority) {
			this.value = data;
			this.priority = priority;
		}

		public T getValue() {
			return value;
		}

		public int getPriority() {
			return priority;
		}
	}

	/**
	 * The function adds items to the queue based on the priority mentioned.
	 * 
	 * @param data
	 * @param priority
	 */
	public <T> void push(T data, int priority) {
		listSize++;
		Node newData = new Node(data, priority);
		if (head == null) {
			head = newData;
			return;
		}
		// sort the data based on priority and then insert
		if (head.getPriority() >= priority) {
			newData.nextNode = head;
			head = newData;
			return;
		}
		Node currentNode = head;
		while (currentNode.nextNode != null && currentNode.nextNode.getPriority() < newData.getPriority()) {
			currentNode = currentNode.nextNode;
		}
		newData.nextNode = currentNode.nextNode;
		currentNode.nextNode = newData;
	}

	/**
	 * The function removes the first element of the queue and returns the same.
	 * 
	 * @return
	 */
	public Object pop() {
		listSize--;
		Object value = head.getValue();
		head = head.nextNode;
		return value;
	}

	/**
	 * The function returns the priority of the object passed.
	 * 
	 * @param data
	 * @return
	 */
	public <T> int getPriority(T data) {
		if (head == null) {
			return 0;
		}
		Node currentNode = head;
		while (currentNode.nextNode != null) {
			if (currentNode.getValue().equals(data)) {
				return currentNode.getPriority();
			}
			currentNode = currentNode.nextNode;
		}
		return 0;
	}

	/**
	 * The function returns the size of the queue.
	 * 
	 * @return - long
	 */
	public long size() {
		return listSize;
	}

}
