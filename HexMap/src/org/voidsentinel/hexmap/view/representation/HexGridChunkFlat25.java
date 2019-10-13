package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.Perturbator;
import org.voidsentinel.hexmap.view.AbstractHexGridChunk;
import org.voidsentinel.hexmap.view.HexMetrics;
import org.voidsentinel.hexmap.view.MeshUtil;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

/**
 * generate and store the representation of a chunk of the map. also store
 * vertices that allow for selection of an hex. The colors and texture of the
 * representation can be modified
 * 
 * @author guipatry
 *
 */
public class HexGridChunkFlat25 extends AbstractHexGridChunk {

	/**
	 * Constructor
	 * 
	 * @param map                  the full map to represent
	 * @param xstart               Xstart of th zone to represent
	 * @param zstart               Z start of the zone to represent
	 * @param chunkSize            the size of the zone to represent
	 * @param perturbationPossible to be removed
	 * @param perturbated          current status of the perturbation
	 * @param colorExtractor       the initial color extractor to use
	 */
	public HexGridChunkFlat25(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
	      AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, perturbated, colorExtractor);
	}

	public boolean canBePerturbated() {
		return true;
	}

	protected void generateSpecializedGeometries(Node localRoot) {
		Mesh mesh = new Mesh();
		Geometry terrain = new Geometry("ground", mesh);
		terrain.setMaterial(this.getTerrainMaterial());
		localRoot.attachChild(terrain);

		generateStructure();
		generateColor(colorExtractor);
	}

	/**
	 * Will (re) generate the colors of the map representation with the given
	 * colorExtractor. Should be called only if representation is non empty.
	 * 
	 * @param colorExtractor the new colorExtractor to use.
	 */
	public void generateColor(AbstractCellColorExtractor colorExtractor) {
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
		Mesh mesh = ((Geometry) (representation.getChild("ground"))).getMesh();
		meshUtility.generateMesh(mesh);
	}

	/**
	 * will (re) generate the mesh structure (vertices, normals, triangles) of the
	 * map representation. Should be called only if representation is non empty.
	 */
	public void generateStructure() {
		MeshUtil meshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
				triangulateCellCenter(hexCell, meshUtility);
				triangulateCellSide(hexCell, meshUtility);
			}
		}
		Mesh mesh = ((Geometry) (representation.getChild("ground"))).getMesh();
		meshUtility.generateMesh(mesh);
	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param meshUtility
	 */
	private void triangulateCellCenter(HexCell cell, MeshUtil meshUtility) {
		Vector3f center = HexMetrics.getCellCenter(cell);
		Vector3f v0 = null;
		Vector3f v2 = null;
		Vector3f v3 = null;
		Vector3f v4 = null;
		Vector3f v5 = null;
		int index = meshUtility.getVerticeCount();
		int offsetDir = 0;
		int offsetDirNext = 0;

		meshUtility.addVertice(center);
		meshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		points.put(center, cell);

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();

			v0 = center.add(HexMetrics.getFirstCornerVector(offsetDir));
			v2 = center.add(HexMetrics.getFirstCornerVector(offsetDir, 1f));
			v3 = center.add(HexMetrics.getSecondCornerVector(offsetDir, 1f));

			if (this.perturbated) {
				// we perturbate both v2 and v0, since the internal point should move the same vector
				// so that the border representation does not change too much
				Vector3f perturbation = Perturbator.getPerturbation(v2);
				v0.addLocal(perturbation);
				v2.addLocal(perturbation);
				Perturbator.perturbate(v3);
			}
			
			meshUtility.addVertice(v0);
			meshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			meshUtility.addVertice(v2);
			meshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);

			float amount = 0.5f * HexMetrics.BLURFACTOR;
			v4 = new Vector3f().interpolateLocal(v2, v3, amount);
			v5 = new Vector3f().interpolateLocal(v2, v3, 1f-amount);
			
			meshUtility.addVertice(v4);
			meshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			meshUtility.addVertice(v5);
			meshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);

			points.put(v0, cell);
			points.put(v2, cell);
			points.put(v5, cell);

		}

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			offsetDirNext = direction.next().ordinal();
			meshUtility.addTriangle(index, index + offsetDirNext * 4 + 1, index + offsetDir * 4 + 1);
			meshUtility.addTriangle(index + offsetDir * 4 + 1, index + offsetDir * 4 + 3, index + offsetDir * 4 + 2);
			meshUtility.addTriangle(index + offsetDirNext * 4 + 1, index + offsetDirNext * 4 + 2,
			      index + offsetDir * 4 + 4);

			meshUtility.addTriangle(index + offsetDir * 4 + 1, index + offsetDirNext * 4 + 1, index + offsetDir * 4 + 4);
			meshUtility.addTriangle(index + offsetDir * 4 + 1, index + offsetDir * 4 + 4, index + offsetDir * 4 + 3);
		}
	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param meshUtility
	 */
	protected void colorizeCellCenter(HexCell cell, MeshUtil meshUtility) {
		ColorRGBA color = colorExtractor.getColor(cell, map).clone().mult(1.1f);
		ColorRGBA color2 = color.mult(0.8f);
		
		meshUtility.addColor(color);

		for (@SuppressWarnings("unused")
		Direction direction : Direction.values()) {
			HexCell neighbor = cell.getNeighbor(direction);
			HexCell neighborp = cell.getNeighbor(direction.previous());

			// internal point
			meshUtility.addColor(color);

			// corner
			if (neighbor == null || neighborp == null) {
				meshUtility.addColor(color2);
			} else if (neighbor.getElevation() != cell.getElevation() || neighborp.getElevation() != cell.getElevation()) {
				meshUtility.addColor(color2);
			} else {
				meshUtility.addColor(color);
			}

			// bridge
			if (neighbor == null || neighbor.getElevation() != cell.getElevation()) {
				meshUtility.addColor(color2);
				meshUtility.addColor(color2);
			} else {
				meshUtility.addColor(color);
				meshUtility.addColor(color);
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

				if (perturbated) {
					Perturbator.perturbate(v1);
					Perturbator.perturbate(v2);
					Perturbator.perturbate(v3);
					Perturbator.perturbate(v4);
				}

				meshUtility.addQuad(v1, v2, v3, v4);
				points.put(v4, cell);
			}
		}
	}

	protected void colorizeCellSide(HexCell cell, MeshUtil meshUtility) {
		for (Direction direction : Direction.values()) {
			float coeff = getColorCoefficient(cell, direction);
			ColorRGBA c1 = colorExtractor.getColor(cell, map).mult(coeff);
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


