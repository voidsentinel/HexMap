package org.voidsentinel.hexmap.view;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.Direction;
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
	protected static final Logger				LOG						= Logger
	      .getLogger(AbstractHexGridChunk.class.toString());

	// prefix for the Chunk Node
	static final public String					CHUNK_PREFIX			= "CHUNK_";

	// perturbation source
	protected static final FastNoise			fn							= new FastNoise(Alea.nextInt());

	// the (part of) map to display
	protected HexMap								map;
	final protected int							xStart;
	final protected int							zStart;
	final protected int							xEnd;
	final protected int							zEnd;

	// representation informations
	protected boolean								perturbated				= false;
	protected boolean								perturbationPossible	= false;
	protected boolean								lighted					= false;
	// the geometryNode corresponding to the (part of) map
	protected Node									representation			= null;
	// points associated with each cell. Should be the minimal number of points (ie
	// the points that are associated with the max nb of triangle)
	protected Map<Vector3f, HexCell>			points					= new HashMap<Vector3f, HexCell>();

	// the colorExtractor
	protected AbstractCellColorExtractor	colorExtractor;
	protected Material							terrainMaterial		= null;

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
		this.representation = new Node(CHUNK_PREFIX + xStart + "_" + zStart);
		this.setPerturbated(perturbated);
	}

	/**
	 * generate the geometry for a given map portion.
	 * 
	 * @see representation
	 */
	public final void generateGeometry() {
		generateSpecializedGeometries(representation);
	}

	/**
	 * indicate if the representation can be perturbed If set to false, this will
	 * cancel any try to set perturbation on.
	 * 
	 * @return true if the representation can be perturbed
	 */
	public abstract boolean canBePerturbated();

	/**
	 * generate the representation(s) for the chunk, and attach it to the given
	 * node. The representation may consist of several subnode. The protected
	 * HashMap points should contains the points that can be checked to find a cell
	 * when clicking on screen
	 * 
	 * @param localRoot the root node for the current chunk (and this chunk alone)
	 */
	protected abstract void generateSpecializedGeometries(Node localRoot);

	/**
	 * Will (re) generate the colors of the map representation with the given
	 * colorExtractor. Should be called only if representation is non empty.
	 * 
	 * @param colorExtractor the new colorExtractor to use.
	 */
	public abstract void generateColor(AbstractCellColorExtractor colorExtractor);

	/**
	 * will (re) generate the mesh structure (vertices, normals, triangles) of the
	 * map representation. Should be called only if representation is non empty.
	 */
	public abstract void generateStructure();

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

	/**
	 * set the pertubation on or off, if allowed
	 * 
	 * @param perturbated true if on.
	 * @see AbstractHexGridChunk.canBePerturbated
	 */
	public void setPerturbated(boolean perturbated) {
		this.perturbated = perturbated && this.canBePerturbated();
	}

	/**
	 * Return the perturbation of a given vertex.
	 * 
	 * @param v1 the vertex whose pertubation is requested
	 * @return the perturbation to be added
	 */
	protected Vector3f getPerturbation(Vector3f v1) {
		final float VARIATION = HexMetrics.INNERRADIUS * 0.75f;
		float o1 = Math.abs(fn.GetPerlin(v1.x * 130, v1.z * 170)) * VARIATION - 0.5f * VARIATION;
		float o2 = Math.abs(fn.GetPerlin(v1.z * 130, v1.x * 170)) * VARIATION - 0.5f * VARIATION;
		return new Vector3f(o1, 0f, o2);
	}

	/**
	 * Apply the perturbatio to the given vertice. Equilavalent to
	 * v1.addLocal(getPerturbation(v1))
	 * 
	 * @param v1 the vertice to perturbate
	 */
	protected void perturbate(Vector3f v1) {
		v1.addLocal(getPerturbation(v1));
	}

	/**
	 * return a coefficient for the color of a direction. The coefficient is based
	 * on the direction and the height difference, as if the light was comming from
	 * x=0; z= map/2
	 * 
	 * @param cell
	 * @param direction
	 * @return
	 */
	protected float getColorCoefficient(HexCell cell, Direction direction) {
		// should be global protected to be used
		final float[] coeff = new float[] { 0.75f, 0.5f, 0.75f, 1f, 1.15f, 1f };
		float response = 1f;
		HexCell neighbor = cell.getNeighbor(direction);
		if (!lighted && neighbor != null) {
			response = response * coeff[direction.ordinal()];
		}
		return response;
	}
}
