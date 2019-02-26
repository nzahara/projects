package IS_Project;

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
	 * The function checks if the given state is already present in the queue or not.
	 * 
	 * @param state
	 * @return - true if the state exists else false
	 */
	public <T> boolean containState(T state) {
		if (head == null) {
			return false;
		}
		Node currentNode = head;
		IS_Project.Node givenState = (IS_Project.Node) state;
		long tempListSize = this.size();
		while (tempListSize > 0) {
			IS_Project.Node currentState = (IS_Project.Node) currentNode.getValue();
			if (Utility.checkEquality(currentState.getState(), givenState.getState())) {
				return true;
			}
			currentNode = currentNode.nextNode;
			tempListSize--;
		}
		return false;
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
