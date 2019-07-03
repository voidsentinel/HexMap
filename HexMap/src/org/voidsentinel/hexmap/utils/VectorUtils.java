/**
 * 
 */
package org.voidsentinel.hexmap.utils;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;

/**
 * @author Xerces
 *
 */
public class VectorUtils {

	public static Vector4f vector4fFromString(String source) {
		Vector4f response = new Vector4f();
		String[] values = source.split(",");
		response.x = Float.parseFloat(values[0]);
		response.y = Float.parseFloat(values[1]);
		response.z = Float.parseFloat(values[2]);
		response.w = Float.parseFloat(values[3]);
		return response;
	}

	public static Vector2f vector2fFromString(String source) {
		Vector2f response = new Vector2f();
		String[] values = source.split(",");
		response.x = Float.parseFloat(values[0]);
		response.y = Float.parseFloat(values[1]);
		return response;
	}

}
