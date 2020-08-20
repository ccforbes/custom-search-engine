/****************************************************************************************
* Chris Forbes
* 10/26/2017
* CSE373
* TA: Fanny Huang
* Project #3 - ChainedHashDictionary.java
****************************************************************************************/
package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int size;
    private float loadFactor;
    
    public static final int DEFAULT_CAPACITY = 11;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // You're encouraged to add extra fields (and helper methods) though!

    /****************************************************************************************
	* Calls main constructor with default capacity (11) and load factor (0.75)
	****************************************************************************************/
    public ChainedHashDictionary() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    /****************************************************************************************
	* Calls main constructor with given array size and default load factor (0.75) 
	* @param arraySize - the size of the array
	****************************************************************************************/
    public ChainedHashDictionary(int arraySize) {
    	this(arraySize, DEFAULT_LOAD_FACTOR);
    }
    
    /****************************************************************************************
	* Calls main constructor with default capacity (11) and given load factor.
	* @param loadFactor - number of elements to array size
	****************************************************************************************/
    public ChainedHashDictionary(float loadFactor) {
    	this(DEFAULT_CAPACITY, loadFactor);
    }
    
    /****************************************************************************************
	* Constructs the tree dictionary.
	* @param arraySize - the size of the array
	* @param loadFactor - number of elements to array size
	****************************************************************************************/
    public ChainedHashDictionary(int arraySize, float loadFactor) {
    	if (arraySize <= 0) {
    		throw new IllegalArgumentException();
    	}
    	size = 0;
    	this.loadFactor = loadFactor;
    	chains = this.makeArrayOfChains(arraySize);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    public V get(K key) {
        if (!containsKey(key)) {
        	throw new NoSuchKeyException("No such key: "+ key);
        }
        return chains[keyHashcodeToIndex(key)].get(key);
    }

    public void put(K key, V value) {
    	if (((float)size / (float)chains.length) >= loadFactor) {
    		increaseCapacity();
   		}
    	int index = keyHashcodeToIndex(key);
    	if (chains[index] == null) {
    		chains[index] = new ArrayDictionary<K, V>();
    	}
    	if (!containsKey(key)) {
    		size++;
    	}
   		chains[index].put(key, value);
   	}
    
    public V remove(K key) {
    	if (!containsKey(key)) {
        	throw new NoSuchKeyException();
        }
    	size--;
    	return chains[keyHashcodeToIndex(key)].remove(key);
    }

    public boolean containsKey(K key) {
    	int index = keyHashcodeToIndex(key);
    	if (chains[index] == null) {
    		return false;
    	} else {
    		return chains[index].containsKey(key);
    	}
    }
    
    public int size() {
        return size;
    }

    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    
    /****************************************************************************************
	* Increases the size of the current array
	****************************************************************************************/
    private void increaseCapacity() {
    	int newCapacity = chains.length * 2;
    	IDictionary<K, V>[] temp = this.makeArrayOfChains(newCapacity);
    	rehashKVPairs(temp);
    	
    }
    
    /****************************************************************************************
	* Retrieves all the pairs in the old array and rehashes them in the new array
	* @param newArray - the new array with the increased size
	****************************************************************************************/
    private void rehashKVPairs(IDictionary<K,V>[] newArray) {
    	for (int i = 0; i < chains.length; i++) {
			if (chains[i] != null) {
				IDictionary<K, V> dict = chains[i];
				for (KVPair<K, V> pair : dict) {
					K key = pair.getKey();
					V value = pair.getValue();
					int index = 0;
					if (key != null) {
						index = Math.abs((key.hashCode() % newArray.length));
					}
					if (newArray[index] == null) {
						newArray[index] = new ArrayDictionary<K, V>();
					}
					newArray[index].put(key, value);
				}
			}
		}
    	chains = newArray;
    }
    
    /****************************************************************************************
	* Returns the index of the key
	* @param key - the key to hashcode and retrieve index
	****************************************************************************************/
    private int keyHashcodeToIndex(K key) {
    	if (key == null) {
    		return 0;
    	} else {
    		return Math.abs(key.hashCode() % chains.length);
    	}
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int position;
        private Iterator<KVPair<K, V>> iterator;
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
        	int size = 0;
        	for (int i = 0; i < chains.length; i++) {
        		if (chains[i] != null) {
        			size++;
        		}
        	}
        	this.chains = chains;
        	position = 0;
        	if (size > 0) {
        		while (chains[position] == null) {
        			position++;
        		}
        		iterator = chains[position].iterator();
        	} else {
        		IDictionary<K, V> placeholder = new ArrayDictionary<K, V>();
        		iterator = placeholder.iterator();
        	}
        }

        /****************************************************************************************
    	* Returns true if there is another element in the array
    	****************************************************************************************/
        public boolean hasNext() {
            if (iterator.hasNext()) {
            	return true;
            } else {
            	for (int i = position + 1; i < chains.length; i++) {
            		if (chains[i] != null) {
            			if (chains[i].iterator().hasNext()) {
            				iterator = chains[i].iterator();
            				position = i;
            				return true;
            			}
            		}
            	}
            	return false;
            }
        }

        /****************************************************************************************
    	* Returns the next available KVPair<K, V> in the array
    	****************************************************************************************/
        public KVPair<K, V> next() {
            if (!hasNext()) {
            	throw new NoSuchElementException();
            } else {
            	return iterator.next();
            }
        }
    }
}
