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
		super(map, xstart, zstart, chunkSize, colorExtractor);
	}

	/**
	 * generate and store the geometry for a given map
	 * 
	 * @param map
	 * @return the generated geometry.
	 */
	public void generateGeometry() {
		Material mat = TerrainRepository.getTerrainMaterial();
		MeshUtil MeshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
				triangulateCellCenter(hexCell, MeshUtility);
				triangulateCellBridge(hexCell, MeshUtility);
				triangulateCellCorner(hexCell, MeshUtility);
				triangulateCellSide(hexCell, MeshUtility);
			}
		}

		Mesh mesh = MeshUtility.generateMesh();
		Geometry terrain = new Geometry("ground", mesh);
		terrain.setMaterial(mat);
		representation.attachChild(terrain);
	}

	public void regenerateColor(AbstractCellColorExtractor colorExtractor) {
		this.colorExtractor = colorExtractor;
		MeshUtil meshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
//				colorizeCellCenter(hexCell, meshUtility);
//				colorizeCellBridge(hexCell, meshUtility);
//				colorizeCellCorner(hexCell, meshUtility);
			}
		}
		((Geometry)(representation.getChild("ground"))).getMesh().setBuffer(Type.Color, 4, meshUtility.getColorArray());
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
		ColorRGBA color = colorExtractor.getColor(cell, map);

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		MeshUtility.addColor(color);

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			v2 = center.add(HexMetrics.getFirstCornerVector(offsetDir));
			v3 = center.add(HexMetrics.getSecondCornerVector(offsetDir));
			Triangle.computeTriangleNormal(center, v3, v2, normal);
			MeshUtility.addVertice(v2);
			MeshUtility.addVertice(v3);
			MeshUtility.addNormal(normal);
			MeshUtility.addNormal(normal);
			MeshUtility.addColor(color);
			MeshUtility.addColor(color);
			MeshUtility.addTriangle(index, index + offsetDir * 2 + 2, index + offsetDir * 2 + 1);
		}
		// add the center to the list of points to be used for selection
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
		HexCell neighbor = cell.getNeighbor(direction);
		Vector3f center = HexMetrics.getCellCenter(cell);
		Vector3f bridge = HexMetrics.getBridgeVector(direction.ordinal());
		Vector3f v1 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
		Vector3f v2 = center.add(HexMetrics.getSecondCornerVector(direction.ordinal()));
		Vector3f v3 = v1.add(bridge);
		Vector3f v4 = v2.add(bridge);

		ColorRGBA c1 = colorExtractor.getColor(cell, map);
		ColorRGBA c2 = c1.clone();
		if ((neighbor != null && neighbor.getElevation() != cell.getElevation()) || neighbor == null) {
			c2.multLocal(0.85f);
		}
		MeshUtility.addQuad(v1, v2, v3, v4);
		MeshUtility.addQuadColors(c1, c1, c2, c2);

		// add v4 to the list of point for selection, with the highest cell as
		// destination

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
		HexCell h1 = cell.getNeighbor(direction);
		HexCell h2 = cell.getNeighbor(direction.previous());
		Vector3f normal = new Vector3f();

		Vector3f v1 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
		Vector3f v2 = center.add(HexMetrics.corners[direction.ordinal()]);

		Vector3f o1 = HexMetrics.getBridgeVector(direction.ordinal());
		Vector3f o2 = HexMetrics.getBridgeVector(direction.previous().ordinal());
		Vector3f v3 = v1.add(o1);
		Vector3f v4 = v1.add(o2);

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

		Triangle.computeTriangleNormal(v1, v2, v4, normal);
		meshUtility.addVertice(v1);
		meshUtility.addVertice(v2);
		meshUtility.addVertice(v3);
		meshUtility.addVertice(v4);
		meshUtility.addNormal(normal);
		meshUtility.addNormal(normal);
		meshUtility.addNormal(normal);
		meshUtility.addNormal(normal);
		meshUtility.addColor(c0);
		meshUtility.addColor(c1);
		meshUtility.addColor(c2);
		meshUtility.addColor(c3);
		meshUtility.addTriangle(index, index + 1, index + 3);
		meshUtility.addTriangle(index, index + 2, index + 1);

		// add one of the points to the list of point for selection, with the highest
		// cell as
		// destination

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
			ColorRGBA c1 = colorExtractor.getColor(cell, map).clone().mult(0.90f);
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
				meshUtility.addQuadColors(c1, c1, c1, c1);
			}
		}
	}

}
