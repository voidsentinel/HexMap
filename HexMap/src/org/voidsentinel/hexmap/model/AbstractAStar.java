/**
 * 
 */
package org.voidsentinel.hexmap.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.utils.HashedList;

/**
 * @author guipatry
 *
 */
public abstract class AbstractAStar<E> {

	/**
	 * A Node from the movement graph Contains the parent node, as well as the
	 * corresponding source object, and the cost values (base, heuristic and total)
	 *
	 */
	private class Node implements Comparable<Node> {
		public float	g			= 0f;
		public float	h			= 0f;
		public float	f			= 0f;
		public E			source	= null;
		public Node		parent	= null;

		public Node(E source) {
			this.source = source;
		}

		@Override
		public int compareTo(AbstractAStar<E>.Node o) {
			if (f < o.f) {
				return -1;
			}
			if (f == o.f) {
				return 0;
			}
			return 1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Node other = (Node) obj;
			if (source == null) {
				if (other.source != null) {
					return false;
				}
			} else if (!source.equals(other.source)) {
				return false;
			}
			return true;
		}

	}

	public List<E> aStarSearch(E start, E goal) {

		HashedList<E, Node> open = new HashedList<E, Node>();
		Map<E, Node> closed = new HashMap<E, Node>();
		Node eNode;

		List<E> response = new ArrayList<E>();

		open.add(start, new Node(start));

		while (open.size() != 0) {
			// lire le plus bas cout (et le retirer)
			Node current = open.get(0);
			open.list.remove(0);
			open.map.remove(current.source);

			if (current.source.equals(goal)) {
				response.add(goal);
				while (current != null) {
					response.add(current.source);
					current = current.parent;
				}
				Collections.reverse(response);
				return response;

			} else {
				List<E> voisins = this.getNeihbours(current.source);
				for (E e : voisins) {
					if (e != null) {
						if (closed.containsKey(e)) {
							// do nothing
						} else {
							float cost = this.getCost(current.source, e);
							float heuristic = getDistance(e, goal); // distance between
							float total = cost + heuristic;
							eNode = open.get(e);
							if (eNode == null || eNode.f > total) {
								if (eNode == null) {
									eNode = new Node(e);
									eNode.g = cost;
									eNode.h = heuristic;
									eNode.f = total;
									eNode.parent = current;
									open.add(e, eNode);
								} else {
									// eNode is already in open, just change the values
									eNode.g = cost;
									eNode.h = heuristic;
									eNode.f = total;
									eNode.parent = current;
								}
							}
						}
					}
				}
				closed.put(current.source, current);
				open.insertionSort();
			}

		}
		return new ArrayList<E>();
	}

	public abstract List<E> getNeihbours(E source);

	public abstract float getCost(E source, E dest);

	public abstract float getDistance(E source, E dest);
}
