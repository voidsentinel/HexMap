/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This operation fill the table with values from a diamond square algorythm.
 * The map is upscaled to a square table of size equal to the nearest power of 2
 * , the values are generated and then clipped to the original table size.
 * 
 * @author VoidSentinel
 *
 */
public class PNGGeneration extends AbstractTerrainGenerator {

	private BufferedImage inputImage = null;

	public PNGGeneration(String filename) {
		LOG.info("   Operation : " + PNGGeneration.class.getSimpleName());
		LOG.info(filename);
		File inputFile = new File(filename);
		try {
			inputImage = ImageIO.read(inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + PNGGeneration.class.getSimpleName());
		int val = Math.max(xSize, ySize);
		LOG.info("      max initial size  : " + val);
		int power = 32 - Integer.numberOfLeadingZeros(val - 1);
		LOG.info("      max power  : " + power);
		int size = (int) (Math.pow(2, power) + 1);
		LOG.info("      max final size : " + size);
		float[][] copy = new float[size][size];

		// creates output image
		BufferedImage outputImage = new BufferedImage(xSize, ySize, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, xSize, ySize, null);
		g2d.dispose();

		File outputfile = new File("ground.jpg");
		try {
			ImageIO.write(outputImage, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		copy = readHeight(outputImage);




		
//		this.normalize(copy);
		return copy;
	}

	private static float[][] readHeight(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;
		
		float[][] result = new float[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {

				float red = (float) (((int) pixels[pixel + 3] & 0xff) << 16);
				float green = (float) (((int) pixels[pixel + 2] & 0xff) << 8);
				float blue = (float) (((int) pixels[pixel + 1] & 0xff));

				result[row][col] = red * 0.2126f + green * 0.7152f + blue * 0.0722f;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
				LOG.info(col + "/" + row + ":" + red + "." + green + "." + blue);

//				int argb = 0;
//				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
//				argb += ((int) pixels[pixel + 1] & 0xff); // blue
//				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
//				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
				float red = (float) (((int) pixels[pixel + 2] & 0xff) << 16);
				float green = (float) (((int) pixels[pixel + 1] & 0xff) << 8);
				float blue = (float) (((int) pixels[pixel + 0] & 0xff));

				result[row][col] = red * 0.2126f + green * 0.7152f + blue * 0.0722f;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return result;
	}

}