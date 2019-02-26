import java.util.LinkedList;
import java.util.List;



public class DoublyLinkedList <T> {

	private class Node<T> {
		private T data;
		private Node nextNode;
		private Node prevNode;
		private Node(T data){
			this.data = data;
		}
		public T getData() {
			return data;
		}
	}
	private Node head = null;
	private long listSize = 0;
	
	/*public static void main(String[] args) {
		add(10);
		add(20);
		add(30);
		addLast(0);
		//display();
		add(0, 3);
//		//display();
		add(1,67);
		
		add(4, 42);
		display();
		delete(1);
		display();
	}*/
	
	public void add(T data) {
		Node newData = new Node(data);
		listSize++;
		if(head == null) {
			head = newData;
			//head.prevNode = newData;
			return;
		}
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			currentNode = currentNode.nextNode;
		}
		newData.prevNode = currentNode;
		currentNode.nextNode = newData;
		//newData.nextNode = head;
		//newData.prevNode = head.nextNode;
		//head.prevNode = newData;
		//head = newData;
		//System.out.println("Next Node:" + head.nextNode.getData()); 
	}
	
	public void addAll(List<T> data) {
		for(int i =0 ;i <data.size();i++) {
			add(data.get(i));
		}
	}
	
	public void addLast(T data) {
		Node newData = new Node(data);
		listSize++;
		if(head == null) {
			head = newData;
			return;
		}
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			currentNode = currentNode.nextNode;
		}
		newData.prevNode = currentNode;
		currentNode.nextNode = newData;
	}
	
	public void add(int index, T data) {
		Node newData = new Node(data);
		if(head == null) {
			add(data);
			return;
		}
		Node currentNode = head;
		int nodeIndex = 0 ;
		if(index == 0) {
			newData.nextNode = head;
			head.prevNode = newData;
			head = newData;
			return;
		}
		while(index !=  nodeIndex) {
			currentNode = currentNode.nextNode;
			nodeIndex++;
		}
		Node temp = currentNode.prevNode;
		newData.nextNode = currentNode;
		currentNode.prevNode = newData;
		newData.prevNode = temp;
		temp.nextNode = newData;
		listSize++;
	}
	
	public Object get(int index) {
		int nodeIndex = 0;
		Node currentNode = head;
		while(nodeIndex != index) {
			currentNode = currentNode.nextNode;
			nodeIndex++;
		}
		return currentNode.getData();
	}
	
	public void delete(int index) {
		if(head == null) {
			return;
		}
		if(index == 0) {
			head = head.nextNode;
			return;
		}
		Node currentNode = head;
		int nodeIndex = 0 ;
		while(index !=  nodeIndex) {
			currentNode = currentNode.nextNode;
			nodeIndex++;
		}
		Node previousNode = currentNode.prevNode ;
		Node nextNode = currentNode.nextNode;
		previousNode.nextNode = nextNode;
		nextNode.prevNode = previousNode;
	}
	
	public void display() {
		Node currentNode = head;
		do{
			
			System.out.println(currentNode.getData() + "->");
			currentNode = currentNode.nextNode;
		}
		while(currentNode != null);
	}
	
	public  <T> boolean contains(T object) {
		Node currentNode = head;
		while(currentNode.nextNode != null) {
			if(currentNode.getData().equals(object)) {
				return true;
			}
			currentNode = currentNode.nextNode;
		}
		return false;
	}
	
	public long size() {
//		LinkedList<Integer> a = new LinkedList<>();
//		a.add(10);
//		a.add(2);
//		System.out.println(a.get(0));
		return listSize;
	}
	
}
