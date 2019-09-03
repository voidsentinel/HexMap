package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.view.MeshUtil;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.math.ColorRGBA;

/**
 * a variation of the HexGridChunkFlatSimple mesh generation where color is not
 * changed based on direction. This should be used with a material where light
 * is used
 * 
 * @author void sentinel
 *
 */
public class HexGridChunkFlatSimpleLighted extends HexGridChunkFlatSimple {

	public HexGridChunkFlatSimpleLighted(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
			AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, perturbated, colorExtractor);
	}

	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	protected void colorizeCellCenter(HexCell cell, MeshUtil MeshUtility) {
		ColorRGBA color = colorExtractor.getColor(cell, map); //.clone().mult(0.9f);
		MeshUtility.addColor(color);
		for (@SuppressWarnings("unused")
		Direction direction : Direction.values()) {
			MeshUtility.addColor(color);
		}
	}

	protected void colorizeCellSide(HexCell cell, MeshUtil meshUtility) {
		ColorRGBA c1 = colorExtractor.getColor(cell, map).clone().mult(0.90f);
		for (Direction direction : Direction.values()) {
			colorizeCellSideDirection(cell, direction, meshUtility, c1);
		}
	}

	protected void colorizeCellSideDirection(HexCell cell, Direction direction, MeshUtil meshUtility, ColorRGBA c1) {
		HexCell neighbor = cell.getNeighbor(direction);
		int height = 0;
		if (neighbor != null && neighbor.getElevation() < cell.getElevation()) {
			height = cell.getElevation() - neighbor.getElevation();
		} else if (neighbor == null) {
			height = cell.getElevation() + 1;
		}

		if (height > 0) {
			meshUtility.addQuadColors(c1, c1, c1, c1);
		}
	}

}
