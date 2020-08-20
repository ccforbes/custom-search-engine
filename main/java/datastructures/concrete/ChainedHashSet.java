/****************************************************************************************
* Chris Forbes
* 10/26/2017
* CSE373
* TA: Fanny Huang
* Project #3 - ChainedHashSet.java
****************************************************************************************/
package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See ISet for more details on what each method is supposed to do.
 */
public class ChainedHashSet<T> implements ISet<T> {
    // This should be the only field you need
    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        this.map = new ChainedHashDictionary<>();
    }

    public void add(T item) {
        map.put(item, true);
    }

    public void remove(T item) {
    	if (!contains(item)) {
    		throw new NoSuchElementException();
    	}
        map.remove(item);
    }

    public boolean contains(T item) {
        return map.containsKey(item);
    }

    public int size() {
        return map.size();
    }

    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }
    
    public String toString() {
    	String result = "";
    	for (KVPair<T, Boolean> pair : map) {
    		T key = pair.getKey();
    		result += key + ", ";
    	}
    	return result;
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        
        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            this.iter = iter;
        }

        /****************************************************************************************
    	* Returns true if there is another element in the array
    	****************************************************************************************/
        public boolean hasNext() {
            return iter.hasNext();
        }

        /****************************************************************************************
    	* Returns the next available KVPair<K, V> in the array
    	****************************************************************************************/
        public T next() {
            return iter.next().getKey();
        }
    }
}
