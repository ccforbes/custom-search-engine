/*
 * Name: Chris Forbes
 * Date: 11/15/2017
 * File: ArrayHeap.java
 */
package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_CAPACITY = 21;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heap = makeArrayOfT(DEFAULT_CAPACITY);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    public T removeMin() {
    	if (this.isEmpty()) {
        	throw new EmptyContainerException();
        }
    	T value = peekMin();
    	this.heap[1] = this.heap[this.size];
    	this.heap[size] = null;
    	this.size--;
    	percolateDown();
    	return value;
    }

    public T peekMin() {
        if (this.isEmpty()) {
        	throw new EmptyContainerException();
        }
        return heap[1];
    }

    public void insert(T item) {
        if (item == null) {
        	throw new IllegalArgumentException();
        }
        if (size >= heap.length - 1) {
        	this.heap = this.resize();
        }
        this.size++;
        heap[this.size] = item;
        this.percolateUp();
    }

    public int size() {
        return this.size;
    }
    
    private void percolateUp() {
    	int index = this.size();
    	while (hasParent(index) && this.heap[index].compareTo(this.parent(index)) < 0) {
    		this.swap(index, parentIndex(index));
    		index = this.parentIndex(index);
    	}
    }
    
    private void percolateDown() {
    	int index = 1;
    	while (hasChild1(index)) {
    		int wantedIndex = child1Index(index);
    		if (hasChild2(index) &&
        			this.heap[wantedIndex].compareTo(this.heap[child2Index(index)]) > 0) {
        		wantedIndex = child2Index(index);
        	}
        	if (hasChild3(index) && 
        			this.heap[wantedIndex].compareTo(this.heap[child3Index(index)]) > 0) {
        		wantedIndex = child3Index(index);
        	}
        	if (hasChild4(index) && 
        			this.heap[wantedIndex].compareTo(this.heap[child4Index(index)]) > 0) {
        		wantedIndex = child4Index(index);
        	}
        	
        	if (this.heap[index].compareTo(this.heap[wantedIndex]) > 0) {
        		this.swap(index, wantedIndex);
        	} else {
        		break;
        	}
        	index = wantedIndex;
    	}
    }
    
    private void swap(int index1, int index2) {
    	T temp = this.heap[index1];
    	this.heap[index1] = this.heap[index2];
    	this.heap[index2] = temp;
    }
    
    private T[] resize() {
    	return Arrays.copyOf(this.heap, this.heap.length * NUM_CHILDREN);
    }
    
    /*
     * Returns the index of the parent
     * */
    private int parentIndex(int i) {
    	return (i + 2) / NUM_CHILDREN;
    }
    
    /*
     * Returns the index of the first child (children 1 through 4 from 
     * left to right)
     * */
    private int child1Index(int i) {
    	return (NUM_CHILDREN * i) - 2;
    }
    
    /*
     * Returns the index of the second child (children 1 through 4 from 
     * left to right)
     * */
    private int child2Index(int i) {
    	return (NUM_CHILDREN * i) - 1;
    }
    
    /*
     * Returns the index of the third child (children 1 through 4 from 
     * left to right)
     * */
    private int child3Index(int i) {
    	return NUM_CHILDREN * i;
    }
    
    /*
     * Returns the index of the fourth child (children 1 through 4 from 
     * left to right)
     * */
    private int child4Index(int i) {
    	return (NUM_CHILDREN * i) + 1;
    }
    
    /*
     * Returns true if there is a parent
     * */
    private boolean hasParent(int i) {
    	return i > 1;
    }
    
    /*
     * Returns true if first child exists(children 1 through 4 from 
     * left to right)
     * */
    private boolean hasChild1(int i) {
    	return child1Index(i) <= this.size;
    }
    
    /*
     * Returns true if a second child exists (children 1 through 4 from 
     * left to right)
     * */
    private boolean hasChild2(int i) {
    	return child2Index(i) <= this.size;
    }
    
    /*
     * Returns true a third child exists(children 1 through 4 from 
     * left to right)
     * */
    private boolean hasChild3(int i) {
    	return child3Index(i) <= this.size;
    }
    
    /*
     * Returns true if fourth child exists (children 1 through 4 from 
     * left to right)
     * */
    private boolean hasChild4(int i) {
    	return child4Index(i) <= this.size;
    }
    
    /*
     * Returns the value of the parent
     * */
    private T parent(int i) {
    	return this.heap[parentIndex(i)];
    }
}
