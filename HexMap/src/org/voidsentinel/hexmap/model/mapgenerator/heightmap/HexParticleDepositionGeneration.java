/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.Alea;

/**
 * This Generator create a map by placing 0.1 to a random point, moving in one
 * direction and doing it again a given number of time (nbParticle). This is
 * done several times (Nb Droplet). As for all generators, the (normalized)
 * final value is added to the initial table
 * 
 * @author guipatry
 *
 */
public class HexParticleDepositionGeneration extends AbstractTerrainGenerator {

	private int							nbDropplet		= 1;
	private int							particleCount	= 1;
	private int							sizeX;
	private int							sizeY;

	/**
	 * 
	 * @param map        the map whose height should be generated
	 * @param nbDropplet the number of droplet
	 * @param nbParticle the number of particle by dropplet
	 */
	public HexParticleDepositionGeneration(HexMap map, int nbDropplet, int nbParticle) {
		this.particleCount = nbParticle;
		this.nbDropplet = nbDropplet;
		this.sizeX = map.WIDTH;
		this.sizeY = map.HEIGHT;
	}

	public HexParticleDepositionGeneration(HexMap map) {
		this(map, Math.max(1, map.WIDTH / 30), map.WIDTH * map.HEIGHT);
	}

	public float[][] generate(float[][] heights) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());
		LOG.info("       Nb Particule : " + this.particleCount);
		Direction[] directions = Direction.values();

		float[][] copy = new float[sizeY][sizeX];

		int cx, cy;

		for (int d = 0; d < nbDropplet; d++) {
			cx = Alea.nextInt(sizeX);
			cy = Alea.nextInt(sizeY);
			HexCoordinates position = new HexCoordinates(cx, cy);
			for (int i = 0; i < particleCount; i++) {
				int dirindex = Alea.nextInt(directions.length + 1);
				if (dirindex != 0) {
					position = position.direction(directions[dirindex - 1]);
				}
				if (position.column < 0) {
					position = new HexCoordinates(position.column + sizeX, position.row);
				}
				if (position.column >= sizeX) {
					position = new HexCoordinates(position.column - sizeX, position.row);
				}
				if (position.row < 0) {
					position = new HexCoordinates(position.column, position.row + sizeY);
				}
				if (position.row >= sizeY) {
					position = new HexCoordinates(position.column, position.row - sizeY);
				}

				copy[position.row][position.column] = copy[position.row][position.column] + 0.1f;
			}
		}

		this.normalize(copy);
		for (int y = 0; y < heights.length; y++) {
			for (int x = 0; x < heights[0].length; x++) {
				heights[y][x] += copy[y][x];
			}
		}

		return copy;
	}

}
