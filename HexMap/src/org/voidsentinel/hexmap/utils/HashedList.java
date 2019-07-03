/**
 * 
 */
package org.voidsentinel.hexmap.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author guipatry
 *
 */
public class HashedList<K, V extends Comparable<V>> {

	public ArrayList<V>	list	= new ArrayList<V>();
	public HashMap<K, V>	map	= new HashMap<K, V>();

	public V get(int index) {
		return list.get(index);
	}

	public V get(K key) {
		return map.get(key);
	}

	/**
	 * add a couple key / value in the data
	 * 
	 * @param k the key
	 * @param v the object
	 * @return the previous object with the same key, or null if no such obejct
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

	public int size() {
		return list.size();
	}

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
}
