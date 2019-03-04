

public class DepthLimitedAndIterativeDeepeningSearch {
	
	private static class Node {
		
		private int data;
		private SinglyLinkedList<Node> adjacentNodes;
		private int depth;
		
		private Node(int data) {
			this.data = data;
		}
		private Node() {
		}
		public int getData() {
			return data;
		}
		public SinglyLinkedList<Node> getAdjacentNodes() {
			if(adjacentNodes == null) {
				return new SinglyLinkedList<>();
			}
			return adjacentNodes;
		}
		public void setAdjacentNodes(SinglyLinkedList<Node> adjacentNodes) {
			this.adjacentNodes = adjacentNodes;
		}
		public int getDepth() {
			return depth;
		}
		public void setDepth(int depth) {
			this.depth = depth;
		}
	}
	
	static Node rootNode = null;
	
	public static void main(String[] args) {
		
		rootNode = new Node(1);
		
		Node node1 = new Node(4);
		Node node2 = new Node(5);
		SinglyLinkedList<Node> adjacentNode0 = rootNode.getAdjacentNodes();
		adjacentNode0.add(node1);
		adjacentNode0.add(node2);
		rootNode.setAdjacentNodes(adjacentNode0);
		
		Node node3 = new Node(10);
		Node node4 = new Node(12);
		SinglyLinkedList<Node> adjacentNode1 = node1.getAdjacentNodes();
		adjacentNode1.add(node3);
		adjacentNode1.add(node4);
		node1.setAdjacentNodes(adjacentNode1);
		
		Node node5 = new Node(8);
		Node node6 = new Node(7);
		SinglyLinkedList<Node> adjacentNode2 = node2.getAdjacentNodes();
		adjacentNode2.add(node5);
		adjacentNode2.add(node6);
		node2.setAdjacentNodes(adjacentNode2);
		
		Node node7 = new Node(0);
		Node node8 = new Node(14);
		SinglyLinkedList<Node> adjacentNode3 = node6.getAdjacentNodes();
		adjacentNode3.add(node7);
		adjacentNode3.add(node8);
		node6.setAdjacentNodes(adjacentNode3);
//		Node result = depthLimitSearch(8, 2);
//		System.out.println("Result of dls:" +result);
		Node result = iterativeDeepeningSearch(14);
		System.out.println("Result of ids:" +result);
		
	}
	
	/**
	 * The function implements Depth First Search  algorithm for the mentioned depth.
	 * 
	 * @param data
	 * @param depthLimit
	 * @return
	 */
	public static Node depthLimitSearch(int data, int depthLimit) {
		SinglyLinkedList<Node> stack = new SinglyLinkedList<Node>();
		SinglyLinkedList<Node> visitedQueue = new SinglyLinkedList<Node>();
		stack.add(rootNode);
		while(stack.size() > 0) {
			Node exploringElement = (Node) stack.pop();
			visitedQueue.add(exploringElement);
			System.out.println("The exploring element is:" + exploringElement.getData());
			if(exploringElement.getData() == data) {
				return exploringElement;
			}
			SinglyLinkedList<Node> adjacentElements = exploringElement.getAdjacentNodes();
			for (int i = 0 ; i< adjacentElements.size(); i++) {
				Node node = (Node) adjacentElements.get(i);
				node.setDepth(exploringElement.getDepth()+1);
				if(node.getDepth() <= depthLimit && (!visitedQueue.contains(node) || !stack.contains(node))) {
					stack.add(node);
				}
			}
		}
		return null;
	}
	
	/**
	 * The function implements Iterative Deepening Search Algorithm.
	 * 
	 * @param data
	 * @return
	 */
	public static Node iterativeDeepeningSearch(int data) {
		int depth = 0;
		Node elementFound = null;
		while (elementFound == null || elementFound.getData() != data) {
			elementFound = depthLimitSearch(data, depth);
			System.out.println("Depth :" +depth);
			depth++;
		}
		return elementFound;
	}
	
}
