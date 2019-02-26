
public class PriorityQueue {
	
	Node head = null;
	long listSize = 0;
	
	/*public static void main(String[] args) {
		push(0, 1);
		push(5, 2);
		push(10, 0);
		display();
		pop();
	}*/
	
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
	
	public <T> void push(T data, int priority) {
		listSize++;
		Node newData = new Node(data, priority);
		if(head == null) {
			head = newData;
			return;
		}
		//sort the data based on priority and then insert
		if(head.getPriority() >= priority) {
			newData.nextNode = head;
			head = newData;
			return;
		} 
		Node currentNode = head;
		while(currentNode.nextNode != null && currentNode.nextNode.getPriority() < newData.getPriority()) {
			currentNode = currentNode.nextNode;
		}
		newData.nextNode = currentNode.nextNode;
		currentNode.nextNode = newData;
	}
	
	public Object pop() {
		listSize--;
		Object value = head.getValue();
		head = head.nextNode;
		return value;
	}
	
	public Object peek() {
		if (head == null) {
			return null;
		}
		return head.getValue();
	}
	
	public Node get (Object data) {
		if(head == null) {
			return  null;
		}
		Node currentNode = head;
		while( currentNode.nextNode != null) {
			if(currentNode.getValue().equals(data)) {
				return currentNode;
			}
			currentNode = currentNode.nextNode;
		}
		
		return currentNode;
	}
	
	public <T> int getPriority(T data) { 
		if(head == null) {
			return 0;
		}
		Node currentNode = head;
		while (currentNode.nextNode != null) {
			if(currentNode.getValue().equals(data)) {
				return currentNode.getPriority();
			}
			currentNode = currentNode.nextNode;
		}
		return 0;
	}
	
	public void display() {
		Node currentNode = head;
		if(head == null) {
			return;
		}
		do {
			//System.out.println(currentNode.getValue()).getData() + "->" + currentNode.getPriority());
			//System.out.println(currentNode.getValue() + "->");
			currentNode = currentNode.nextNode;
		}while(currentNode != null);
	}
	
	public  <T> boolean contains(T object) {
		if(head == null) {
			return false;
		}
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			if(currentNode.getValue().equals(object)) {
				return true;
			}
			currentNode = currentNode.nextNode;
		}
		return false;
	}
	
	public boolean remove(Object object) {
		if(object.equals(head.getValue())) {
			head = head.nextNode;
			return true;
		}
		Node currentNode = head;
		Node previousNode = head;
		while(currentNode.nextNode != null) {
			if(currentNode.getValue().equals(object)) {
				previousNode.nextNode = currentNode.nextNode;
				return true;
			}
			previousNode = currentNode;
			currentNode = currentNode.nextNode;
		}
		return false;
	}
	
	public long size() {
		return listSize;
	}

	
}
