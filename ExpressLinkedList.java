import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Quadruply-linked list implementation of the {@code List}.
 * This variation of the linked list which implements the list interface,
 * will allow for faster traversal of a list compared to a singly or doubly linked-list.
 * It implements the 7 methods add(E e), add(int index, E element), remove(int index)
 * , get(int index), size(), clear(), and toString(). It will behave just like a linked-list.
 *
 * @author Ariyan Sahebghalam, Eyosyas Andarge, Sadiq Azmi
 * @param <E> elements type held in this collection
 */
public class ExpressLinkedList<E> implements List<E>  {

	private static class Node<E> {
		E element;
		Node<E> nextShort;
		Node<E> prevShort;
		Node<E> nextLong;
		Node<E> prevLong;

		public Node(E element) {
			this.element = element;
		}
	}

	private Node<E> head;

	private Node<E> tail;

	private int size;

	/**
	 * Constructor that creates an empty list
	 */
	public ExpressLinkedList() {
		size = 0;
	}

	/**
	 * Appends a given Object to the end of the list
	 * @param e This is the given item, it should be of the correct generic type
	 * @return Returns true if the element was added correctly, false otherwise.
	 */
	public boolean add(E e) {
		Node<E> node = new Node<>(e);
		if (size == 0) {
			head = node;
			tail = node;
		} else {
			tail.nextShort = node;
			node.prevShort = tail;
			if (size > 7) {
				Node<E> tempNode = tail;
				for (int i = 0; i < 7; i++) {
					tempNode = tempNode.prevShort;
				}
				tempNode.nextLong = node;
				node.prevLong = tempNode;
			}
			tail = node;
		}
		size++;
		return true;
	}

	/**
	 * Inserts the specified element at the given index position in the list.
	 * Shifts the element at the index given, and its following elements to its right to
	 * the right by adding one to their indices. This is the Quadruply LinkedList implementation of the
	 * method, which is faster for traversal through list than a normal LinkedList or Doubly LinkedList.
	 * It is faster by making use of a pointer in each node cell that stores the address of 8 nodes ahead and
	 * behind.
	 *
	 * @param index The index at which the element must be inserted
	 * @param element The element to be inserted into the specified index
	 * @throws IndexOutOfBoundsException In case the index given is less than 0 or bigger than size
	 */
	@Override
	public void add(int index, E element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		if (index == size) {
			add(element);
			return;
		}

		Node<E> node = new Node<>(element);
		Node<E> current = getNode(index);

		if (index == 0) {
			node.nextShort = head;
			head.prevShort = node;
			head = node;
		} else {
			Node<E> prev = current.prevShort;
			node.nextShort = current;
			current.prevShort = node;
			node.prevShort = prev;
			prev.nextShort = node;
			if (index > 8) {
				node.prevLong = current.prevLong;
				current.prevLong.nextLong = node;
			} else if (index == 8) {
				node.prevLong = head;
			}
		}

		// update long links for the 8 nodes before and after the new node
		Node<E> tempNode = node.prevShort;
		for (int i = 1; i <= 8 && tempNode != null; i++) {
			Node<E> longNextNode = tempNode;
			for (int j = 0; j < 8 && longNextNode != null; j++) {
				longNextNode = longNextNode.nextShort;
			}
			tempNode.nextLong = longNextNode;
			if (longNextNode != null) {
				longNextNode.prevLong = tempNode;
			}
			tempNode = tempNode.prevShort;
		}

		tempNode = node.nextShort;
		for (int i = 1; i <= 8 && tempNode != null; i++) {
			Node<E> longPrevNode = tempNode;
			for (int j = 0; j < 8 && longPrevNode != null; j++) {
				longPrevNode = longPrevNode.prevShort;
			}
			tempNode.prevLong = longPrevNode;
			if (longPrevNode != null) {
				longPrevNode.nextLong = tempNode;
			}
			tempNode = tempNode.nextShort;
		}

		size++;
	}


