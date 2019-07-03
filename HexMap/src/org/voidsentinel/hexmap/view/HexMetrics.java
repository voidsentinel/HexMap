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

	// number of terrace per slope
	public static final int				TERRACEPERSLOPE		= 2;
	// number of steps per slope
	public static final int				TERRACESTEPS			= TERRACEPERSLOPE * 2 + 1;
	public static final float			HORIZONTALSTEPSIZE	= 1f / TERRACESTEPS;
	public static final float			VERTICALSTEPSIZE		= 1f / (TERRACEPERSLOPE + 1);

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
		response.y = cell.getElevation() * HexMetrics.CELL_ELEVATION;
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

	/**
	 * 
	 * @param a
	 * @param b
	 * @param step
	 * @return
	 */
	public static Vector3f interpolateTerraceLerp(Vector3f a, Vector3f b, int step) {
		Vector3f response = new Vector3f();
		float h = step * HORIZONTALSTEPSIZE;
		response.x = a.x + (b.x - a.x) * h;
		response.z = a.z + (b.z - a.z) * h;
		float v = ((step + 1) / 2) * VERTICALSTEPSIZE;
		response.y = a.y + (b.y - a.y) * v;
		return response;
	}

	public static ColorRGBA interpolateTerraceColor(ColorRGBA a, ColorRGBA b, int step) {
		ColorRGBA response = new ColorRGBA();
		float h = step * HORIZONTALSTEPSIZE;
		response.interpolateLocal(a, b, h);
		return response;
	}

	public static HexEdgeType getEdgeType(int elevation1, int elevation2) {
		int diff = elevation2 - elevation1;
		if (diff < 0) {
			diff = diff * -1;
		}
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
			height += c1.getElevation() * CELL_ELEVATION;
			count += 1f;
		}
		if (c3 != null) {
			height += c3.getElevation() * CELL_ELEVATION;
			count += 1f;
		}
		if (c2 != null) {
			height += c2.getElevation() * CELL_ELEVATION;
			count += 1f;
		}
		if (count != 0)
			return height / count;
		return 0f;

	}

}
