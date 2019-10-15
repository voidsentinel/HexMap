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
public class HexGridChunkFlatSimple2 extends AbstractHexGridChunk {

	final float	nbEdge		= 5f;
	final float	edgecoeff	= 1f / (1f + nbEdge);

	public HexGridChunkFlatSimple2(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
	      AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, perturbated, colorExtractor);
	}

	public boolean canBePerturbated() {
		return true;
	}

	/**
	 * generate and store the geometry for a given map
	 * 
	 * @param map
	 * @return the generated geometry.
	 */
	protected void generateSpecializedGeometries(Node localRoot) {
		Mesh mesh = new Mesh();
		Geometry terrain = new Geometry("ground", mesh);
		terrain.setMaterial(this.getTerrainMaterial());
		localRoot.attachChild(terrain);

		generateStructure();
		generateColor(colorExtractor);

	}

	/**
	 * Should be called only if representation is non empty. Will extract the colors
	 * for each cell with the new extractor, and fill the color buffer of the mesh
	 * with the new values
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
	 * @param MeshUtility
	 */
	private void triangulateCellCenter(HexCell cell, MeshUtil MeshUtility) {
		Vector3f center = HexMetrics.getCellCenter(cell);
		Vector3f v2 = null;
		Vector3f v3 = null;
		Vector3f v4 = null;
		int index = MeshUtility.getVerticeCount();
		int offsetDir = 0;
		int offsetDirNext = 0;
		int count = 0;

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		points.put(center, cell);

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			v2 = center.add(HexMetrics.getFirstCornerVector(offsetDir, 1f));
			v3 = center.add(HexMetrics.getFirstCornerVector(direction.next().ordinal(), 1f));
			if (perturbated) {
				Perturbator.perturbate(v2);
				Perturbator.perturbate(v3);
			}
			MeshUtility.addVertice(v2);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			count++;
			points.put(v2, cell);

			for (int i = 1; i <= nbEdge; i++) {
				float amount = i * edgecoeff;
				v4 = new Vector3f().interpolateLocal(v2, v3, amount);
				if (perturbated) {
					Perturbator.perturbate(v4, 0.5f);
				}
				MeshUtility.addVertice(v4);
				MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
				count++;
				MeshUtility.addTriangle(index, index + count, index + count - 1);

				if (i % 2 == 0) {
					points.put(v4, cell);
				}
			}

		}

		for (Direction direction : Direction.values()) {
			offsetDir = direction.ordinal();
			offsetDirNext = direction.previous().ordinal();
			MeshUtility.addTriangle(index, (int) (index + offsetDir * (nbEdge + 1) + 1),
			      (int) (index + (offsetDirNext + 1) * (nbEdge + 1)));
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
			for (int i = 0; i <= nbEdge; i++) {
				MeshUtility.addColor(color);
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
				if (perturbated) {
					Perturbator.perturbate(v1);
					Perturbator.perturbate(v2);
				}
				Vector3f v3 = v1.clone();
				Vector3f v4 = v2.clone();
				v1.y = cell.getElevation() * HexMetrics.CELL_ELEVATION;
				v2.y = v1.y;
				v3.y = (cell.getElevation() - height) * HexMetrics.CELL_ELEVATION;
				v4.y = v3.y;

				Vector3f vlast1 = v1;
				Vector3f vlast2 = v3;

				for (int i = 1; i <= nbEdge; i++) {
					float amount = i * edgecoeff;
					Vector3f v5 = new Vector3f().interpolateLocal(v1, v2, amount);
					Vector3f v6 = new Vector3f().interpolateLocal(v3, v4, amount);
					if (perturbated) {
						Perturbator.perturbate(v5, 0.5f);
						Perturbator.perturbate(v6, 0.5f);
					}

					meshUtility.addQuad(vlast1, v5, vlast2, v6);

					vlast1 = v5;
					vlast2 = v6;

					points.put(v6, cell);
				}

				meshUtility.addQuad(vlast1, v2, vlast2, v4);

			}
		}
	}

	protected void colorizeCellSide(HexCell cell, MeshUtil meshUtility) {
		for (Direction direction : Direction.values()) {
			ColorRGBA c1 = colorExtractor.getColor(cell, map).clone().mult(this.getColorCoefficient(cell, direction));
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
			for (int i = 0; i <= nbEdge; i++) {
				meshUtility.addQuadColors(c1, c1, c1, c1);
			}
		}
	}

}