	/**
	 * Remove an item from the list at the given index. Shifts the following
	 * elements to the left of the index to the left by subtracting one from their indices.
	 * This is the Quadruply LinkedList implementation of the method, which is faster for traversal through list than
	 * a normal LinkedList or Doubly LinkedList. It is faster by making use of a pointer in each node cell
	 * that stores the address of 8 nodes ahead and behind.
	 *
	 * @param index	Index of the element to be removed
	 * @return The element given by the index that was supposed to be removed
	 * @throws IndexOutOfBoundsException In case the index given is less than 0 or bigger than or equal to size
	 */
	@Override
	public E remove(int index) {

		// In case the index was out of bounds
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node<E> pointer = null;
		int count;

		// Start traversing from the head if the index was closer to the head
		if (index < size / 2) {
			pointer = head;
			count = 0;

			// Express route
			while ((count + 8) <= index) {
				pointer = pointer.nextLong;
				count += 8;
			}

			// Normal route
			while (count != index) {
				pointer = pointer.nextShort;
				count++;
			}
		}
		// Start traversing from the tail if the index was closer to the tail
		else {
			pointer = tail;
			// Counter used to measure proximity to the specified index
			count = size - 1;

			// Express route
			while ((count - 8) >= index) {
				pointer = pointer.prevLong;
				count -= 8;
			}

			// Normal route
			while (count != index) {
				pointer = pointer.prevShort;
				count--;
			}
		}

		E data = pointer.element;

		// Pointer to the left and right of the about to be removed index, to fix the previousLong
		// and nextLong references of individual nodes
		Node<E> next = pointer.nextShort;
		Node<E> prev = pointer.prevShort;

		// In case no node to the left
		if (prev == null) {
			head = next;
		}
		else {
			prev.nextShort = next;
			pointer.prevShort = null;
			//
			pointer.prevLong = null;
		}

		// In case no node to the right
		if (next == null) {
			tail = prev;
		}
		else {
			next.prevShort = prev;
			pointer.nextShort = null;
			//
			pointer.nextLong = null;
		}

		// Fixing the possible next 8 nodes or less references after the nodes removal
		count = 1;
		while (count <= 8 && next != null) {
			if (next.prevLong == null) {
				next.prevLong = null;
			}
			else if (next.prevLong.prevShort == null) {
				next.prevLong = null;
			}
			else {
				next.prevLong = next.prevLong.prevShort;
			}

			if (count == 8) {
				next.prevLong = next.prevShort.prevShort.prevShort.prevShort.prevShort.prevShort.prevShort.prevShort;
			}
			count++;
			next = next.nextShort;
		}


		// Fixing the possible previous 8 nodes or less references after the node removal
		count = 1;
		while (count <= 8 && prev != null) {
			if (prev.nextLong == null) {
				prev.nextLong = null;
			}
			else if (prev.nextLong.nextShort == null) {
				prev.nextLong = null;
			}
			else {
				prev.nextLong = prev.nextLong.nextShort;

			}

			if (count == 8) {
				prev.nextLong = prev.nextShort.nextShort.nextShort.nextShort.nextShort.nextShort.nextShort.nextShort;
			}
			count++;
			prev = prev.prevShort;
		}


		// Decreasing the list size by 1
		size--;

		return data;
	}




	/**
	 * Returns the element at the given index in the list, but with the faster
	 * traversal of the Quadruply LinkedList implementation.
	 *
	 * @param index	Index of the element to be returned
	 * @return Returns the element corresponding to the given index
	 * @throws IndexOutOfBoundsException In case the index given is less than 0 or bigger than or equal to size
	 */
	public E get(int index) {
		// Returns the element at the specified index

		Node<E> pointer = null;

		// Out of bounds
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		// Start from head
		if (index < size / 2) {
			int count = 0;
			pointer = head;

			// Express route
			while ((count + 8) <= index) {
				pointer = pointer.nextShort;
				count += 8;
			}

			// Normal route
			while (count != index) {
				pointer = pointer.nextShort;
				count++;
			}

			return pointer.element;
		}
		// Start from tail
		else {
			int count = size - 1;
			pointer = tail;

			// Express route
			while ((count - 8) >= index) {
				pointer = pointer.prevLong;
				count -= 8;
			}

			// Normal route
			while (count != index) {
				pointer = pointer.prevShort;
				count--;
			}

			return pointer.element;
		}

	}



	/**
	 * This is a helper method that helps modularize the code, and returns
	 * the reference to a node given by the index.
	 *
	 * @param index The index of the node to be returned
	 * @return Returns the reference to the node given by the index
	 */
	private Node<E> getNode(int index) {
		Node<E> current;
		if (index < size / 2) {
			current = head;
			for (int i = 0; i < index; i++) {
				current = current.nextShort;
			}
		} else {
			current = tail;
			for (int i = size - 1; i > index; i--) {
				current = current.prevShort;
			}
		}
		return current;
	}


	/**
	 * Returns the size of the list, meaning how many nodes are in the list.
	 *
	 * @return The integer number of elements in the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Empties the list after the call and makes the size 0
	 */
	public void clear() {
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * Returns a String representation of the Quadruply LinkedList object.
	 *
	 * @return String representation of the Quadruply LinkedList object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Node<E> current = head;
		while (current != null) {
			sb.append(current.element.toString());
			current = current.nextShort;
			if (current != null) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}







	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}



	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}


	@Override
	public E set(int index, E element) {
		// TODO Auto-generated method stub]
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}