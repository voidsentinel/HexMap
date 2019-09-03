package org.voidsentinel.hexmap.view;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.material.Material;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * generate and store the representation of a chunk of the map. also store
 * vertices that allow for selection of an hex. The colors and texture of the
 * representation can be modified
 * 
 * @author guipatry
 *
 */
public abstract class AbstractHexGridChunk {
	protected static final Logger				LOG					= Logger.getLogger(AbstractHexGridChunk.class.toString());

	// prefix for the Chunk Node
	static final public String					CHUNK_PREFIX		= "CHUNK_";

	// perturbation source
	protected static final FastNoise			fn						= new FastNoise(Alea.nextInt());

	// the (part of) map to display
	protected HexMap								map;
	final protected int							xStart;
	final protected int							zStart;
	final protected int							xEnd;
	final protected int							zEnd;
   // representation informations
	protected boolean								perturbated			= false;

	// the geometryNode corresponding to the (part of) map
	protected Node									representation		= null;
	// points associated with each cell. Should be the minimal number of points (ie
	// the points that are associated with the max nb of triangle)
	protected Map<Vector3f, HexCell>			points				= new HashMap<Vector3f, HexCell>();

	// the colorExtractor
	protected AbstractCellColorExtractor	colorExtractor;
	protected Material							terrainMaterial	= null;

	/**
	 * Constructor
	 * 
	 * @param map            the map to display
	 * @param xstart         the x position that the chunk will start to display
	 * @param zstart         the z position that the chunk will start to display
	 * @param chunkSize      the size of the displayed area (will display to
	 *                       xtsart+size and zstart+size)
	 * @param colorExtractor the color Extractor to use
	 */
	public AbstractHexGridChunk(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
	      AbstractCellColorExtractor colorExtractor) {
		this.map = map;
		this.xStart = xstart;
		this.zStart = zstart;
		this.xEnd = Math.min(xstart + chunkSize - 1, map.WIDTH - 1);
		this.zEnd = Math.min(zstart + chunkSize - 1, map.HEIGHT - 1);
		this.colorExtractor = colorExtractor;
		this.perturbated = perturbated;
		this.representation = new Node(CHUNK_PREFIX + xStart + "_" + zStart);
	}

	/**
	 * generate the geometry for a given map portion this will fill the
	 * representation member value
	 * 
	 * @see representation
	 */
	public abstract void generateGeometry();

	/**
	 * Should be called only if representation is non empty. Will extract the colors
	 * for each cell with the new extractor, and fill the color buffer of the mesh
	 * with the new values
	 * 
	 * @param colorExtractor the new colorExtractor to use.
	 */
	public abstract void regenerateColor(AbstractCellColorExtractor colorExtractor);

	/**
	 * return the representation of the part of the map.
	 * 
	 * @return the representation (Geometry) if it has already be generated, null
	 *         otherwise
	 */
	public Node getRepresentation() {
		return representation;
	}

	/**
	 * return the cell whose representation contains this triangle, or null if none.
	 * 
	 * @param collisionTriangle
	 * @return
	 */
	public HexCell getCell(Triangle collisionTriangle) {
		HexCell response = null;
		if (response == null)
			response = points.get(collisionTriangle.get3());
		if (response == null)
			response = points.get(collisionTriangle.get2());
		if (response == null)
			response = points.get(collisionTriangle.get3());
		return response;
	}

	/**
	 * @return the terrainMaterial
	 */
	public Material getTerrainMaterial() {
		return terrainMaterial;
	}

	/**
	 * @param terrainMaterial the terrainMaterial to set
	 */
	public void setTerrainMaterial(Material terrainMaterial) {
		this.terrainMaterial = terrainMaterial;
	}

	protected void perturbate(Vector3f v1) {
		final float VARIATION = HexMetrics.INNERRADIUS / 1.3f;

		float o1 = fn.GetPerlin(v1.x * 30, v1.z * 70) * VARIATION - 0.5f * VARIATION;
		float o2 = fn.GetPerlin(v1.z * 30, v1.x * 70) * VARIATION - 0.5f * VARIATION;
		v1.addLocal(o1, 0f, o2);
	}

}
