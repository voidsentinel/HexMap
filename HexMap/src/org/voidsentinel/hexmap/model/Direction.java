/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.model;

/**
 * @author
 *
 */
public enum Direction {
	NE, EAST, SE, SW, WEST, NW;

	public static final int NBDIRECTIONS;
	static {
		NBDIRECTIONS = Direction.values().length;
	}

	/**
	 * return the next direction in the compass (clockwise order)
	 * 
	 * @return the next direction
	 */
	public Direction next() {
		switch (this) {
		case NE:
			return EAST;
		case EAST:
			return SE;
		case SE:
			return SW;
		case SW:
			return WEST;
		case WEST:
			return NW;
		case NW:
			return NE;
		default:
			return NE;
		}
	}

	/**
	 * return the previous direction in the compass (counter-clockwise)
	 * 
	 * @return the previous direction
	 */
	public Direction previous() {
		switch (this) {
		case NE:
			return NW;
		case EAST:
			return NE;
		case SE:
			return EAST;
		case SW:
			return SE;
		case WEST:
			return SW;
		case NW:
			return WEST;
		default:
			return NE;
		}
	}

	/**
	 * return the inverse of the current direction. works only if even number of
	 * directions...
	 *
	 * @return the inverse direction
	 */
	public Direction oppposite() {
		switch (this) {
		case NE:
			return SW;
		case EAST:
			return WEST;
		case SE:
			return NW;
		case SW:
			return NE;
		case WEST:
			return EAST;
		case NW:
			return SE;
		default:
			return NE;
		}
	}

	/**
	 * indicate the number of turn to perform to go from the current direction to
	 * the other. if the number id negative, this is counterclockwise, otherwise
	 * this is clockwise
	 *
	 * @param target final sector
	 * @return the number of turn to make.
	 */
	public int nbTurn(final Direction target) {
		int to = target.ordinal();
		int from = this.ordinal();

		int a = to - from;
		int b = to - (from + 6);
		return (Math.abs(a) < Math.abs(b)) ? a : b;
	}
}
