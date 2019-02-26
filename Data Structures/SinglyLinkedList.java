import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SinglyLinkedList <T> {
	
	ArrayList<Integer> linkedListData = new ArrayList<Integer>();
	private Node head = null;
	private  int listSize = 0;
	
	/*public  void main(String[] args) {
		add(10);
		add(20);
		List<Integer> data = new ArrayList<>();
		data.add(99);
		data.add(100);
		addAll(data);
		//display();
		add(0, 40);
		add(1, 35);
		//display();
		add(3,6);
		//add(7,200);
		System.out.println("The popped element is :" + pop());
		System.out.println("Does eleemnt present:" + contains(100));
		//display();
//		System.out.println("The first Element :" + remove());
//		System.out.println("The second Element :" + remove());
//		System.out.println("Does the element exist:" + contains(6));
//		System.out.println("Pop the last element:"+pop());
		display();
		//deleteNode(0);
		//display();
		//deleteNode(1);
		//display();
	}*/
	
	private  class Node<T>{
		private T value;
		private Node nextNode;
		Node(T data){
			this.value = data;
		}
		public T getValue() {
			return value;
		}
	}
	
	/**
	 * The function inserts data at the beginning of the list.
	 * @param data
	 */
	public void add(int data) {
		listSize++;
		Node newNode = new Node(data);
		if(head == null) {
			head = newNode;
			return;
		}
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			currentNode = currentNode.nextNode;
		}
		currentNode.nextNode = newNode;
	}
	
	// ??? new object each time
	public  <T> void add(T data) {
		listSize++;
		Node newNode = new Node(data);
		if(head == null) {
			head = newNode;
			return;
		}
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			currentNode = currentNode.nextNode;
		}
		currentNode.nextNode = newNode;
	}
	
	public <T> void addFirst(T data) {
		listSize++;
		Node newData = new Node(data);
		newData.nextNode = head;
		head = newData;
	}
	
	public  void addAll(List<Integer> data) {
		for(int i = 0; i<data.size();i++) {
			add(data.get(i));
		}
	}
	
	public  void add(int index, int data) {
		int nodeIndex = 0;
		Node newData = new Node(data);
		Node currentNode = head;
		Node previousNode = head;
		while(nodeIndex != index) {
			previousNode = currentNode;
			currentNode = currentNode.nextNode;
			nodeIndex++;
		}
		if(nodeIndex == 0) {
			newData.nextNode = head;
			head = newData;
			return;
		}
		previousNode.nextNode = newData;
		newData.nextNode = currentNode;
	}
	
	public  void deleteNode(int index) {
		System.out.println("head Node:"+head.getValue());
		int nodeIndex = 0;
		Node currentNode = head;
		Node previousNode = head;
		while(nodeIndex != index) {
			previousNode = currentNode;
			currentNode = currentNode.nextNode;
			nodeIndex++;
		}
		if(nodeIndex == 0) {
			previousNode = currentNode.nextNode;
			head = previousNode;
		}else {
			previousNode.nextNode = currentNode.nextNode;
		}
	}
	
	/**
	 * This is the queue implementation. FIFO
	 * @return
	 */
	public  Object remove() {
		listSize--;
		Object firstElement = head.getValue();
		head = head.nextNode;
		return firstElement;
	}
	
	public  Object pop() {
		listSize--;
		Node currentNode = head;
		Node previousNode = head;
		while(currentNode.nextNode != null) {
			previousNode = currentNode;
			currentNode = currentNode.nextNode;
		}
		Object lastElement = currentNode.getValue();
		previousNode.nextNode = currentNode.nextNode;
		return lastElement;
	}
	
	public  Object peek() {
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			currentNode = currentNode.nextNode;
		}
		return currentNode.getValue();
	}
	
	public Object get(int index) {
		int nodeIndex = 0;
		Node currentNode = head;
		while(nodeIndex != index) {
			currentNode = currentNode.nextNode;
			nodeIndex++;
		}
		return currentNode.getValue();
	}
	
	public  <T> boolean contains(T object) {
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			if(currentNode.getValue().equals(object)) {
				return true;
			}
			currentNode = currentNode.nextNode;
		}
		return false;
	}
	
	public  void display() {
		Node currentNode = head;
		do {
			System.out.println(currentNode.getValue() + "->\t");
			currentNode = currentNode.nextNode;
		}
		while(currentNode != null );
	}
	
	/**
	 * The function increases the list size if it is going outof bound.
	 * 
	 * @param listSize
	 */
	public void ensureCapacity(int listSize) {
		if(linkedListData.size() > listSize) {
			return;
		}
		 linkedListData.ensureCapacity(linkedListData.size() + listSize);
	}
	
	
	public  int size() {
		return listSize;
	}


}
