
public class BFSDFS {
	
	private static class Node {
		 private int data;
		 private Node leftNode;
		 private Node rightNode;
		 private Node() {
			}
		 private Node(int data) {
			 this.data = data;
		 }
		public int getData() {
			return data;
		}
	}
	
	static Node rootNode = null; 
	
	public static void main(String[] args) {
		rootNode = new Node(1);
		Node node1 = new Node(2);
		rootNode.leftNode = node1;
		Node node2 = new Node(5);
		rootNode.rightNode = node2;
		Node node5 = new Node(8);
		node2.leftNode = node5;
		Node node6 = new Node(7);
		node2.rightNode = node6;
		node5.leftNode = new Node(6);
		node5.rightNode = new Node(11);
		node6.leftNode = new Node(0);
		node6.rightNode = new Node(9);
		Node node3 = new Node(3);
		node1.leftNode = node3;
		Node node4 = new Node(4);
		node1.rightNode = node4;
		node3.leftNode = new Node(10);
		node3.rightNode = new Node(12);
//		Node bfs = breadthFirstSearch(5);
//		System.out.println("Node found ::" + bfs.getData());
//		System.out.println("Left Node :"+ bfs.leftNode.getData());
//		System.out.println("Right Node :"+ bfs.rightNode.getData());
		Node dfs = depthFirstSearch(5);
		System.out.println("Node Found ::" + dfs.getData());
//		System.out.println("Left Node :" +dfs.leftNode.getData());
//		System.out.println("Right Node:"+dfs.rightNode.getData());
	}
	
	// explored = visited; fringe/frontier - queue
	/**
	 * The function implements Breadth First Search Algorithm
	 * @param value
	 * @return
	 */
	public static Node breadthFirstSearch (int value) {
		SinglyLinkedList queue = new SinglyLinkedList();
		SinglyLinkedList visitedQueue = new SinglyLinkedList();
		queue.add(rootNode);
		while(queue.size() > 0) {
			Node firstQueueElement = (Node) queue.remove();
			visitedQueue.add(firstQueueElement);
			if(firstQueueElement.getData() == value) {
				return firstQueueElement;
			}
			Node leftNode = firstQueueElement.leftNode;
			Node rightNode = firstQueueElement.rightNode;
			boolean availabilityCheck = visitedQueue.contains(leftNode) && queue.contains(leftNode);
			if(leftNode != null && !availabilityCheck) {
				queue.add(leftNode);
			}
			if(rightNode != null && !availabilityCheck) {
				queue.add(rightNode);
			}
		}
		return new Node();
	}
	
	
	/**
	 * The function implements Depth First Search Algorithm.
	 * 
	 * @param searchElement
	 * @return
	 */
	public static Node depthFirstSearch(int searchElement) {
		SinglyLinkedList stack = new SinglyLinkedList();
		SinglyLinkedList visitedQueue = new SinglyLinkedList();
		if(rootNode.getData() == searchElement) {
			return rootNode;
		}
		stack.add(rootNode);	
		while(stack.size() > 0) {
			Node exploringNode = (Node) stack.pop();
			visitedQueue.add(exploringNode);
			if(exploringNode.getData() == searchElement) {
				return exploringNode;
			}
			Node leftNode = exploringNode.leftNode;
			Node rightNode = exploringNode.rightNode;
			if(rightNode != null && (!visitedQueue.contains(rightNode) || !stack.contains(rightNode))) {
				stack.add(rightNode);
			}
			if(leftNode != null && (!visitedQueue.contains(leftNode) || !stack.contains(leftNode))) {
				stack.add(leftNode);
			}
		}
		return new Node(); 
	}
}
