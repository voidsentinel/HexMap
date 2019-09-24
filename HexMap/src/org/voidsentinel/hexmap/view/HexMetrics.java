/**
 * 
 */
package org.voidsentinel.hexmap.view;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * Constant definitions and generic methods
 * 
 * @author guipatry
 *
 */
public class HexMetrics {
	// Outer radius of an Hex
	public static final float			OUTERRADIUS				= 0.5f;
	// Inner radius of an Hex
	public static final float			INNERRADIUS				= OUTERRADIUS * 0.866025404f;

	// flat part of an Hex
	public static final float			SOLIDFACTOR				= 0.75f;
	// slope part of an Hex
	public static final float			BLURFACTOR				= 1f - SOLIDFACTOR;

	// One level of elevation (negative since we go down for each elevation)
	public static final float			CELL_ELEVATION			= -0.25f;
	// negative unit Y
	public static final Vector3f		CELL_UNIT_NORMAL		= Vector3f.UNIT_Y.mult(-1f);


	// position of the six corners. We put a corner at the Top
	public static final Vector3f[]	corners					= { new Vector3f(0f, 0f, OUTERRADIUS),
	      new Vector3f(INNERRADIUS, 0f, 0.5f * OUTERRADIUS), new Vector3f(INNERRADIUS, 0f, -0.5f * OUTERRADIUS),
	      new Vector3f(0f, 0f, -OUTERRADIUS), new Vector3f(-INNERRADIUS, 0f, -0.5f * OUTERRADIUS),
	      new Vector3f(-INNERRADIUS, 0f, 0.5f * OUTERRADIUS), new Vector3f(0f, 0f, OUTERRADIUS) };

	/**
	 * return the wporld position of a given cell corrdinate
	 * 
	 * @param coordinates the coordinate to transform
	 * @return the world (3D) position. Y is null
	 */
	public static Vector3f getCellCenter(HexCoordinates coordinates) {
		return new Vector3f(
		      coordinates.row % 2 == 0 ? coordinates.column * HexMetrics.INNERRADIUS * 2
		            : coordinates.column * HexMetrics.INNERRADIUS * 2 + HexMetrics.INNERRADIUS,
		      0f, coordinates.row * HexMetrics.OUTERRADIUS * 1.5f);
	}

	/**
	 * return the wporld position of a given cell's center
	 * 
	 * @param cell cell to transform
	 * @return the world (3D) position of the cell center
	 */
	public static Vector3f getCellCenter(HexCell cell) {
		Vector3f response = getCellCenter(cell.hexCoordinates);
		response.y = cell.getIntData(HexCell.ELEVATION_DATA) * HexMetrics.CELL_ELEVATION;
		return response;
	}

	/**
	 * 
	 * @param direction
	 * @return
	 */
	public static Vector3f getFirstCornerVector(int direction) {
		return corners[direction].mult(SOLIDFACTOR);
	}

	public static Vector3f getFirstCornerVector(int direction, float coeff) {
		return corners[direction].mult(coeff);
	}

	public static Vector3f getSecondCornerVector(int direction) {
		return corners[direction + 1].mult(SOLIDFACTOR);
	}

	public static Vector3f getSecondCornerVector(int direction, float coeff) {
		return corners[direction + 1].mult(coeff);
	}

	public static Vector3f getBridgeVector(int direction) {
		return corners[direction].add(corners[direction + 1]).mult(0.5f).mult(BLURFACTOR);
	}


	public static HexEdgeType getEdgeType(int elevation1, int elevation2) {
		int diff = Math.abs(elevation2 - elevation1);
		switch (diff) {
		case 0:
			return HexEdgeType.Flat;
		case 1:
			return HexEdgeType.Terrace;
		default:
			return HexEdgeType.Cliff;
		}

	}

	public static float getMeanHeight(HexCell c1, HexCell c2, HexCell c3) {
		float height = 0.0f;
		float count = 0.0f;
		if (c1 != null) {
			height += c1.getIntData(HexCell.ELEVATION_DATA) * CELL_ELEVATION;
			count += 1f;
		}
		if (c3 != null) {
			height += c3.getIntData(HexCell.ELEVATION_DATA) * CELL_ELEVATION;
			count += 1f;
		}
		if (c2 != null) {
			height += c2.getIntData(HexCell.ELEVATION_DATA) * CELL_ELEVATION;
			count += 1f;
		}
		if (count != 0)
			return height / count;
		return 0f;

	}

}
