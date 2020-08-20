/****************************************************************************************
* Chris Forbes
* 10/26/2017
* CSE373
* TA: Fanny Huang
* Project #3 - ArrayDictionary.java
****************************************************************************************/
package datastructures.concrete.dictionaries;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;


/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private KVPair<K, V>[] pairs; // an array of KVPairs
    private int size; 		 	  // number of data in the array
    private int indexSearch;      // marks the index for a key after confirming the key exists
    
    public static final int DEFAULT_CAPACITY = 10;

    /****************************************************************************************
	* Calls main constructor with default values.
	****************************************************************************************/
    public ArrayDictionary() {
        this(DEFAULT_CAPACITY);
    }
    
    /****************************************************************************************
	* Constructs the dictionary
	* @param arraySize - the size of the array
	****************************************************************************************/
    public ArrayDictionary(int arraySize) {
    	if (arraySize <= 0) {
    		throw new IllegalArgumentException();
    	}
    	size = 0;
    	indexSearch = 0;
    	pairs = this.makeArrayOfPairs(arraySize);
    	
    }
    

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private KVPair<K, V>[] makeArrayOfPairs(int arraySize) {
        return (KVPair<K, V>[]) (new KVPair[arraySize]);

    }

    public V get(K key) {
    	if (!this.containsKey(key)) {
    		throw new NoSuchKeyException();
    	}
    	return pairs[indexSearch].getValue();
    }
    
    public void put(K key, V value) {
    	KVPair<K, V> newPair = new KVPair<K, V>(key, value);
    	if (this.containsKey(key)) {
    		pairs[indexSearch] = newPair; 
    	} else {
    		if (size == pairs.length) {
    			increaseCapacity(size + 1);
    		}
    		pairs[size] = newPair;
    		size++;
    	}
    }

    public V remove(K key) {
        if (!this.containsKey(key)) {
        	throw new NoSuchKeyException();
        }
        int index = pairIndex(key);
        V value = pairs[index].getValue();
        for (int i = index; i < size - 1; i++) {
        	pairs[i] = pairs[i + 1];
        }
        size--;
        return value; 
    }

    public boolean containsKey(K key) {
        return this.pairIndex(key) >= 0;
    }

    public int size() {
        return size;
    }
    
    public Iterator<KVPair<K, V>> iterator() {
    	return new ArrayDictionaryIterator<K, V>(pairs);
    }
    
    /****************************************************************************************
	* Returns the index of the key in the array
	* @param key - the key of the wanted index
	****************************************************************************************/
    private int pairIndex(K key) {
    	for (int i = 0; i < this.size(); i++) {
    		if (pairs[i].getKey() == null) {
    			if (key == null) {
    				indexSearch = i;
    				return i;
    			}
    		} else {
    			if (pairs[i].getKey() == key || pairs[i].getKey().equals(key)) {
    				indexSearch = i;
     				return i;
     			}
    		}
    	}
    	return -1;
    }
    
    /****************************************************************************************
	* Increases the capacity of the array to ensure there will be open spots left.
	* @param capacity - the original capacity of the original array
	****************************************************************************************/
    private void increaseCapacity(int capacity) {
    	if (capacity > pairs.length) {
    		int newCapacity = pairs.length * 2 + 1;
    		if (capacity > newCapacity) {
    			newCapacity = capacity;
    		}
    		pairs = Arrays.copyOf(pairs, newCapacity);
    	}		
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
    	KVPair<K, V>[] pairs;
    	private int position;
    	
    	public ArrayDictionaryIterator(KVPair<K, V>[] pairs) {
    		this.pairs = pairs;
    		this.position = 0;
    	}
    	
    	/****************************************************************************************
    	* Returns true if there is another element in the array
    	****************************************************************************************/
    	public boolean hasNext() {
    		if (position > pairs.length - 1) {
    			return false;
    		} else {
    			return pairs[position] != null;
    		}
    	}
    	
    	/****************************************************************************************
    	* Returns the next available KVPair<K, V> in the array
    	****************************************************************************************/
    	public KVPair<K, V> next() {
    		if (!hasNext()) 
    			throw new NoSuchElementException();
    		KVPair<K, V> pair = pairs[position];
    		position++;
    		return pair;
    	}
    }
}
