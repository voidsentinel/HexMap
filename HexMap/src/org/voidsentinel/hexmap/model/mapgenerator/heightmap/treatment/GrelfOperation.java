/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.treatment;

import org.voidsentinel.hexmap.model.mapgenerator.heightmap.HeightMapExecutor;
import org.voidsentinel.hexmap.utils.Alea;

/**
 * This Generator create a map using the algorythm desribed by Grelf (hence the
 * name) URL https://www.grelf.net/forestdesign.html#terrain The method take a
 * value in a one-dimensional array, the index depending on x & y using an
 * affine tranformation (ax+by). This is done 5 time with different a & b, the
 * value beeing added. note that the The map is then normalized to 0-1
 * 
 * @author voidSentinel
 *
 */
public class GrelfOperation extends HeightMapExecutor {

	private float[]	prof	= new float[] { 77, 80, 84, 88, 92, 96, 101, 104, 108, 112, 115, 118, 120, 123, 126, 129,
			131, 133, 134, 134, 133, 133, 131, 130, 129, 126, 123, 122, 122, 122, 123, 125, 126, 130, 134, 137, 137, 138,
			138, 137, 135, 133, 129, 123, 118, 111, 105, 101, 97, 93, 90, 86, 82, 78, 74, 71, 69, 67, 67, 67, 66, 67, 69,
			71, 73, 74, 73, 73, 71, 69, 66, 62, 58, 54, 52, 52, 54, 55, 58, 59, 62, 63, 63, 65, 65, 65, 66, 66, 67, 69, 70,
			73, 77, 80, 82, 85, 88, 90, 93, 95, 96, 96, 96, 96, 93, 92, 90, 85, 80, 75, 71, 67, 63, 60, 58, 55, 52, 50, 47,
			44, 43, 41, 40, 39, 36, 35, 33, 32, 30, 28, 24, 20, 15, 11, 7, 3, 2, 2, 2, 2, 2, 2, 3, 6, 7, 10, 11, 15, 18,
			22, 24, 25, 25, 26, 26, 25, 25, 25, 25, 25, 26, 28, 29, 30, 33, 36, 37, 39, 39, 40, 40, 40, 39, 39, 39, 37, 37,
			37, 36, 36, 36, 35, 35, 33, 33, 32, 30, 28, 25, 20, 15, 11, 10, 9, 9, 9, 9, 11, 14, 15, 17, 17, 18, 18, 18, 18,
			18, 18, 17, 17, 17, 15, 14, 13, 11, 11, 10, 10, 10, 11, 13, 14, 17, 20, 22, 25, 28, 30, 35, 39, 41, 45, 50, 58,
			63, 69, 73, 77, 80, 82, 84, 84, 85, 85, 84, 84, 82, 81, 80, 75, 73, 71, 71, 73, 74, 75 };

	private float[]	ah		= new float[] { 0, 13, 21, 22, 29 };
	private float[]	bh		= new float[] { 27, 26, 21, 11, 1 };

	/**
	 * constructor
	 * 
	 * @param count
	 *           the number of fault-line to use.
	 */
	public GrelfOperation() {
	}

	/**
	 * 
	 */
	public void performOperation() {
		execution = 0f;

		float[][] copy = new float[ySize][xSize];
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				copy[j][i] = calcHeight(i * 5, j*5);
			}
		}

		this.normalize(copy);
		heightMap = copy;
		execution = 1f;
		LOG.info("   Operation finished internally");
	}

	private float calcHeight(int x, int y) {
		float ht = 0.0f;
		for (int i = 0; i < 5; i++) {
			double j = (ah[i] * x + bh[i] * y) / 128.0f;
			double jint = Math.floor(j);
			double jfrac = j - jint;
			double prof0 = prof[((int) (jint)) % 256];
			double prof1 = prof[((int) (jint + 1)) % 256];
			ht += prof0 + jfrac * (prof1 - prof0); // interpolate
		}

		for (int i = 0; i < 5; i++) {
			double j = (bh[i] * x + ah[i] * y) / 128.0f;
			double jint = Math.floor(j);
			double jfrac = j - jint;
			double prof0 = prof[((int) (jint)) % 256];
			double prof1 = prof[((int) (jint + 1)) % 256];
			ht += (prof0 + jfrac * (prof1 - prof0)) / 2f; // interpolate
		}

		return ht;
	};

}
