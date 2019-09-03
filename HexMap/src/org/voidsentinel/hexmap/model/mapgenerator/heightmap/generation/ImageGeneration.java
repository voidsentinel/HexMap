/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This operation fill the table with values coming from an image file (PNG,
 * JPEG...) The value is the gray value (red * 0.2126f + green * 0.7152f + blue
 * * 0.0722f)
 * 
 * @author VoidSentinel
 *
 */
public class ImageGeneration extends AbstractTerrainGenerator {

	private BufferedImage inputImage = null;

	public ImageGeneration(String filename) {
		LOG.info("   Operation : " + ImageGeneration.class.getSimpleName());
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
		LOG.info("   Operation : " + ImageGeneration.class.getSimpleName());
		float[][] copy;

		// creates output image
		BufferedImage outputImage = new BufferedImage(xSize, ySize, inputImage.getType());
		Graphics2D g = outputImage.createGraphics();
		g.setBackground(Color.BLACK);
		g.drawRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.drawImage(inputImage, 0, 0, outputImage.getWidth(), outputImage.getHeight(), 0, 0, inputImage.getWidth(),
		      inputImage.getHeight(), null);
		g.dispose();

		File outputfile = new File("ground.png");
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

	private float[][] readHeight(BufferedImage image) {
		LOG.info("      Final image height : " + image.getHeight());
		LOG.info("      Final image width : " + image.getWidth());

		final int width = image.getWidth();
		final int height = image.getHeight();
		float[][] result = new float[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int clr = image.getRGB(x, y);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				result[height - 1 - y][x] = red * 0.2126f + green * 0.7152f + blue * 0.0722f;
			}
		}

		return result;
	}

}