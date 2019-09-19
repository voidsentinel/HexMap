package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.view.AbstractHexGridChunk;
import org.voidsentinel.hexmap.view.HexMetrics;
import org.voidsentinel.hexmap.view.MeshUtil;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;

/**
 * a variation on the hexChunk where Hex border are slopped and terraced.
 * 
 * @author Void Sentinel
 *
 */
public class HexGridChunkSlopped extends AbstractHexGridChunk {

	public HexGridChunkSlopped(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
	      AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, perturbated, colorExtractor);
	}

	/**
	 * generate and store the geometry for a given map
	 * 
	 * @param mapterrain;
	 * 
	 * @return the generated geometry.
	 */
	protected Spatial generateSpecializedGeometries() {
		MeshUtil MeshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
				triangulateCellCenter(hexCell, MeshUtility);
				colorizeCellCenter(hexCell, MeshUtility);
				triangulateCellBridge(hexCell, MeshUtility);
				colorizeCellBridge(hexCell, MeshUtility);
				triangulateCellCorner(hexCell, MeshUtility);
				colorizeCellCorner(hexCell, MeshUtility);
			}
		}

		Mesh mesh = MeshUtility.generateMesh();
		Geometry terrain = new Geometry("ground", mesh);
		terrain.setMaterial(this.getTerrainMaterial());
		return terrain;
	}

	public void regenerateColor(AbstractCellColorExtractor colorExtractor) {
		this.colorExtractor = colorExtractor;
		MeshUtil meshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
				colorizeCellCenter(hexCell, meshUtility);
				colorizeCellBridge(hexCell, meshUtility);
				colorizeCellCorner(hexCell, meshUtility);
			}
		}
		((Geometry) (representation.getChild("ground"))).getMesh().setBuffer(Type.Color, 4, meshUtility.getColorArray());
	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	private void triangulateCellCenter(HexCell cell, MeshUtil MeshUtility) {
		Vector3f center = HexMetrics.getCellCenter(cell);
		Vector3f v2 = null;
		Vector3f v3 = null;
		Vector2f uv2 = null;
		Vector2f uv3 = null;
		int index = MeshUtility.getVerticeCount();
		Vector3f normal = new Vector3f();
		int offsetDir = 0;

		Vector2f UVCenter = cell.getTerrain().getCenterUV(cell.random);

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		MeshUtility.addUV(UVCenter);

		TerrainData terrain = cell.getTerrain();

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			v2 = center.add(HexMetrics.getFirstCornerVector(offsetDir));
			v3 = center.add(HexMetrics.getSecondCornerVector(offsetDir));
			Triangle.computeTriangleNormal(center, v3, v2, normal);

			uv2 = new Vector2f(HexMetrics.getFirstCornerVector(offsetDir).x, HexMetrics.getFirstCornerVector(offsetDir).z)
			      .mult(terrain.getUVSize());
			uv3 = new Vector2f(HexMetrics.getSecondCornerVector(offsetDir).x,
			      HexMetrics.getSecondCornerVector(offsetDir).z).mult(terrain.getUVSize());

//			perturbate(v2);
//			perturbate(v3);
			MeshUtility.addVertice(v2);
			MeshUtility.addVertice(v3);
			MeshUtility.addNormal(normal);
			MeshUtility.addNormal(normal);
			MeshUtility.addUV(UVCenter.add(uv2));
			MeshUtility.addUV(UVCenter.add(uv3));

			MeshUtility.addTriangle(index, index + offsetDir * 2 + 2, index + offsetDir * 2 + 1);
			// put one of the point to the interest list
			points.put(v2, cell);
		}
	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	private void colorizeCellCenter(HexCell cell, MeshUtil MeshUtility) {
		ColorRGBA color = colorExtractor.getColor(cell, map);
		MeshUtility.addColor(color);
		for (@SuppressWarnings("unused")
		Direction direction : Direction.values()) {
			MeshUtility.addColor(color);
			MeshUtility.addColor(color);
		}
	}

	/**
	 * make half the bridge between 2 cells for each needed direction
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	private void triangulateCellBridge(HexCell cell, MeshUtil MeshUtility) {
		triangulateBridgeDirection(cell, Direction.NE, MeshUtility);
		triangulateBridgeDirection(cell, Direction.EAST, MeshUtility);
		triangulateBridgeDirection(cell, Direction.SE, MeshUtility);
	}

	private void colorizeCellBridge(HexCell cell, MeshUtil MeshUtility) {
		colorizeBridgeDirection(cell, Direction.NE, MeshUtility);
		colorizeBridgeDirection(cell, Direction.EAST, MeshUtility);
		colorizeBridgeDirection(cell, Direction.SE, MeshUtility);
	}

	/**
	 * triangulate the bridge between 2 cells for a given direction
	 * 
	 * @param cell
	 * @param direction
	 * @param MeshUtility
	 */
	private void triangulateBridgeDirection(HexCell cell, Direction direction, MeshUtil MeshUtility) {
		HexCell neighbor = cell.getNeighbor(direction);
		TerrainData terrain = cell.getTerrain();
		Vector2f UVCenter = terrain.getCenterUV(cell.random);
		if (neighbor != null) {
			Vector3f center = HexMetrics.getCellCenter(cell);
			Vector3f bridge = HexMetrics.getBridgeVector(direction.ordinal()).multLocal(2);
			Vector3f v1 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
			Vector3f v2 = center.add(HexMetrics.getSecondCornerVector(direction.ordinal()));
			Vector3f v3 = v1.add(bridge);
			Vector3f v4 = v2.add(bridge);

//			perturbate(v1);
//			perturbate(v2);			
//			perturbate(v3);
//			perturbate(v4);

			v3.y = neighbor.getElevation() * HexMetrics.CELL_ELEVATION;
			v4.y = v3.y;

			Vector2f bridge2d = new Vector2f(bridge.x, bridge.z).mult(1f);
			Vector2f uv1 = new Vector2f(HexMetrics.getFirstCornerVector(direction.ordinal()).x,
			      HexMetrics.getFirstCornerVector(direction.ordinal()).z).mult(terrain.getUVSize()).mult(0.25f);
			Vector2f uv2 = new Vector2f(HexMetrics.getSecondCornerVector(direction.ordinal()).x,
			      HexMetrics.getSecondCornerVector(direction.ordinal()).z).mult(terrain.getUVSize()).mult(0.25f);
			Vector2f uv3 = uv1.add(bridge2d).mult(terrain.getUVSize());
			Vector2f uv4 = uv2.add(bridge2d).mult(terrain.getUVSize());

			MeshUtility.addQuad(v1, v2, v3, v4);
			MeshUtility.addUV(UVCenter.add(uv1));
			MeshUtility.addUV(UVCenter.add(uv2));
			MeshUtility.addUV(UVCenter.add(uv3));
			MeshUtility.addUV(UVCenter.add(uv4));

		}
	}

	private void colorizeBridgeDirection(HexCell cell, Direction direction, MeshUtil MeshUtility) {
		HexCell neighbor = cell.getNeighbor(direction);
		if (neighbor != null) {
			ColorRGBA c1 = colorExtractor.getColor(cell, map);
			ColorRGBA c2 = colorExtractor.getColor(neighbor, map);
			MeshUtility.addQuadColors(c1, c1, c2, c2);
		}
	}

	/**
	 * Triangulate the corners of a given cell
	 * 
	 * @param cell        the source cell
	 * @param meshUtility the mesh to add to
	 */
	private void triangulateCellCorner(HexCell cell, MeshUtil meshUtility) {
		triangulateCornerDirection(cell, Direction.NE, meshUtility);
		triangulateCornerDirection(cell, Direction.EAST, meshUtility);
		// since a corner is shared by 3 hex, and we go up from z and x, others corner
		// either don't exist (border) or
		// will have been created by the previous row (SW & SE) or column (WEST)
	}

	private void colorizeCellCorner(HexCell cell, MeshUtil meshUtility) {
		colorizeCornerDirection(cell, Direction.NE, meshUtility);
		colorizeCornerDirection(cell, Direction.EAST, meshUtility);
	}

	/**
	 * Triangulate a given corner
	 * 
	 * @param cell        the source cell
	 * @param direction   the direction
	 * @param meshUtility the mesh to add point to
	 */
	private void triangulateCornerDirection(HexCell cell, Direction direction, MeshUtil meshUtility) {
		int index = meshUtility.getVerticeCount();
		Vector3f center = HexMetrics.getCellCenter(cell);
		HexCell h1 = cell.getNeighbor(direction);
		HexCell h2 = cell.getNeighbor(direction.previous());

		TerrainData terrain = cell.getTerrain();
		Vector2f UVCenter = terrain.getCenterUV(cell.random);

		if (h1 != null && h2 != null) {
			Vector3f normal = new Vector3f();
			Vector3f o1 = HexMetrics.getBridgeVector(direction.ordinal()).multLocal(2f);
			Vector3f o2 = HexMetrics.getBridgeVector(direction.previous().ordinal()).multLocal(2f);
			Vector3f v0 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
			Vector3f v1 = v0.add(o1);
			Vector3f v2 = v0.add(o2);
			v1.y = h1.getElevation() * HexMetrics.CELL_ELEVATION;
			v2.y = h2.getElevation() * HexMetrics.CELL_ELEVATION;

//			perturbate(v0);
//			perturbate(v1);
//			perturbate(v2);

			Vector2f uv0 = UVCenter.add(new Vector2f(HexMetrics.getFirstCornerVector(direction.ordinal()).x,
			      HexMetrics.getFirstCornerVector(direction.ordinal()).z).mult(terrain.getUVSize()));

			Vector2f uv1 = uv0.add(new Vector2f(o1.x, o1.z).mult(0.5f));
			Vector2f uv2 = uv0.add(new Vector2f(o2.x, o2.z).mult(0.5f));

			Triangle.computeTriangleNormal(v0, v1, v2, normal);
			meshUtility.addVertice(v0);
			meshUtility.addVertice(v1);
			meshUtility.addVertice(v2);
			meshUtility.addNormal(normal);
			meshUtility.addNormal(normal);
			meshUtility.addNormal(normal);
			meshUtility.addTriangle(index, index + 1, index + 2);

			meshUtility.addUV(uv0);
			meshUtility.addUV(uv1);
			meshUtility.addUV(uv2);

		}
	}

	private void colorizeCornerDirection(HexCell cell, Direction direction, MeshUtil meshUtility) {
		HexCell h1 = cell.getNeighbor(direction);
		HexCell h2 = cell.getNeighbor(direction.previous());
		if (h1 != null && h2 != null) {

			ColorRGBA c0 = colorExtractor.getColor(cell, map);
			ColorRGBA c1 = colorExtractor.getColor(h1, map);
			ColorRGBA c2 = colorExtractor.getColor(h2, map);

			meshUtility.addColor(c0);
			meshUtility.addColor(c1);
			meshUtility.addColor(c2);
		}
	}

}
