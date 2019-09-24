package org.voidsentinel.hexmap.view;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;

/**
 * generate and store the representation of a chunk of the map. also store
 * vertices that allow for selection of an hex. The colors and texture of the
 * representation can be modified
 * 
 * @author guipatry
 *
 */
public class HexGridChunkFlat extends AbstractHexGridChunk {

	public HexGridChunkFlat(HexMap map, int xstart, int zstart, int chunkSize,
	      AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, false, colorExtractor);
	}

	/**
	 * generate and store the geometry for a given map
	 * 
	 * @param map
	 * @return the generated geometry.
	 */
	protected Spatial generateSpecializedGeometries() {
		MeshUtil meshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
				triangulateCellCenter(hexCell, meshUtility);
				colorizeCellCenter(hexCell, meshUtility);
				triangulateCellBridge(hexCell, meshUtility);
				colorizeCellBridge(hexCell, meshUtility);
				triangulateCellCorner(hexCell, meshUtility);
				colorizeCellCorner(hexCell, meshUtility);
				triangulateCellSide(hexCell, meshUtility);
				colorizeCellSide(hexCell, meshUtility);
			}
		}

		Mesh mesh = meshUtility.generateMesh();
		Geometry terrain = new Geometry("ground", mesh);
		terrain.setMaterial(this.getTerrainMaterial());
		return terrain;
	}

	/**
	 * Should be called only if representation is non empty. Will extract the colors
	 * for each cell with the new extractor, and fill the color buffer of the mesh
	 * with the new values
	 * 
	 * @param colorExtractor
	 *           the new colorExtractor to use.
	 */
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
				colorizeCellSide(hexCell, meshUtility);
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
		int index = MeshUtility.getVerticeCount();
		Vector3f normal = new Vector3f();
		int offsetDir = 0;

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		points.put(center, cell);

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			v2 = center.add(HexMetrics.getFirstCornerVector(offsetDir));
			v3 = center.add(HexMetrics.getSecondCornerVector(offsetDir));
			Triangle.computeTriangleNormal(center, v3, v2, normal);
			MeshUtility.addVertice(v2);
			MeshUtility.addVertice(v3);
			MeshUtility.addNormal(normal);
			MeshUtility.addNormal(normal);
			MeshUtility.addTriangle(index, index + offsetDir * 2 + 2, index + offsetDir * 2 + 1);
			points.put(v2, cell);
		}
		// add the center to the list of points to be used for selection
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
		for (@SuppressWarnings("unused") Direction direction : Direction.values()) {
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
		for (Direction direction : Direction.values()) {
			triangulateBridgeDirection(cell, direction, MeshUtility);
		}
	}

	/**
	 * triangulate the bridge between 2 cells for a given direction
	 * 
	 * @param cell
	 * @param direction
	 * @param MeshUtility
	 */
	private void triangulateBridgeDirection(HexCell cell, Direction direction, MeshUtil MeshUtility) {
		Vector3f center = HexMetrics.getCellCenter(cell);
		Vector3f bridge = HexMetrics.getBridgeVector(direction.ordinal());
		Vector3f v1 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
		Vector3f v2 = center.add(HexMetrics.getSecondCornerVector(direction.ordinal()));
		Vector3f v3 = v1.add(bridge);
		Vector3f v4 = v2.add(bridge);

		MeshUtility.addQuad(v1, v2, v3, v4);
		points.put(v4, cell);

	}

	private void colorizeCellBridge(HexCell cell, MeshUtil MeshUtility) {
		for (Direction direction : Direction.values()) {
			colorizeBridgeDirection(cell, direction, MeshUtility);
		}
	}

	private void colorizeBridgeDirection(HexCell cell, Direction direction, MeshUtil MeshUtility) {
		HexCell neighbor = cell.getNeighbor(direction);
		ColorRGBA c1 = colorExtractor.getColor(cell, map);
		ColorRGBA c2 = c1.clone();
		if ((neighbor != null && neighbor.getElevation() != cell.getElevation()) || neighbor == null) {
			c2.multLocal(0.85f);
		}
		MeshUtility.addQuadColors(c1, c1, c2, c2);
	}

	/**
	 * Triangulate the corners of a given cell
	 * 
	 * @param cell        the source cell
	 * @param meshUtility the mesh to add to
	 */
	private void triangulateCellCorner(HexCell cell, MeshUtil meshUtility) {
		for (Direction direction : Direction.values()) {
			triangulateCornerDirection(cell, direction, meshUtility);
		}
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
		Vector3f normal = new Vector3f();

		Vector3f v1 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
		Vector3f v2 = center.add(HexMetrics.corners[direction.ordinal()]);

		Vector3f o1 = HexMetrics.getBridgeVector(direction.ordinal());
		Vector3f o2 = HexMetrics.getBridgeVector(direction.previous().ordinal());
		Vector3f v3 = v1.add(o1);
		Vector3f v4 = v1.add(o2);

		Triangle.computeTriangleNormal(v1, v2, v4, normal);
		meshUtility.addVertice(v1);
		meshUtility.addVertice(v2);
		meshUtility.addVertice(v3);
		meshUtility.addVertice(v4);
		meshUtility.addNormal(normal);
		meshUtility.addNormal(normal);
		meshUtility.addNormal(normal);
		meshUtility.addNormal(normal);
		meshUtility.addTriangle(index, index + 1, index + 3);
		meshUtility.addTriangle(index, index + 2, index + 1);

		// add one of the points to the list of point for selection, with the highest
		// cell as  destination
	}

	private void colorizeCellCorner(HexCell cell, MeshUtil meshUtility) {
		for (Direction direction : Direction.values()) {
			colorizeCornerDirection(cell, direction, meshUtility);
		}
	}

	private void colorizeCornerDirection(HexCell cell, Direction direction, MeshUtil meshUtility) {
		HexCell h1 = cell.getNeighbor(direction);
		HexCell h2 = cell.getNeighbor(direction.previous());
		ColorRGBA c0 = colorExtractor.getColor(cell, map);
		ColorRGBA c1 = c0;
		ColorRGBA c2 = c0;
		ColorRGBA c3 = c0;
		if ((h1 != null && h1.getElevation() != cell.getElevation())) { // || h1 == null
			c2 = c0.clone().multLocal(0.85f);
		}

		if ((h2 != null && h2.getElevation() != cell.getElevation())) { // || h2 != null
			c3 = c0.clone().multLocal(0.85f);
		}
		if ((h1 != null && h1.getElevation() != cell.getElevation())
		      || (h2 != null && h2.getElevation() != cell.getElevation())) { //
			c1 = c0.clone().multLocal(0.85f);
		}

		if (h1 == null || h2 == null) {
			c1 = c0.clone().multLocal(0.85f);
			c2 = c1;
			c3 = c1;
		}
		meshUtility.addColor(c0);
		meshUtility.addColor(c1);
		meshUtility.addColor(c2);
		meshUtility.addColor(c3);
	}

	private void triangulateCellSide(HexCell cell, MeshUtil meshUtility) {
		for (Direction direction : Direction.values()) {
			HexCell neighbor = cell.getNeighbor(direction);
			int height = 0;
			if (neighbor != null && neighbor.getElevation() < cell.getElevation()) {
				height = cell.getElevation() - neighbor.getElevation();
			} else if (neighbor == null) {
				height = cell.getElevation() + 1;
			}

			Vector3f center = HexMetrics.getCellCenter(cell);
			for (int h = 0; h < height; h++) {
				Vector3f v1 = center.add(HexMetrics.corners[direction.ordinal()]);
				Vector3f v2 = center.add(HexMetrics.corners[direction.next().ordinal()]);
				Vector3f v3 = v1.clone();
				Vector3f v4 = v2.clone();
				v1.y = (cell.getElevation() - h) * HexMetrics.CELL_ELEVATION;
				v2.y = v1.y;
				v3.y = (cell.getElevation() - h - 1) * HexMetrics.CELL_ELEVATION;
				v4.y = v3.y;
				meshUtility.addQuad(v1, v2, v3, v4);
				points.put(v4, cell);
			}
		}
	}

	private void colorizeCellSide(HexCell cell, MeshUtil meshUtility) {
		for (Direction direction : Direction.values()) {
			colorizeCellSideDirection(cell, direction, meshUtility);
		}
	}

	private void colorizeCellSideDirection(HexCell cell, Direction direction, MeshUtil meshUtility) {
		ColorRGBA c1 = colorExtractor.getColor(cell, map).clone().mult(0.90f);
		HexCell neighbor = cell.getNeighbor(direction);
		int height = 0;
		if (neighbor != null && neighbor.getElevation() < cell.getElevation()) {
			height = cell.getElevation() - neighbor.getElevation();
		} else if (neighbor == null) {
			height = cell.getElevation() + 1;
		}
		for (int h = 0; h < height; h++) {
			meshUtility.addQuadColors(c1, c1, c1, c1);
		}
	}

}
