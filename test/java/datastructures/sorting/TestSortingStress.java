/*
 * Name: Chris Forbes
 * Date: 11/15/2017
 * File: TestSortingStress.java
 */
package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout=10*SECOND)
    public void stressTestArrayHeap() {
    	IPriorityQueue<Integer> heap = new ArrayHeap<Integer>();
    	int limit = 1000000;
    	
    	for (int i = 0; i < limit; i++) {
    		heap.insert(i);
    	}
    	
    	for (int i = 0; i < limit; i++) {
    		assertEquals(i, heap.peekMin());
    		assertEquals(i, heap.removeMin());
    	}
    	
    	for (int i = 0; i < limit; i++) {
    		heap.insert(-i);
    	}
    	
    	for (int i = limit - 1; i >= 0; i--) {
    		assertEquals(-i, heap.peekMin());
    		assertEquals(-i, heap.removeMin());
    	}
    	
    	for (int i = 0; i < limit; i++) {
    		if (i % 2 == 0) {
    			heap.insert(-i);
    		} else {
    			heap.insert(i);
    		}
    	}
    	
    	for (int i = limit - 2; i >= 0; i-=2) {
    		assertEquals(-i, heap.peekMin());
    		assertEquals(-i, heap.removeMin());
    	}
    	for (int i = 1; i < limit; i+=2) {
    		assertEquals(i, heap.peekMin());
    		assertEquals(i, heap.removeMin());
    	}
    }
    
    @Test(timeout=10*SECOND)
    public void stressTestTopKSort() {
    	IList<Integer> list1 = new DoubleLinkedList<>();
    	IList<Integer> list2 = new DoubleLinkedList<>();
    	IList<Integer> list3 = new DoubleLinkedList<>();
    	int limit = 1000000;
    	
    	for (int i = limit - 1; i >= 0; i--) {
    		list1.add(i);
    	}
    	
    	IList<Integer> top1 = Searcher.topKSort(limit, list1);
    	assertEquals(limit, top1.size());
    	int counter = 0;
    	for (int values : top1) {
    		assertEquals(counter, values);
    		counter++;
    	}
    	
    	for (int i = 0; i < limit; i++) {
    		list2.add(-i);
    	}
    	
    	IList<Integer> top2 = Searcher.topKSort(limit, list2);
    	assertEquals(limit, top2.size());
    	counter = -999999;
    	for (int values : top2) {
    		assertEquals(counter, values);
    		counter++;
    	}
    	
    	for (int i = 0; i < limit; i++) {
    		if (i % 2 == 0) {
    			list3.add(-i);
    		} else {
    			list3.add(i);
    		}
    	}
    	IList<Integer> top3 = Searcher.topKSort(limit, list3);
    	assertEquals(limit, top3.size());
    	counter = -999998;
    	int counter1 = 1;
    	for (int values : top3) {
    		if (counter % 2 == 0 && counter <= 0) {
    			assertEquals(counter, values);
    			counter+=2;
    		} else {
    			assertEquals(counter1, values);
    			counter1+=2;
    		}
    		
    	}
    }
}
