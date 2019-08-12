package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;
import org.voidsentinel.hexmap.view.AbstractHexGridChunk;
import org.voidsentinel.hexmap.view.HexMetrics;
import org.voidsentinel.hexmap.view.MeshUtil;
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
public class HexGridChunkFlatSimpleTron extends HexGridChunkFlat25 {

	public HexGridChunkFlatSimpleTron(HexMap map, int xstart, int zstart, int chunkSize,
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
		MeshUtil meshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
				triangulateCellCenter(hexCell, meshUtility);
				colorizeCellCenter(hexCell, meshUtility);
				triangulateCellSide(hexCell, meshUtility);
				colorizeCellSide(hexCell, meshUtility);
			}
		}

		Mesh mesh = meshUtility.generateMesh();
		Geometry terrain = new Geometry("ground", mesh);
		terrain.setMaterial(mat);
		representation.attachChild(terrain);
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
		Vector3f v0 = null;
		Vector3f v1 = null;
		Vector3f v2 = null;
		Vector3f v3 = null;
		Vector3f v4 = null;
		int index = MeshUtility.getVerticeCount();
		int offsetDir = 0;
		int offsetDirNext = 0;

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		// points.put(center, cell);

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();

			v1 = center.add(HexMetrics.getFirstCornerVector(offsetDir));
			v0 = center.add(HexMetrics.getSecondCornerVector(offsetDir));
			v2 = center.add(HexMetrics.getFirstCornerVector(offsetDir, 1f));

			MeshUtility.addVertice(v1);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v2);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);

			Vector3f bridge = HexMetrics.getBridgeVector(offsetDir);
			v3 = v1.add(bridge);
			v4 = v0.add(bridge);
			MeshUtility.addVertice(v3);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v4);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);

		   points.put(v1, cell);
		   points.put(v4, cell);

		}

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			offsetDirNext = direction.next().ordinal();
			MeshUtility.addTriangle(index, index + offsetDirNext * 4 + 1, index + offsetDir * 4 + 1);
			MeshUtility.addTriangle(index + offsetDir * 4 + 1, index + offsetDir * 4 + 3, index + offsetDir * 4 + 2);
			MeshUtility.addTriangle(index + offsetDirNext * 4 + 1, index + offsetDirNext * 4 + 2,
					index + offsetDir * 4 + 4);

			MeshUtility.addTriangle(index + offsetDir * 4 + 1, index + offsetDirNext * 4 + 1, index + offsetDir * 4 + 4);
			MeshUtility.addTriangle(index + offsetDir * 4 + 1, index + offsetDir * 4 + 4, index + offsetDir * 4 + 3);
		}

	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	protected void colorizeCellCenter(HexCell cell, MeshUtil MeshUtility) {
		ColorRGBA color = colorExtractor.getColor(cell, map);
		ColorRGBA color2 = color.mult(0.8f);
		MeshUtility.addColor(ColorRGBA.Black);
		for (@SuppressWarnings("unused")
		Direction direction : Direction.values()) {
			HexCell neighbor = cell.getNeighbor(direction);
			HexCell neighborp = cell.getNeighbor(direction.previous());

			// internal point
			MeshUtility.addColor(ColorRGBA.Black);

			// corner
			if (neighbor == null || neighborp == null) {
				MeshUtility.addColor(color);
			} else if (neighbor.getElevation() != cell.getElevation() || neighborp.getElevation() != cell.getElevation()) {
				MeshUtility.addColor(color);
			} else {
				MeshUtility.addColor(ColorRGBA.Black);
			}

			// bridge
			if (neighbor == null || neighbor.getElevation() != cell.getElevation()) {
				MeshUtility.addColor(color);
				MeshUtility.addColor(color);
			} else {
				MeshUtility.addColor(ColorRGBA.Black);
				MeshUtility.addColor(ColorRGBA.Black);
			}
		}
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

			if (height > 0) {
				Vector3f center = HexMetrics.getCellCenter(cell);
				Vector3f v1 = center.add(HexMetrics.corners[direction.ordinal()]);
				Vector3f v2 = center.add(HexMetrics.corners[direction.next().ordinal()]);
				Vector3f v3 = v1.clone();
				Vector3f v4 = v2.clone();
				v1.y = cell.getElevation() * HexMetrics.CELL_ELEVATION;
				v2.y = v1.y;
				v3.y = (cell.getElevation() - height) * HexMetrics.CELL_ELEVATION;
				v4.y = v3.y;
				meshUtility.addQuad(v1, v2, v3, v4);
				points.put(v4, cell);
			}
		}
	}

	protected void colorizeCellSide(HexCell cell, MeshUtil meshUtility) {
		ColorRGBA c1 = colorExtractor.getColor(cell, map).clone().mult(0.75f);
		for (Direction direction : Direction.values()) {
			colorizeCellSideDirection(cell, direction, meshUtility, c1);
		}
	}

	protected void colorizeCellSideDirection(HexCell cell, Direction direction, MeshUtil meshUtility, ColorRGBA c1) {
		HexCell neighbor = cell.getNeighbor(direction);
		int height = 0;
		if (neighbor != null && neighbor.getElevation() < cell.getElevation()) {
			height = cell.getElevation() - neighbor.getElevation();
		} else if (neighbor == null) {
			height = cell.getElevation() + 1;
		}

		if (height > 0) {
			meshUtility.addQuadColors(c1, c1, ColorRGBA.Black, ColorRGBA.Black);
		}
	}

}
