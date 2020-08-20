/*
 * Name: Chris Forbes
 * Date: 11/15/2017
 * File: TestArrayHeapFunctionality.java
 */
package datastructures.sorting;


import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;



/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    public IPriorityQueue<Integer> makeLargeHeap() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	int cap = 750;
    	for (int i = 1; i <= cap; i++) {
    		heap.insert(i);
    	}
    	return heap;
    }
    
    public IPriorityQueue<Integer> makeLargeNonSortedHeap() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	int cap = 750;
    	for (int i = 1; i <= cap; i++) {
    		heap.insert(-i);
    	}
    	return heap;
    }
    
    public IPriorityQueue<Integer> makeLargeAlternatingHeap() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	int cap = 750;
    	for (int i = 1; i <= cap; i++) {
    		if (i % 2 == 0) {
    			heap.insert(-i);
    		} else {
    			heap.insert(i);
    		}
    	}
    	return heap;
    }
    
    public IPriorityQueue<Integer> makeBasicHeap() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(5);
        heap.insert(2);
        heap.insert(1);
        heap.insert(3);
        heap.insert(4);
    	return heap;
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinBasic() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        assertEquals(5, heap.size());
        for (int i = 1; i <= 5; i++) {
        	assertEquals(i, heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testEmptyPeekErrorHandling() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	
    	try {
    		heap.peekMin();
    		fail("Expected EmptyContainerException");
    	} catch (EmptyContainerException ex) {
    			// Do nothing: this is ok
    	}
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinErrorHandling() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	
    	try {
    		heap.removeMin();
    		fail("Expected EmptyContainerException");
    	} catch (EmptyContainerException ex) {
    			// Do nothing: this is ok
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInvalidInsertErrorHandling() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	
    	try {
    		heap.insert(null);
    		fail("Expected IllegalArgumentException");
    	} catch (IllegalArgumentException ex) {
    			// Do nothing: this is ok
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveManyRepeatedIsEfficient() {
    	IPriorityQueue<Integer> heap1 = this.makeLargeHeap();
    	IPriorityQueue<Integer> heap2 = this.makeLargeHeap();
    	IPriorityQueue<Integer> heap3 = this.makeLargeHeap();
    	//IPriorityQueue<Integer> heap3 = this.makeLargeNonSortedHeap();
    	
    	assertEquals(750, heap1.size());
    	assertEquals(750, heap2.size());
    	assertEquals(750, heap3.size());
    	
    	for (int i = 1; i <= 750; i++) {
    		assertEquals(i, heap1.removeMin());
    		assertEquals(i, heap2.removeMin());
    		assertEquals(i, heap3.removeMin());
    	}
    	//for (int i = 750; i > 0; i--) {
    	//	assertEquals(-i, heap3.removeMin());
    	//}
    	assertTrue(heap1.isEmpty());
    	assertTrue(heap2.isEmpty());
    	assertTrue(heap3.isEmpty());
    } 
    
    @Test(timeout=SECOND)
    public void testInsertAndRemoveMinAlternating() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	
    	for (int i = 0; i < 750; i++) {
    		heap.insert(i);
    		assertEquals(i, heap.removeMin());
    		assertTrue(heap.isEmpty());
    	}
    	
    	assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndPeekMinAlternating() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	
    	for (int i = 0; i < 750; i++) {
    		heap.insert(i);
    		assertEquals(0, heap.peekMin());
    		assertFalse(heap.isEmpty());
    	}
    	
    	assertFalse(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testLargeHeapsWithNonSortedInserts() {
    	IPriorityQueue<Integer> reverseSortedHeap = this.makeLargeNonSortedHeap();
    	IPriorityQueue<Integer> alternatingHeap = this.makeLargeAlternatingHeap();
    	
    	for (int i = 750; i > 0; i--) {
    		assertEquals(-i, reverseSortedHeap.removeMin());
    	}
    	for (int i = 750; i > 0; i-=2) {
    		assertEquals(-i, alternatingHeap.removeMin());
    	}
    	for (int i = 1; i <= 749; i+=2) {
    		assertEquals(i, alternatingHeap.removeMin());
    	}
    }
}
