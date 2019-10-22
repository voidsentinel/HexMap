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
public class HexGridChunkFlatSimple3 extends AbstractHexGridChunk {

	final float	nbEdge		= 5f;
	final float	edgecoeff	= 1f / (1f + nbEdge);

	public HexGridChunkFlatSimple3(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
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
//				colorizeCellSide(hexCell, meshUtility);
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
				// triangulateCellSide(hexCell, meshUtility);
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
		final int NBPOINTS = 7;
		Vector3f center = HexMetrics.getCellCenter(cell);
		Vector3f v1 = null;
		Vector3f vn = null;
		Vector3f p1 = null;
		Vector3f pn = null;
		Vector3f v2 = null;
		Vector3f v3 = null;
		Vector3f v4 = null;
		Vector3f v5 = null;
		Vector3f v6 = null;
		Vector3f v7 = null;

		int index = MeshUtility.getVerticeCount();
		int offsetDir = 0;
		int offsetDirNext = 0;

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
		points.put(center, cell);

		for (Direction direction : Direction.values()) {
			offsetDir = index + direction.ordinal() * NBPOINTS;
			offsetDirNext = index + direction.next().ordinal() * NBPOINTS;

			v1 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal(), 1f));
			vn = center.add(HexMetrics.getFirstCornerVector(direction.next().ordinal(), 1f));
			p1 = Perturbator.getPerturbation(v1);
			pn = Perturbator.getPerturbation(vn);

			v2 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
			v3 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal(), 0.5f));

			if (perturbated) {
//				v1.addLocal(p1);
//				v2.addLocal(p1);
//				vn.addLocal(pn);
			}

			v4 = new Vector3f();
			v4.interpolateLocal(v1, vn, HexMetrics.BLURFACTOR / 2f);
			v5 = new Vector3f();
			v5.interpolateLocal(v1, vn, 0.25f);
			v6 = new Vector3f();
			v6.interpolateLocal(v1, vn, 1f - 0.25f);
			v7 = new Vector3f();
			v7.interpolateLocal(v1, vn, 1f - HexMetrics.BLURFACTOR / 2f);

//			HexCell cellDir = cell.getNeighbor(direction);
//			if (cellDir != null && cell.getElevation() - cellDir.getElevation() == 1) {
//				Vector3f center2 = HexMetrics.getCellCenter(cellDir);
//				v5.y = center.y - center2.y;
//				v6.y = center2.y;
//			}			
			
			MeshUtility.addVertice(v3);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v2);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v1);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v4);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v5);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v6);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);
			MeshUtility.addVertice(v7);
			MeshUtility.addNormal(HexMetrics.CELL_UNIT_NORMAL);

			MeshUtility.addTriangle(index, offsetDirNext + 1, offsetDir + 1);
			MeshUtility.addTriangle(offsetDir + 1, offsetDir + 5, offsetDir + 2);
			MeshUtility.addTriangle(offsetDir + 2, offsetDir + 5, offsetDir + 4);
			MeshUtility.addTriangle(offsetDir + 2, offsetDir + 4, offsetDir + 3);

			MeshUtility.addTriangle(offsetDirNext + 1, offsetDirNext + 2, offsetDir + 6);
			MeshUtility.addTriangle(offsetDirNext + 2, offsetDir + 7, offsetDir + 6);
			MeshUtility.addTriangle(offsetDirNext + 2, offsetDirNext + 3, offsetDir + 7);

			MeshUtility.addTriangle(offsetDir + 1, offsetDir + 6, offsetDir + 5);
			MeshUtility.addTriangle(offsetDir + 1, offsetDirNext + 1, offsetDir + 6);

			points.put(v1, cell);
			points.put(v2, cell);
			points.put(v6, cell);

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
			MeshUtility.addColor(color);

			HexCell cellDir = cell.getNeighbor(direction);
			HexCell cellDirNext = cell.getNeighbor(direction.previous());

			if ((cellDir != null && cellDir.getElevation() != cell.getElevation())
			      || (cellDirNext != null && cellDirNext.getElevation() != cell.getElevation())) {
				MeshUtility.addColor(color.mult(0.8f));
			} else {
				MeshUtility.addColor(color);
			}

			if (cellDir != null && cellDir.getElevation() != cell.getElevation()) {
				MeshUtility.addColor(color.mult(0.8f));
				MeshUtility.addColor(color.mult(0.8f));
				MeshUtility.addColor(color.mult(0.8f));
				MeshUtility.addColor(color.mult(0.8f));
			} else {
				MeshUtility.addColor(color);
				MeshUtility.addColor(color);
				MeshUtility.addColor(color);
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
						Perturbator.perturbate(v5, 0.5f, 2);
						Perturbator.perturbate(v6, 0.5f, 2);
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
