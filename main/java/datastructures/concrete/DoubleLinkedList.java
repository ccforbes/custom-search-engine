// Hiral Patel and Christofer Conrad Forbes

package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
	// You may not rename these fields or change their types.
	// We will be inspecting these in our private tests.
	// You also may not add any additional fields.
	private Node<T> front;
	private Node<T> back;
	private int size;

	public DoubleLinkedList() {
		this.front = null;
		this.back = null;
		this.size = 0;
	}

	/**
     * Adds the given item to the *end* of this IList.
     */
	@Override
	public void add(T item) {
		Node<T> tmp = new Node<T>(back,item, null); 		
		if (front == null) { 
			front = tmp;
		}
		if (back != null) {
			back.next = tmp;
		}
		back = tmp;
		size++;
	}

	/**
     * Removes and returns the item from the *end* of this IList.
     *
     * @throws EmptyContainerException if the container is empty and there is no element to remove.
     */
	@Override
	public T remove() {
		if (size == 0) { 
			throw new EmptyContainerException(); 
		}
		Node<T> tmp = back;
		back = back.prev;

		if (back!= null) {
			back.next = null;
		}
		size--;
		return tmp.data;
	}

	/**
     * Returns the item located at the given index.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
	@Override
	public T get(int index) {
		if (index < 0 || index >= this.size()) { 
			throw new IndexOutOfBoundsException();
		}

		Node<T> tmp = front;  
		int count = 0;
		while (count != index) {
			tmp = tmp.next;
			count++;
		}
		return tmp.data;
	}

	 /**
     * Overwrites the element located at the given index with the new item.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
	@Override
	public void set(int index, T item) {
		if (index < 0 || index >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {	//change the front node
			Node<T> newNode = new Node<T>(null, item, front.next);
			front = newNode;
			if (front.next != null) {
				front.next.prev = newNode;
			}
		}else {
			int count = 0;
			Node<T> tmp = front; 			 
			while (count != index-1) {
				tmp = tmp.next;
				count++;
			}
			Node<T> newNode = new Node<T>(tmp, item, tmp.next.next);
			tmp.next = newNode;

			if(tmp.next.next != null) {
				tmp.next.next.prev = newNode;
			}
		}
	}

	 /**
     * Inserts the given item at the given index. If there already exists an element
     * at that index, shift over that element and any subsequent elements one index
     * higher.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
     */
	@Override
	public void insert(int index, T item) {		
		if ( index < 0 || index >= this.size()+1) { 
			throw new IndexOutOfBoundsException(); 
		}

		if (index == 0) {
			Node<T> newNode = new Node<T>(null, item, front);
			front = newNode;
			if (this.size() == 0) {
				back = newNode;
			} else {
				front.next.prev = newNode;
			}
		}
		else if (index == this.size()) {
			Node<T> newNode = new Node<T>(back, item, null);
			back.next = newNode;
			back = newNode;
		}
		else {
			Node<T> tmp = front;
			if (index > this.size()/2) {
				tmp = back;
				int count = this.size()-1;
				while (count != index-1) {
					tmp = tmp.prev;
					count--;
				}	
			} else {
				int count = 0;
				while (count != index-1) {
					tmp = tmp.next;
					count++;
				}   
			}         	       	
			Node<T> newNode = new Node<T>(item);
			Node<T> tmp2 = tmp.next;
			tmp.next = newNode;
			newNode.next = tmp2;
			newNode.prev = tmp;
			tmp2.prev = newNode;
		}
		size ++;
	}

	/**
     * Deletes the item at the given index. If there are any elements located at a higher
     * index, shift them all down by one.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
	@Override
	public T delete(int index) {
		if (this.size() == 0) {
			throw new EmptyContainerException();
		}
		if (index < 0 || index >= this.size()) {
			throw new IndexOutOfBoundsException();
		}

		T data = null;
		if (index == 0) {		//deletes the front node
			data = front.data;
			front = front.next;
			if (front != null) {
				front.prev = null;
			}
		} else if (index == this.size() - 1) {	//deletes the back node
			data = back.data;
			back = back.prev;
			back.next = null;
		} else {
			Node<T> tmp = front;
			if (index > this.size()/2) {		//places tmp to node before 
				tmp = back;
				int count = this.size()-1;
				while (count != index-1) {
					tmp = tmp.prev;
					count--;
				}	
			} else {
				int count = 0;
				while (count != index-1) {
					tmp = tmp.next;
					count++;
				}   
			}         	       	
			Node<T> temp2 = tmp.next.next;
			tmp.next = temp2;
			temp2.prev = tmp;
		}
		size--;
		return data;	
	}

	/**
     * Returns the index corresponding to the first occurrence of the given item
     * in the list.
     *
     * If the item does not exist in the list, return -1.
     */
	@Override
	public int indexOf(T item) {
		if (size == 0) {
			return -1;
		} else {
			Node<T> tmp = front;  
			int counter = 0; 
			while (tmp != null) {
				if (item != null && tmp.data.equals(item)) { 
					return counter;
				} else if (item == tmp.data){
					return counter;
				} 
				else {
					tmp = tmp.next;  
					counter++;
				}
			}
			return -1;
		}  
	}

	/**
     * Returns the number of elements in the container.
     */
	@Override
	public int size() {
		return this.size;
	}

	/**
     * Returns 'true' if this container contains the given element, and 'false' otherwise.
     */
	@Override
	public boolean contains(T other) {
		boolean isContained = false;
		if (size == 0) {
			return isContained;
		} else {
			return indexOf(other) != -1;
		}  	
	}

	@Override
	public Iterator<T> iterator() {
		// Note: we have provided a part of the implementation of
		// an iterator for you. You should complete the methods stubs
		// in the DoubleLinkedListIterator inner class at the bottom
		// of this file. You do not need to change this method.
		return new DoubleLinkedListIterator<>(this.front);
	}

	private static class Node<E> {
		// You may not change the fields in this node or add any new fields.
		public final E data;
		public Node<E> prev;
		public Node<E> next;

		public Node(Node<E> prev, E data, Node<E> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}

		public Node(E data) {
			this(null, data, null);
		}

		// Feel free to add additional constructors or methods to this class.

	}

	private static class DoubleLinkedListIterator<T> implements Iterator<T> {
		// You should not need to change this field, or add any new fields.
		private Node<T> current;

		public DoubleLinkedListIterator(Node<T> current) {
			// You do not need to make any changes to this constructor.
			this.current = current;
		}

		/**
		 * Returns 'true' if the iterator still has elements to look at;
		 * returns 'false' otherwise.
		 */
		public boolean hasNext() {
			return current != null;
		}

		/**
		 * Returns the next item in the iteration and internally updates the
		 * iterator to advance one element forward.
		 *
		 * @throws NoSuchElementException if we have reached the end of the iteration and
		 *         there are no more elements to look at.
		 */
		public T next() {
			if (!hasNext()) throw new NoSuchElementException();
			T tempData = current.data;
			current = current.next;
			return tempData;
		}
	} 
}
