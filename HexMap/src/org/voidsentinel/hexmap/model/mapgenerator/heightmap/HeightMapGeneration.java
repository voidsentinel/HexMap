/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author guipatry
 *
 */
public abstract class HeightMapGeneration extends HeightMapExecutor {

	protected float coefficient = 1f;

	public HeightMapGeneration(String id) {
		super(id);
	}

	public abstract float[][] generate(int xSize, int ySize);

	/**
	 * @return the coefficient
	 */
	final public float getCoefficient() {
		return coefficient;
	}

	/**
	 * @param coefficient the coefficient to set
	 */
	final public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}

	/**
	 * set a parameter from a name/value couple
	 */
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		if ("coefficient".equalsIgnoreCase(name)) {
			float coeff = Float.parseFloat(value);
			this.setCoefficient(coeff);
			used = true;
		}
		if (used == false) {
			used = super.addDataParameters(name, value, additional);
		}
		return used;
	}

}
