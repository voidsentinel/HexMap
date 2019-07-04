/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.AbstractTerrainGenerator;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.AbstractTerrainOperation;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * @author Xerces
 *
 */
public class HeightMapOperation extends AbstractTerrainAction implements IMapOperation {

	private List<AbstractTerrainGenerator>	generators	= new ArrayList<AbstractTerrainGenerator>();
	private List<AbstractTerrainOperation>	operations	= new ArrayList<AbstractTerrainOperation>();

	@Override
	public void filter(HexMap map) {
		float[][] values = new float[map.HEIGHT][map.WIDTH];
		Iterator<AbstractTerrainGenerator> it = generators.iterator();
		while (it.hasNext()) {
			AbstractTerrainGenerator generator = it.next();
			float[][] local = generator.generate(map.WIDTH, map.HEIGHT);
			for (int y = 0; y < map.HEIGHT; y++) {
				for (int x = 0; x < map.WIDTH; x++) {
					values[y][x] = values[y][x] + local[y][x];
				}
			}
		}
		normalize(values);

		Iterator<AbstractTerrainOperation> op = operations.iterator();
		while (op.hasNext()) {
			AbstractTerrainOperation operation = op.next();
			operation.filter(values);
		}
		normalize(values);

		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				cell.setHeight(values[y][x]);
			}
		}

		TerrainImage.generateImage(values, this.getClass().getSimpleName());

	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addGenerator(AbstractTerrainGenerator arg0) {
		return generators.add(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void addGenerator(int arg0, AbstractTerrainGenerator arg1) {
		generators.add(arg0, arg1);
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clearGenerators() {
		generators.clear();
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public AbstractTerrainGenerator removeGenerator(int arg0) {
		return generators.remove(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean removeGenerator(Object arg0) {
		return generators.remove(arg0);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int generatorSize() {
		return generators.size();
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addOperation(AbstractTerrainOperation e) {
		return operations.add(e);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void addOperation(int index, AbstractTerrainOperation element) {
		operations.add(index, element);
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clearOperations() {
		operations.clear();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public AbstractTerrainOperation removeOperation(int index) {
		return operations.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean removeOperation(Object o) {
		return operations.remove(o);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int operationSize() {
		return operations.size();
	}

}
