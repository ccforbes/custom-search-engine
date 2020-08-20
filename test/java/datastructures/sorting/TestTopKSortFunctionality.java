/*
 * Name: Chris Forbes
 * Date: 11/15/2017
 * File: TestTopKSortFunctionality.java
 */
package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKIsLargerOrEqualThanList() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	for (int i = 0; i < 20; i++) {
    		list.add(i);
    	}
    	
    	IList<Integer> top = Searcher.topKSort(20, list);
    	assertEquals(20, top.size());
    	for (int i = 0; i < top.size(); i++) {
    		assertEquals(0 + i, top.get(i));
    	}
    	
    	top = Searcher.topKSort(25, list);
    	assertEquals(20, top.size());
    	for (int i = 0; i < top.size(); i++) {
    		assertEquals(0 + i, top.get(i));
    	}
    }
    
    @Test(timeout=SECOND)
    public void testInvalidK() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	list.add(1);
    	list.add(2);
    	list.add(3);
    	
        try {
        	IList<Integer> top = Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
        
        try {
        	IList<Integer> top = Searcher.topKSort(-1000, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testKIsZero() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	list.add(1);
    	list.add(2);
    	list.add(3);
    	
    	IList<Integer> top = Searcher.topKSort(0, list);
    	assertTrue(top.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testLargeInputList() {
    	IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 1; i < 750; i++) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(100, list);
    	assertEquals(100, top.size());
    	for (int i = 0; i < top.size(); i++) {
    		assertEquals(650 + i, top.get(i));
    	}
    }
    
    @Test(timeout=SECOND)
    public void testLargeInputListIsReversed() {
    	IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 749; i >= 0; i--) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(100, list);
    	assertEquals(100, top.size());
    	for (int i = 0; i < top.size(); i++) {
    		assertEquals(650 + i, top.get(i));
    	}
    }
    
    @Test(timeout=SECOND)
    public void testListWithOneElement() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	list.add(100);
    	
    	IList<Integer> top = Searcher.topKSort(1, list);
    	assertEquals(1, top.size());
    	assertEquals(100, top.get(0));
    	
    	top = Searcher.topKSort(100, list);
    	assertEquals(1, top.size());
    	assertEquals(100, top.get(0));
    }
    
    @Test(timeout=SECOND)
    public void testInputListIsEmpty() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	
    	IList<Integer> top = Searcher.topKSort(5, list);
    	assertTrue(top.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testInputListIsTheSame() {
    	IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 749; i >= 0; i--) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(100, list);
    	assertEquals(100, top.size());
    	for (int i = 0; i < top.size(); i++) {
    		assertEquals(650 + i, top.get(i));
    	}
    	
    	int index = 0;
    	for (int i = 749; i >= 0; i--) {
    		assertEquals(i, list.get(index));
    		index++;
    	}
    }
    
    @Test(timeout=SECOND)
    public void testLargeInputAlternatingList() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	for (int i = 0; i < 750; i++) {
    		if (i % 2 == 0) {
    			list.add(-i);
    		} else {
    			list.add(i);
    		}
    	}
    	
    	IList<Integer> top = Searcher.topKSort(100, list);
    	int index = 0;
    	for (int i = 551; i <= 749; i+=2) {
    		assertEquals(i, top.get(index));
    		index++;
    	}
    	
    	int counter1 = 0;
    	int counter2 = 1;
    	for (int i = 0; i < 750; i++) {
    		if (i % 2 == 0) {
    			assertEquals(counter1, list.get(i));
    			counter1 -= 2;
    		} else {
    			assertEquals(counter2, list.get(i));
    			counter2 += 2;
    		}
    	}
    }
}
