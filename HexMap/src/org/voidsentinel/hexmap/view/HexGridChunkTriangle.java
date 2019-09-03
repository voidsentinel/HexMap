package org.voidsentinel.hexmap.view;

import java.util.Iterator;
import java.util.List;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
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
public class HexGridChunkTriangle extends AbstractHexGridChunk {

	public HexGridChunkTriangle(HexMap map, int xstart, int zstart, int chunkSize,
	      AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, false,colorExtractor);
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
				colorizeCellCenter(hexCell, MeshUtility);
			}
		}
		perturbatePositions(MeshUtility);

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
				colorizeCellCenter(hexCell, meshUtility);
			}
		}
		((Geometry) (representation.getChild("ground"))).getMesh().setBuffer(Type.Color, 4, meshUtility.getColorArray());
	}

	private void perturbatePositions(MeshUtil meshUtil) {
		List<Vector3f> vertices = meshUtil.getVertices();
		FastNoise noise = new FastNoise(Alea.nextInt());
		Iterator<Vector3f> it = vertices.iterator();
		while (it.hasNext()) {
			Vector3f vertex = it.next();
			Vector3f perturb = new Vector3f(noise.GetNoise(vertex.x, vertex.z), 0f, noise.GetNoise(vertex.z, vertex.y))
			      ;
			vertex.addLocal(perturb);
		}

	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	private void triangulateCellCenter(HexCell cell, MeshUtil MeshUtility) {
		int index = MeshUtility.getVerticeCount();
		Vector3f v1 = null;
		Vector3f v2 = null;
		Vector3f v3 = null;
		Vector3f center = HexMetrics.getCellCenter(cell);
		Vector3f normalCenter = new Vector3f();
		Vector2f UVCenter = cell.getTerrain().getCenterUV(cell.random);

		MeshUtility.addVertice(center);
		MeshUtility.addNormal(normalCenter);
		MeshUtility.addUV(UVCenter);

		TerrainData terrain = cell.getTerrain();

		for (Direction direction : Direction.values()) {

			Vector3f normal1 = new Vector3f();
			Vector3f normal2 = new Vector3f();

			HexCell c1 = cell.getNeighbor(direction.next());
			HexCell c2 = cell.getNeighbor(direction);
			HexCell c3 = cell.getNeighbor(direction.previous());
			HexCell c4 = cell.getNeighbor(direction.previous().previous());

			v1 = center.add(HexMetrics.getFirstCornerVector(direction.next().ordinal(), 1f));
			v1.y = HexMetrics.getMeanHeight(cell, c1, c2);

			v2 = center.add(HexMetrics.getFirstCornerVector(direction.ordinal(), 1f));
			v2.y = HexMetrics.getMeanHeight(cell, c2, c3);

			v3 = center.add(HexMetrics.getFirstCornerVector(direction.previous().ordinal(), 1f));
			v3.y = HexMetrics.getMeanHeight(cell, c3, c4);

			Triangle.computeTriangleNormal(center, v1, v2, normal1);
			Triangle.computeTriangleNormal(center, v2, v3, normal2);
			normal2.addLocal(normal1);
			normal2.normalizeLocal();
			normalCenter.addLocal(normal1);

			MeshUtility.addVertice(v2);
			MeshUtility.addNormal(normal2);

			Vector2f UVDir = new Vector2f(HexMetrics.getFirstCornerVector(direction.ordinal(), 1f).x,
			      HexMetrics.getFirstCornerVector(direction.ordinal(), 1f).z).mult(terrain.getUVSize());
			MeshUtility.addUV(UVCenter.add(UVDir));

			MeshUtility.addTriangle(index, index + direction.ordinal() + 1, index + direction.previous().ordinal() + 1);
			points.put(v2, cell);
		}

		normalCenter.normalize();
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
		for (Direction direction : Direction.values()) {
			MeshUtility.addColor(color);
		}
	}

}
