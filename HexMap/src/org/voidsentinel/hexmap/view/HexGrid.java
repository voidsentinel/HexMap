/**
 * 
 */
package org.voidsentinel.hexmap.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.mapColor.ColorMapperRepository;
import org.voidsentinel.hexmap.view.representation.MapRepresentation;
import org.voidsentinel.hexmap.view.representation.MapRepresentationRepository;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 * This class manage the graphic representation of a HexMap
 * 
 * @author VoidSentinel
 *
 */
public class HexGrid {
	protected static final Logger					LOG					= Logger.getLogger(HexGrid.class.toString());

	public static final int							CHUNKSIZE			= 16;

	private HexMap										map					= null;
	private Node										terrainNode			= null;

	private AbstractCellColorExtractor			colorExtractor		= null;
	private MapRepresentation						meshGenerator		= null;

	private Map<String, AbstractHexGridChunk>	chunks				= new HashMap<String, AbstractHexGridChunk>();
	private Material									terrainMaterial	= null;

	/**
	 * Contructor for the representation of a HexMap
	 * 
	 * @param map      the map to display
	 * @param rootNode the 3d node to put the map under
	 */
	public HexGrid(HexMap map, Node rootNode) {
		this.map = map;
		terrainNode = new Node("Terrain");
		rootNode.attachChild(terrainNode);

		// Color for each cell
		colorExtractor = ColorMapperRepository.repository.getDefaultMapper();
		// mesh generator information
		meshGenerator = MapRepresentationRepository.repository.getDefault();

		addLights();

		try {
			setMeshGeneration(meshGenerator.id);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
		      | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			;
		}

	}

	/**
	 * change the colorExtractor, and reapply the extraction to the current map
	 * representation
	 * 
	 * @param extractor the colorExtractor to use
	 */
	public void setColorExtractor(AbstractCellColorExtractor extractor) {
		colorExtractor = extractor;
		Iterator<AbstractHexGridChunk> it = chunks.values().iterator();
		while (it.hasNext()) {
			it.next().generateColor(extractor);
		}
	}

	/**
	 * chenge the metho used to generate the mesh
	 * 
	 * @param id the id of the class to use in MapRepresentationRepository
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void setMeshGeneration(String id) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		terrainNode.detachAllChildren();
		chunks.clear();

		MapRepresentation generatorInfo = MapRepresentationRepository.repository.getData(id);

		LOG.info("Setting Mesh generator to " + I18nMultiFile.getText(generatorInfo.getLabelName()));

		AssetManager assets = HexTuto.getInstance().getAssetManager();
		terrainMaterial = (Material) assets.loadMaterial(generatorInfo.getMaterialName());

		Class<?> clazz = Class.forName(generatorInfo.getClassName());
		Constructor<?> ctor = clazz.getConstructor(HexMap.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE,
		      AbstractCellColorExtractor.class);

		for (int z = 0; z < map.HEIGHT; z = z + CHUNKSIZE) {
			for (int x = 0; x < map.WIDTH; x = x + CHUNKSIZE) {
				Object object = ctor
				      .newInstance(new Object[] { map, x, z, CHUNKSIZE, generatorInfo.isPerturbated(), colorExtractor });
				AbstractHexGridChunk generator = ((AbstractHexGridChunk) (object));
				generator.setTerrainMaterial(terrainMaterial);
				generator.generateGeometry();
				terrainNode.attachChild(generator.getRepresentation());
				chunks.put(generator.getRepresentation().getName(), generator);
			}
		}
	}

	public MapRepresentation getMapRepresentation() {
		return meshGenerator;
	}

	/**
	 * Set all the chunk to the perturbated status. The result depends on the
	 * canBePetrubed status.
	 * 
	 * @param perturbated tru if the map should be perturbated
	 */
	public void setPerturbated(boolean perturbated) {
		Iterator<AbstractHexGridChunk> it = chunks.values().iterator();
		while (it.hasNext()) {
			AbstractHexGridChunk chunk = it.next();
			chunk.setPerturbated(perturbated);
			chunk.generateStructure();
		}
	}

	/**
	 * return the node where the map is to be added. there may be Node/spatial as
	 * child, depending on the map and material
	 * 
	 * @return the Node that contains the terrain mesh
	 */
	public Node getNode() {
		return terrainNode;
	}

	/**
	 * return the cell under the cursor (as defined by a 2d point and the camera)
	 * 
	 * @param cursor
	 * @param camera
	 * @return the HexCell or null
	 */
	public HexCell getCellPointed(Vector2f cursor, Camera camera) {
		HexCell selectedCell = null;
		Vector3f click3d = camera.getWorldCoordinates(new Vector2f(cursor.x, cursor.y), 0f).clone();
		Vector3f dir = camera.getWorldCoordinates(new Vector2f(cursor.x, cursor.y), 1f).subtractLocal(click3d)
		      .normalizeLocal();

		// 1. Reset results list.
		CollisionResults results = new CollisionResults();
		// 2. Aim the ray from cam loc to cam direction.
		Ray ray = new Ray(click3d, dir);
		// 3. check againts all geometry children
		terrainNode.collideWith(ray, results);
		// 4. get the geometry
		CollisionResult closest = results.getClosestCollision();
		if (closest != null) {
			// find the Chunk Node corresponding to the selection
			Node result = closest.getGeometry().getParent();
			System.out.println("Selected Bloc" + result.getName());
			if (result.getName().startsWith(AbstractHexGridChunk.CHUNK_PREFIX)) {
				Triangle collisionTriangle = new Triangle();
				closest.getTriangle(collisionTriangle);
				// find the AbstractBlock that goes with this Node

				selectedCell = chunks.get(result.getName()).getCell(collisionTriangle);
				if (selectedCell != null) {
					System.out.println("position    " + selectedCell.hexCoordinates);
					System.out.println("terrain     " + selectedCell.getTerrain().id);
					System.out.println("Height      " + selectedCell.getHeight());
					System.out.println("Humidity    " + selectedCell.getFloatData(HexCell.HUMIDITY_DATA));
					System.out.println("Temperature " + selectedCell.getFloatData(HexCell.TEMPERATURE_DATA));
				}
			}
		}

		return selectedCell;
	}

	/**
	 * 
	 */
	private void addLights() {
		DirectionalLight light = new DirectionalLight();
		light.setColor(ColorRGBA.White.mult(1.1f));
		light.setDirection(new Vector3f(5f, 4f, -2f).normalizeLocal());
		terrainNode.addLight(light);

		DirectionalLight light2 = new DirectionalLight();
		light2.setColor(ColorRGBA.White.mult(0.75f));
		light2.setDirection(new Vector3f(1f, 1f, 1f).normalizeLocal());
		terrainNode.addLight(light2);
	}

}
