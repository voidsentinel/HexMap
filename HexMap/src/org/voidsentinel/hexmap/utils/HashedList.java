/**
 * Project HexMap
 */
package org.voidsentinel.hexmap.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class store data both as a list and a hashmap, allowing access both by
 * key (hash) or by index. This allows for a
 * 
 * @author VoidSentinel
 *
 */
public class HashedList<K, V extends Comparable> {

	public ArrayList<V>	list	= new ArrayList<V>();
	public HashMap<K, V>	map	= new HashMap<K, V>();

	/**
	 * access by index to an alement in the list
	 * 
	 * @param index the 0-based index
	 * @return the data, or null if not existing
	 */
	public V get(int index) {
		return list.get(index);
	}

	/**
	 * access by key to an element in the map
	 * 
	 * @param key the key of the data to be accessed
	 * @return the data, or null if not existing
	 */
	public V get(K key) {
		return map.get(key);
	}
	
	/**
	 * indicate if the Hahsed list contains the given key
	 * @param key
	 * @return true if the hashedlist contain an object with this key, false otherwise;
	 */
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	/**
	 * add a couple key / value in the data. the data is added to both the list and
	 * the map. If the data was present in the list, it is removed from its old
	 * position before beeing added at the end.
	 * 
	 * @param k the key
	 * @param v the object
	 * @return the previous object with the same key, or null if no such object
	 *         existed
	 */
	public V add(K k, V v) {
		V old = map.put(k, v);
		if (old != null) {
			list.remove(old);
		}
		list.add(v);
		return old;
	}

	/**
	 * remove an object designated by it's key. the object is first removed from the
	 * map, and if it was present in it, is then removed from the list (removal from
	 * list imply a costly full list scan, thus the check)
	 * 
	 * @param k the key to remove
	 * @return
	 */
	public V remove(K k) {
		V old = map.remove(k);
		if (old != null) {
			list.remove(old);
		}
		return old;
	}

	/**
	 * return the size of the list
	 * 
	 * @return
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Sort the list using an insertion sort (effective if list is nearly sorted)
	 */
	public void insertionSort() {
		int i, j;
		for (i = 1; i < list.size(); i++) {
			V tmp = list.get(i);
			j = i;
			while ((j > 0) && (tmp.compareTo(list.get(j - 1)) < 0)) {
				list.set(j, list.get(j - 1));
				j--;
			}
			list.set(j, tmp);
		}
	}
	
	
	public void shuffle() {
		Collections.shuffle(list);
	}
}
