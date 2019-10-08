package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.view.AbstractHexGridChunk;
import org.voidsentinel.hexmap.view.HexEdgeType;
import org.voidsentinel.hexmap.view.HexMetrics;
import org.voidsentinel.hexmap.view.MeshUtil;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;

/**
 * generate and store the representation of a chunk of the map. also store
 * vertices that allow for selection of an hex. The colors and texture of the
 * representation can be modified
 * 
 * @author guipatry
 *
 */
public class HexGridChunkTerraced extends AbstractHexGridChunk {

	// number of terrace per slope
	public final int		TERRACEPERSLOPE		= 2;
	// number of steps per slope
	public final int		TERRACESTEPS			= TERRACEPERSLOPE * 2 + 1;
	public final float	HORIZONTALSTEPSIZE	= 1f / TERRACESTEPS;
	public final float	VERTICALSTEPSIZE		= 1f / (TERRACEPERSLOPE + 1);

	public HexGridChunkTerraced(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
	      AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, perturbated, colorExtractor);
	}

	public boolean canBePerturbated() {
		return false;
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

	public void generateColor(AbstractCellColorExtractor colorExtractor) {
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
		((Geometry) (representation.getChild("ground"))).getMesh().setBuffer(Type.Color, 4, meshUtility.getColorArray());
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
				triangulateCellBridge(hexCell, meshUtility);
				triangulateCellCorner(hexCell, meshUtility);
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
		triangulateBridgeDirection(cell, Direction.NE, MeshUtility);
		triangulateBridgeDirection(cell, Direction.EAST, MeshUtility);
		triangulateBridgeDirection(cell, Direction.SE, MeshUtility);
	}

	/**
	 * triangulate the bridge between 2 cells for a given direction
	 * 
	 * @param cell
	 * @param direction
	 * @param MeshUtility
	 */
	private void triangulateBridgeDirection(HexCell cell, Direction direction, MeshUtil meshUtility) {
		HexCell neighbor = cell.getNeighbor(direction);
		if (neighbor != null) {
			Vector3f center = HexMetrics.getCellCenter(cell);
			Vector3f bridge = HexMetrics.getBridgeVector(direction.ordinal()).multLocal(2);
			Vector3f v1 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
			Vector3f v2 = center.add(HexMetrics.getSecondCornerVector(direction.ordinal()));
			Vector3f v3 = v1.add(bridge);
			Vector3f v4 = v2.add(bridge);
			v3.y = neighbor.getElevation() * HexMetrics.CELL_ELEVATION;
			v4.y = v3.y;

			HexEdgeType edge = HexMetrics.getEdgeType(cell.getElevation(), neighbor.getElevation());
			switch (edge) {
			case Flat:
			case Cliff:
				ColorRGBA c1 = colorExtractor.getColor(cell, map);
				ColorRGBA c2 = colorExtractor.getColor(neighbor, map);
				meshUtility.addQuad(v1, v2, v3, v4);
				meshUtility.addQuadColors(c1, c1, c2, c2);
				break;
			case Terrace:
				triangulateEdgeTerraces(v1, v2, cell, v3, v4, neighbor, meshUtility);
				break;
			}
		}
	}

	private void triangulateEdgeTerraces(Vector3f beginLeft, Vector3f beginRight, HexCell beginCell, Vector3f endLeft,
	      Vector3f endRight, HexCell endCell, MeshUtil meshUtility) {
		ColorRGBA c1 = colorExtractor.getColor(beginCell, map);
		ColorRGBA c2 = colorExtractor.getColor(endCell, map);
		Vector3f v1 = beginLeft;
		Vector3f v2 = beginRight;

		for (int i = 1; i <= TERRACESTEPS; i++) {
			Vector3f v3 = interpolateTerraceLerp(beginLeft, endLeft, i);
			Vector3f v4 = interpolateTerraceLerp(beginRight, endRight, i);
			ColorRGBA c3 = interpolateTerraceColor(c1, c2, i);
			meshUtility.addQuad(v1, v2, v3, v4);
			meshUtility.addQuadColors(c1, c1, c3, c3);
			v1 = v3;
			v2 = v4;
			c1 = c3;
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
		// either don't exist (border) or will have been created by the previous row (SW
		// & SE) or column (WEST)
	}

	/**
	 * Triangulate a given corner (by direction)
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
		if (h1 != null && h2 != null) {
			Vector3f normal = new Vector3f();
			Vector3f o1 = HexMetrics.getBridgeVector(direction.ordinal()).multLocal(2f);
			Vector3f o2 = HexMetrics.getBridgeVector(direction.previous().ordinal()).multLocal(2f);
			Vector3f v0 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal()));
			Vector3f v1 = v0.add(o1);
			Vector3f v2 = v0.add(o2);
			v1.y = h1.getElevation() * HexMetrics.CELL_ELEVATION;
			v2.y = h2.getElevation() * HexMetrics.CELL_ELEVATION;

			ColorRGBA c0 = colorExtractor.getColor(cell, map);
			ColorRGBA c1 = colorExtractor.getColor(h1, map);
			ColorRGBA c2 = colorExtractor.getColor(h2, map);

			Triangle.computeTriangleNormal(v0, v1, v2, normal);
			meshUtility.addVertice(v0);
			meshUtility.addVertice(v1);
			meshUtility.addVertice(v2);
			meshUtility.addNormal(normal);
			meshUtility.addNormal(normal);
			meshUtility.addNormal(normal);
			meshUtility.addColor(c0);
			meshUtility.addColor(c1);
			meshUtility.addColor(c2);
			meshUtility.addTriangle(index, index + 1, index + 2);

			// add one of the points to the list of point for selection, with the highest
			// cell as
			// destination
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @param step
	 * @return
	 */
	protected Vector3f interpolateTerraceLerp(Vector3f a, Vector3f b, int step) {
		Vector3f response = new Vector3f();
		float h = step * HORIZONTALSTEPSIZE;
		response.x = a.x + (b.x - a.x) * h;
		response.z = a.z + (b.z - a.z) * h;
		float v = ((step + 1) / 2) * VERTICALSTEPSIZE;
		response.y = a.y + (b.y - a.y) * v;
		return response;
	}

	protected ColorRGBA interpolateTerraceColor(ColorRGBA a, ColorRGBA b, int step) {
		ColorRGBA response = new ColorRGBA();
		float h = step * HORIZONTALSTEPSIZE;
		response.interpolateLocal(a, b, h);
		return response;
	}

}
