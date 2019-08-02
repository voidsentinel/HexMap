/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * @author guipatry
 *
 */
public class TorusOperation extends AbstractTerrainOperation {

	public enum HTreatment {
		LEFT, RIGHT, BOTH, NONE
	};

	public enum VTreatment {
		TOP, BOTTOM, BOTH, NONE
	};

	private HTreatment	horizontal	= HTreatment.BOTH;
	private VTreatment	vertical		= VTreatment.BOTH;

	public TorusOperation(HTreatment horizontal, VTreatment vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	public void filter(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		int xmax = height[0].length - 1;
		int ymax = height.length - 1;

		int sizex = xmax / 2;
		int sizey = height.length / 2;

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x <= sizex; x++) {
				float coeff = ((float) (x) / (float) (sizex)) / 2.0f + 0.55f;
				if (horizontal == HTreatment.LEFT || horizontal == HTreatment.BOTH) {
					height[y][x] = height[y][x] * coeff + height[y][xmax - x] * (1.0f - coeff);
				}
				if (horizontal == HTreatment.RIGHT || horizontal == HTreatment.BOTH) {
					height[y][xmax - x] = height[y][x] * (1.0f - coeff) + height[y][xmax - x] * coeff;
				}
			}
		}

		for (int x = 0; x <= xmax; x++) {
			for (int y = 0; y <= sizey; y++) {
				float coeff = ((float) (y) / (float) (sizey)) / 2.0f + 0.50f;
				if (vertical == VTreatment.TOP || vertical == VTreatment.BOTH) {
					height[y][x] = height[y][x] * coeff + height[ymax - y][x] * (1.0f - coeff);
				}
				if (vertical == VTreatment.BOTTOM || vertical == VTreatment.BOTH) {
					height[ymax - y][x] = height[y][x] * (1.0f - coeff) + height[ymax - y][x] * coeff;
				}
			}
		}

	}
}
