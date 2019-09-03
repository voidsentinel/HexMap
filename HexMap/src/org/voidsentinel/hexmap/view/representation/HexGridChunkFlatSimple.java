package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.view.AbstractHexGridChunk;
import org.voidsentinel.hexmap.view.HexMetrics;
import org.voidsentinel.hexmap.view.MeshUtil;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.math.ColorRGBA;
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
public class HexGridChunkFlatSimple extends AbstractHexGridChunk {

	private float[] coeff = new float[] { 0.35f, 0.25f, 0.35f, 0.75f, 1.15f, 0.75f };

	public HexGridChunkFlatSimple(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
	      AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, perturbated, colorExtractor);

	}

	/**
	 * generate and store the geometry for a given map
	 * 
	 * @param map
	 * @return the generated geometry.
	 */
	public void generateGeometry() {
		LOG.info("generation mesh with variation");
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
		terrain.setMaterial(terrainMaterial);
		representation.attachChild(terrain);
	}

	/**
	 * Should be called only if representation is non empty. Will extract the colors
	 * for each cell with the new extractor, and fill the color buffer of the mesh
	 * with the new values
	 * 
	 * @param colorExtractor the new colorExtractor to use.
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
		Vector3f v2 = null;
		Vector3f v3 = null;
		Vector3f point = null;
		int index = MeshUtility.getVerticeCount();
		int offsetDir = 0;
		int offsetDirNext = 0;

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		// points.put(center, cell);

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			v2 = center.add(HexMetrics.getFirstCornerVector(offsetDir, 1f));

			if (perturbated) {
				perturbate(v2);
			}

			MeshUtility.addVertice(v2);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			points.put(v2, cell);

		}

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			offsetDirNext = direction.next().ordinal();
			MeshUtility.addTriangle(index, index + offsetDirNext + 1, index + offsetDir + 1);
		}

	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	protected void colorizeCellCenter(HexCell cell, MeshUtil MeshUtility) {
		ColorRGBA color = colorExtractor.getColor(cell, map).clone().mult(1.1f);
		MeshUtility.addColor(color);
		for (@SuppressWarnings("unused")
		Direction direction : Direction.values()) {
			MeshUtility.addColor(color);
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
				if (perturbated) {
					perturbate(v1);
					perturbate(v2);
				}

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
		for (Direction direction : Direction.values()) {
			ColorRGBA c1 = colorExtractor.getColor(cell, map).clone().mult(coeff[direction.ordinal()]);
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
			meshUtility.addQuadColors(c1, c1, c1, c1);
		}
	}

}
