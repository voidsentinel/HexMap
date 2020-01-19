/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.HashedList;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * @author Xerces
 *
 */
public class IslandDown extends AbstractTerrainGenerator {

	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + IslandDown.class.getSimpleName());
		float[][] copy = new float[ySize][xSize];

		float coeff = (1f / Math.min((xSize - 1) / 2f, (ySize - 1) / 2f));
		int xCenter = xSize / 2;
		int yCenter = ySize / 2;

		HexCoordinates source = new HexCoordinates(xCenter, yCenter);
		copy[source.row][source.column] = 1f;

		HashedList<String, HexCoordinates> toTreat = new HashedList<String, HexCoordinates>();
		Map<String, HexCoordinates> treated = new HashMap<String, HexCoordinates>();
		while (source != null) {
			float sourceValue = copy[source.row][source.column];
			toTreat.remove(source.toString());
			List<HexCoordinates> down = source.inRange(1);
			Iterator<HexCoordinates> it = down.iterator();
			while (it.hasNext()) {
				HexCoordinates point = it.next();
				if (point.row >= 0 && point.row < ySize && point.column >= 0 && point.column < xSize) {
					if (!toTreat.containsKey(point.toString()) && !treated.containsKey(point.toString())) {
						float variation = coeff * Alea.nextFloat() * 2;
						float value = sourceValue - variation;
						copy[point.row][point.column] = value;
						toTreat.add(point.toString(), point);
					}
				}
			}
			treated.put(source.toString(), source);
			source = null;
			// get one of the values
			if (toTreat.size() > 0) {
				source = toTreat.get(Alea.nextInt(toTreat.size()));
			}
		}
		this.normalize(copy);
		TerrainImage.generateImage(copy, "island");
		return copy;
	}
}
