/**
 * 
 */
package org.voidsentinel.hexmap.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.mapColor.colorMapperRepository;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
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
 * @author guipatry
 *
 */
public class HexGrid {

	public static final int							CHUNKSIZE		= 124;

	private HexMap										map				= null;
	private Node										terrainNode		= null;

	private AbstractCellColorExtractor			colorExtractor	= null;
	private Map<String, AbstractHexGridChunk>	chunks			= new HashMap<String, AbstractHexGridChunk>();

	public HexGrid(HexMap map, Node rootNode) {
		this.map = map;
		terrainNode = new Node("Terrain");
		rootNode.attachChild(terrainNode);

		//
		colorExtractor = colorMapperRepository.repository.getDefaultMapper();

		addLights();

		for (int z = 0; z < map.HEIGHT; z = z + CHUNKSIZE) {
			for (int x = 0; x < map.WIDTH; x = x + CHUNKSIZE) {
				AbstractHexGridChunk generator = new HexGridChunkSlopped(map, x, z, CHUNKSIZE, colorExtractor);
				generator.generateGeometry();
				terrainNode.attachChild(generator.getRepresentation());
				chunks.put(generator.getRepresentation().getName(), generator);
			}
		}
	}

	public void setColorExtractor(AbstractCellColorExtractor extractor) {
		colorExtractor = extractor;
		Iterator<AbstractHexGridChunk> it = chunks.values().iterator();
		while (it.hasNext()) {
			it.next().regenerateColor(extractor);
		}
	}

	public void setMeshGeneration(String generatorName)
	      throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		terrainNode.detachAllChildren();
		chunks.clear();
		Class<?> clazz = Class.forName(generatorName);
		Constructor<?> ctor = clazz.getConstructor(HexMap.class, Integer.TYPE, Integer.TYPE, Integer.TYPE,
		      AbstractCellColorExtractor.class);

		for (int z = 0; z < map.HEIGHT; z = z + CHUNKSIZE) {
			for (int x = 0; x < map.WIDTH; x = x + CHUNKSIZE) {
				Object object = ctor.newInstance(new Object[] { map, x, z, CHUNKSIZE, colorExtractor });
				AbstractHexGridChunk generator = ((AbstractHexGridChunk) (object));
				generator.generateGeometry();
				terrainNode.attachChild(generator.getRepresentation());
				chunks.put(generator.getRepresentation().getName(), generator);
			}
		}

	}

	public Node getNode() {
		return terrainNode;
	}

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
					System.out.println("SelectedCell position " + selectedCell.hexCoordinates);
					System.out.println("SelectedCell to Water " + selectedCell.getDistanceToWater());
					System.out.println("SelectedCell Height   " + selectedCell.getHeight());
				}
			}
		}

		return selectedCell;
	}

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
