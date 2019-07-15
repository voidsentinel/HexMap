/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.utils.Alea;

/**
 * This Generator create a map by adding 1 to a random point, moving in one
 * direction and doing it again a given number of time (nbParticle). This is
 * done several times (Nb Droplet). The table is normalized at the end to get
 * values between 0 and 1
 * 
 * @author guipatry
 *
 */
public class HexParticleDepositionGeneration extends AbstractTerrainGenerator {

	private int	nbDropplet		= 1;
	private int	particleCount	= 1;

	/**
	 * 
	 * @param nbDropplet
	 *           the number of droplet
	 * @param nbParticle
	 *           the number of particle for each dropplet
	 */
	public HexParticleDepositionGeneration(int nbDropplet, int nbParticle) {
		this.particleCount = nbParticle;
		this.nbDropplet = nbDropplet;
	}

	/**
	 * generate a heightfield
	 * 
	 * @param xSize
	 *           the xsize of the map to create
	 * @param ySize
	 *           the ysize of the map to create
	 * @return a float table of size [ySize][xSize]
	 */
	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());
		LOG.info("       Nb Particule : " + this.particleCount);
		Direction[] directions = Direction.values();

		float[][] copy = new float[ySize][xSize];

		int cx, cy;

		for (int d = 0; d < nbDropplet; d++) {
			cx = Alea.nextInt(xSize);
			cy = Alea.nextInt(ySize);
			HexCoordinates position = new HexCoordinates(cx, cy);
			for (int i = 0; i < particleCount; i++) {
				int dirindex = Alea.nextInt(directions.length + 1);
				if (dirindex != 0) {
					position = position.direction(directions[dirindex - 1]);
				}
				if (position.column < 0) {
					position = new HexCoordinates(position.column + xSize, position.row);
				}
				if (position.column >= xSize) {
					position = new HexCoordinates(position.column - xSize, position.row);
				}
				if (position.row < 0) {
					position = new HexCoordinates(position.column, position.row + ySize);
				}
				if (position.row >= ySize) {
					position = new HexCoordinates(position.column, position.row - ySize);
				}

				copy[position.row][position.column]++;
			}
		}

		this.normalize(copy);
		return copy;
	}

}
