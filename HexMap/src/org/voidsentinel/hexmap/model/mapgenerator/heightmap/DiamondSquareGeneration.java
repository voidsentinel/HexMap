/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import org.voidsentinel.hexmap.utils.Alea;

/**
 * This operation fill the table with values from a diamond square algorythm.
 * The map is upscaled to a square table of the nearest power of 2 size, the
 * values are generated and then clipped to the original table. The value of the
 * original table if multiplied by the new values.
 * 
 * @author VoidSentinel
 *
 */
public class DiamondSquareGeneration extends AbstractTerrainGenerator {


	public DiamondSquareGeneration() {
	}

	public float[][] generate(float[][] data) {
		LOG.info("   Operation : " + DiamondSquareGeneration.class.getSimpleName());
		int width = data[0].length;
		int height = data.length;
		int val = Math.max(width, height);
		LOG.info("      max initial size  : " + val);
		int power = 32 - Integer.numberOfLeadingZeros(val - 1);
		LOG.info("      max power  : " + power);
		int size = (int) (Math.pow(2, power) + 1);
		LOG.info("      max final size : " + size);
		float[][] copy = new float[size][size];
		copy = plasma(copy);
		this.normalize(copy);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				data[y][x] = data[y][x] + copy[y][x];
			}
		}
		this.normalize(data);
		return copy;
	}

	private float[][] plasma(float[][] data) {
		if (data[0].length != data.length) {
			LOG.severe("      Sizes are different in z and x");
			return null;
		}

		int width = data[0].length;
		int height = data.length;
		int DATA_SIZE = width;

		final float SEED = 1000f;
		// seed the data
		data[0][0] = data[0][DATA_SIZE - 1] = data[DATA_SIZE - 1][0] = data[DATA_SIZE - 1][DATA_SIZE - 1] = SEED;

		float valmin = Float.MAX_VALUE;
		float valmax = Float.MIN_VALUE;

		float h = 500.0f;// the range (-h -> +h) for the average offset

//		Random r = new Random(mseed);// for the new value in range of h
		// side length is distance of a single square side
		// or distance of diagonal in diamond
		for (int sideLength = DATA_SIZE - 1;
		      // side length must be >= 2 so we always have
		      // a new value (if its 1 we overwrite existing values
		      // on the last iteration)
		      sideLength >= 2;
		      // each iteration we are looking at smaller squares
		      // diamonds, and we decrease the variation of the offset
		      sideLength /= 2, h /= 2.0) {
			// half the length of the side of a square
			// or distance from diamond center to one corner
			// (just to make calcs below a little clearer)
			int halfSide = sideLength / 2;

			// generate the new square values
			for (int x = 0; x < DATA_SIZE - 1; x += sideLength) {
				for (int y = 0; y < DATA_SIZE - 1; y += sideLength) {
					// x, y is upper left corner of square
					// calculate average of existing corners
					float avg = data[x][y] + // top left
					      data[x + sideLength][y] + // top right
					      data[x][y + sideLength] + // lower left
					      data[x + sideLength][y + sideLength];// lower right
					avg /= 4.0;

					// center is average plus random offset
					data[x + halfSide][y + halfSide] =
					      // We calculate random value in range of 2h
					      // and then subtract h so the end value is
					      // in the range (-h, +h)
					      avg + (Alea.nextFloat() * 2 * h) - h;

					valmax = Math.max(valmax, data[x + halfSide][y + halfSide]);
					valmin = Math.min(valmin, data[x + halfSide][y + halfSide]);
				}
			}

			// generate the diamond values
			// since the diamonds are staggered we only move x
			// by half side
			// NOTE: if the data shouldn't wrap then x < DATA_SIZE
			// to generate the far edge values
			for (int x = 0; x < DATA_SIZE - 1; x += halfSide) {
				// and y is x offset by half a side, but moved by
				// the full side length
				// NOTE: if the data shouldn't wrap then y < DATA_SIZE
				// to generate the far edge values
				for (int y = (x + halfSide) % sideLength; y < DATA_SIZE - 1; y += sideLength) {
					// x, y is center of diamond
					// note we must use mod and add DATA_SIZE for subtraction
					// so that we can wrap around the array to find the corners
					float avg = data[(x - halfSide + DATA_SIZE - 1) % (DATA_SIZE - 1)][y] + // left of center
					      data[(x + halfSide) % (DATA_SIZE - 1)][y] + // right of center
					      data[x][(y + halfSide) % (DATA_SIZE - 1)] + // below center
					      data[x][(y - halfSide + DATA_SIZE - 1) % (DATA_SIZE - 1)]; // above center
					avg /= 4.0;

					// new value = average plus random offset
					// We calculate random value in range of 2h
					// and then subtract h so the end value is
					// in the range (-h, +h)
					avg = avg + (Alea.nextFloat() * 2 * h) - h;
					// update value for center of diamond
					data[x][y] = avg;

					valmax = Math.max(valmax, avg);
					valmin = Math.min(valmin, avg);

					// wrap values on the edges, remove
					// this and adjust loop condition above
					// for non-wrapping values.
					if (x == 0)
						data[DATA_SIZE - 1][y] = avg;
					if (y == 0)
						data[x][DATA_SIZE - 1] = avg;
				}
			}
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				data[i][j] = (data[i][j] - valmin) / (valmax - valmin);
			}
		}
		return data;
	}

}