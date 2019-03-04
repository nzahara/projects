

public class UniformCostSearch {
	
	
	static class Node <T>{
		
		private T value;
		private int totalPathCost;
		private int pathCost;
		private SinglyLinkedList<T> adjacentNodes;
		
		Node( T data){
			this.value = data;
		}
		
		public Node(T data, int pathCost) {
			this.value = data;
			this.pathCost = pathCost;
		}

		public Node() {
		}

		public T getValue() {
			return value;
		}

		public int getPathCost() {
			return pathCost;
		}

		public int getTotalPathCost() {
			return totalPathCost;
		}

		public void setTotalPathCost(int totalPathCost) {
			this.totalPathCost = totalPathCost;
		}

		public SinglyLinkedList<T> getAdjacentNodes() {
			if(adjacentNodes == null) {
				return new SinglyLinkedList<T>();
			}
			return adjacentNodes;
		}

		public void setAdjacentNodes(SinglyLinkedList<T> adjacentNodes) {
			System.out.println("Nodes :"+ adjacentNodes);
			this.adjacentNodes = adjacentNodes;
		}
		
	}
	
	static Node rootNode = null ;
	
	public static <T> void main(String[] args) {
		
		rootNode = new Node("S");
		Node node2 = new Node("B");
		Node node3 = new Node("D");
		Node node4 = new Node("E");
		Node node5 = new Node("C");
		Node node6 = new Node("F");
		Node node7 = new Node("H");
		Node node8 = new Node("G");
		
		
		SinglyLinkedList<Node> adjacent0 = new SinglyLinkedList<Node>();
		adjacent0.add(new Node(node2, 5));
		adjacent0.add(new Node(node3, 6));
		rootNode.setAdjacentNodes(adjacent0);
		
		SinglyLinkedList<Node> adjacent1 = new SinglyLinkedList<Node>();
		adjacent1.add(new Node(node5, 1));
		adjacent1.add( new Node(node4, 2));
		node2.setAdjacentNodes(adjacent1);
		
		SinglyLinkedList<Node> adjacent2 = new SinglyLinkedList<Node>();
		adjacent2.add(new Node(node4, 5));
		adjacent2.add( new Node(node8, 5));
		node3.setAdjacentNodes(adjacent2);
		
		SinglyLinkedList<Node> adjacent3 = new SinglyLinkedList<Node>();
		adjacent3.add( new Node(node6, 4));
		adjacent3.add(new Node(node8, 3));
		node4.setAdjacentNodes(adjacent3);
		
		SinglyLinkedList<Node> adjacent4 = new SinglyLinkedList<Node>();
		adjacent4.add(new Node(node6, 3));
		node5.setAdjacentNodes(adjacent4);
		
		SinglyLinkedList<Node> adjacent5 = new SinglyLinkedList<Node>();
		adjacent5.add(new Node(node8, 7));
		adjacent5.add(new Node(node7, 1));
		node6.setAdjacentNodes(adjacent5);
		
		SinglyLinkedList<Node> adjacent6 = new SinglyLinkedList<Node>();
		adjacent6.add(new Node(node8, 1));
		node7.setAdjacentNodes(adjacent6);
		
		
		Node<T> elementFound = uniformCostSearch("F");
		System.out.println("Element Found :" + elementFound + " :" + elementFound.getValue() + ":" +elementFound.getTotalPathCost());
	}
	
	/**
	 *  The function chooses the least path cost child and expand.
	 *  Also if the node is already in visited and has more path cost than the current ,
	 *  move the node to queue and update the path cost.
	 *  
	 * @param data
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Node uniformCostSearch(T data) {
		PriorityQueue queue = new PriorityQueue();
		PriorityQueue visitedQueue = new PriorityQueue();
		queue.push(rootNode, rootNode.getTotalPathCost());
		while (queue.size() > 0) {
			Object peek = queue.peek();
			int firstElementPriority = queue.getPriority(peek);
			Node<T> exploringElement = (Node<T>) queue.pop();
			visitedQueue.push(exploringElement, firstElementPriority);
			System.out.println("Element being explored:" + exploringElement.getValue());
			if (exploringElement.getValue().equals(data)) {
				return exploringElement;
			}
			System.out.println("Child size:" +exploringElement.getAdjacentNodes().size());
			for (int index = 0; index < exploringElement.getAdjacentNodes().size(); index++) {
				Node childNode = (Node) exploringElement.getAdjacentNodes().get(index);
				Node node = (Node) childNode.getValue();
				int totalPathCost = childNode.getPathCost() + firstElementPriority;
				// check if it is already in visisted/queue
				if (!queue.contains(node) && !visitedQueue.contains(node)) {
					node.setTotalPathCost(totalPathCost);
					queue.push(node, totalPathCost);
					continue;
				}
				// check if the current path cost is lesser than what exists
				if (queue.get(node).equals(null)) {
					int existingPathCost = visitedQueue.get(node).getPriority();
					if (totalPathCost < existingPathCost) {
						queue.push(node, totalPathCost);
						visitedQueue.remove(node);
						queue.push(node, totalPathCost);
					} else {
						node.setTotalPathCost(existingPathCost);
					}
					continue;
				}
				
				int existingPathCost = queue.get(node).getPriority();
				if (totalPathCost < existingPathCost) {
					queue.push(node, totalPathCost);
					queue.remove(node);
					queue.push(node, totalPathCost);
				} else {
					node.setTotalPathCost(existingPathCost);
				}
				continue;
			}
		}
		return new Node<T>();
	}

}
