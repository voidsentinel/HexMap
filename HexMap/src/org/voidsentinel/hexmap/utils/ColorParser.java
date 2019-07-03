/**
 * 
 */
package org.voidsentinel.hexmap.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jme3.math.ColorRGBA;

/**
 * @author Guillaume
 * 
 */
public class ColorParser {

	private static final Pattern	rgba	= Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
	private static final Pattern	rgb	= Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
	private static final Pattern	hexa	= Pattern
	      .compile("#([A-Fa-f0-9]{2})([A-Fa-f0-9]{2})([A-Fa-f0-9]{2})([A-Fa-f0-9]{2})");
	private static final Pattern	hex	= Pattern.compile("#([A-Fa-f0-9]{2})([A-Fa-f0-9]{2})([A-Fa-f0-9]{2})");

	/**
	 * private constructor forbiding instanciation
	 */
	private ColorParser() {
	}

	/**
	 * parse a string to return a RGBAColor. The following format are suppported :
	 * <ul>
	 * <li>rgba(<i>r</i>,<i>g</i>,<i>b</i>,<i>a</i>) where r,g,b and a are int from
	 * 0-255</li>
	 * <li>rgb(<i>r</i>,<i>g</i>,<i>b</i>) where r,g,b are int from 0-255. a is
	 * supposed to be 255 (no alpha)</li>
	 * <li>#<i>aarrggbb</i> where r,g,b and a are hex from 00 to FF. please note
	 * that 2 char are mandatory for each color component</li>
	 * <li>#<i>rrggbb</i> where r,g,b are hex from 00 to FF. please note that 2 char
	 * are mandatory for each color component. alpha is supposed to be FF (no
	 * alpha)</li>
	 * 
	 * @param input the string to parse
	 * @return the corresponding color
	 */
	public static ColorRGBA parse(String input) {
		Matcher mrgba = rgba.matcher(input);
		if (mrgba.matches()) {
			return new ColorRGBA(intToFloatValue(mrgba.group(1)), // r
			      intToFloatValue(mrgba.group(2)), // g
			      intToFloatValue(mrgba.group(3)), // b
			      intToFloatValue(mrgba.group(4))); // a
		}

		Matcher mrgb = rgb.matcher(input);
		if (mrgb.matches()) {
			return new ColorRGBA(intToFloatValue(mrgb.group(1)), // r
			      intToFloatValue(mrgb.group(2)), // g
			      intToFloatValue(mrgb.group(3)), // b
			      1.0f); // a
		}

		Matcher mhexa = hexa.matcher(input);
		if (mhexa.matches()) {
			return new ColorRGBA(hexToFloatValue(mhexa.group(2)), // r
			      hexToFloatValue(mhexa.group(3)), // g
			      hexToFloatValue(mhexa.group(4)), // b
			      hexToFloatValue(mhexa.group(1))); // a
		}

		Matcher mhex = hex.matcher(input);
		if (mhex.matches()) {
			return new ColorRGBA(hexToFloatValue(mhex.group(1)), // r
			      hexToFloatValue(mhex.group(2)), // g
			      hexToFloatValue(mhex.group(3)), // b
			      1.0f); // a
		}
		System.out.println("Error parsing color " + input);
		return null;
	}

	public static ColorRGBA mean(ColorRGBA a, ColorRGBA b, ColorRGBA c) {
		ColorRGBA color = new ColorRGBA(0f, 0f, 0f, 0f);
		int count = 0;

		if (a != null) {
			color.r += a.r;
			color.g += a.g;
			color.b += a.b;
			color.a += a.a;
			count++;
		}

		if (b != null) {
			color.r += b.r;
			color.g += b.g;
			color.b += b.b;
			color.a += b.a;
			count++;
		}

		if (c != null) {
			color.r += c.r;
			color.g += c.g;
			color.b += c.b;
			color.a += c.a;
			count++;
		}
		if (count > 0) {
			color.r = color.r / count;
			color.g = color.g / count;
			color.b = color.b / count;
			color.a = color.a / count;
		}
		return color;
	}

	private static float intToFloatValue(String val) {
		int intVal = Integer.valueOf(val);
		float floatval = (1.0f * intVal) / 255f;
		return floatval;
	}

	private static float hexToFloatValue(String val) {
		int intVal = Integer.valueOf(val, 16);
		float floatval = (1.0f * intVal) / 255f;
		return floatval;
	}
}
