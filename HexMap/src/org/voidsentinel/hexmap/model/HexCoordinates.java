package org.voidsentinel.hexmap.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Hex Coordinate class. Coordinate are stored both into offset (odd-r) and cube
 * coordinates. Both kind are accessible as public mmeber value of the objetc
 * 
 * @author guipatry
 *
 */
public class HexCoordinates {
	public final int		column;
	public final int		row;

	public final int		X;
	public final int		Z;
	public final int		Y;

	private static int[]	deltaX	= { 1, 1, 0, -1, -1, 0 };
	private static int[]	deltaZ	= { -1, 0, 1, 1, 0, -1 };

	/**
	 * Constructor from Offset Coordinate
	 * 
	 * @param column
	 * @param row
	 */
	public HexCoordinates(int column, int row) {
		this.column = column;
		this.row = row;

		X = column - (int) ((row - row % 2) / 2);
		Z = row;
		Y = -X - Z;
	}

	/**
	 * Axial constructor
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public HexCoordinates(int x, int y, int z) {
		X = x;
		Y = y;
		Z = z;

		column = X + (int) ((Z - (Z % 2)) / 2);
		row = Z;

	}

	public String toString() {
		return "(" + X + "," + Y + ", " + Z + ")";
	}

	/**
	 * return a coordinate that is an interpolation between two coordinate
	 *
	 * @param a the first coordinate
	 * @param b the second coordinate
	 * @param t the % of
	 * @return
	 */
	public static HexCoordinates interpolate(final HexCoordinates a, final HexCoordinates b, final float t) {
		int x = (int) (a.X + (float) ((b.X - a.X)) * t);
		int z = (int) (a.Z + (float) ((b.Z - a.Z)) * t);
		int y = -x - z;
		HexCoordinates response = new HexCoordinates(x, y, z);
		return response;
	}

	/**
	 * return a coordinate that is an interpolation between two coordinate
	 *
	 * @param a the first coordinate
	 * @param b the second coordinate
	 * @param t the % of
	 * @return
	 */
	public HexCoordinates interpolate(final HexCoordinates b, final float t) {
		int x = (int) (X + (float) ((b.X - X)) * t);
		int z = (int) (Z + (float) ((b.Z - Z)) * t);
		int y = -x - z;
		HexCoordinates response = new HexCoordinates(x, y, z);
		return response;
	}

	/**
	 * return the coordinate of the Hex in the given direction
	 * 
	 * @param dir the direction to calculate new coordinatre for
	 * @return the Cooridnate of the hex in this direction
	 */
	public HexCoordinates direction(Direction dir) {
		return new HexCoordinates(column + deltaX[dir.ordinal()], row + deltaZ[dir.ordinal()]);
	}

	/**
	 * return the distance between this coodinate and the one given
	 * 
	 * @param c2 the coodinate to search distance to
	 * @return the distance between the 2 hex coordinate
	 */
	public int distance(HexCoordinates c2) {
		return (Math.abs(X - c2.X) + Math.abs(Y - c2.Y) + Math.abs(Z - c2.Z)) / 2;
	}

	/**
	 * return the list of coordinate that for a line between a given point and this
	 * one.
	 * 
	 * @param c2 the destination point
	 * @return a list of coordinates
	 */
	public List<HexCoordinates> lineTo(HexCoordinates c2) {
		List<HexCoordinates> response = new ArrayList<HexCoordinates>();
		int distance = this.distance(c2);
		for (int i = 0; i <= distance; i++) {
			float percent = 1f * i * (1f / (float) distance);
			response.add(this.interpolate(c2, percent));
		}
		return response;
	}

	/**
	 * return a list of all coordinate at the given distance of this HexCoordinates
	 * 
	 * @param distance
	 * @return
	 */
	public List<HexCoordinates> inRange(int distance) {
		List<HexCoordinates> response = new ArrayList<HexCoordinates>();
		for (int x = -distance; x <= distance; x++) {
			for (int y = -distance; y <= distance; y++) {
				for (int z = -distance; z <= distance; z++) {
					if (x + y + z == 0) {
						response.add(new HexCoordinates(X + x, Y + y, Z + z));
					}
				}
			}
		}

		return response;

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
		result = prime * result + X;
		result = prime * result + Y;
		result = prime * result + Z;
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
		HexCoordinates other = (HexCoordinates) obj;
		if (column != other.column) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}

}
