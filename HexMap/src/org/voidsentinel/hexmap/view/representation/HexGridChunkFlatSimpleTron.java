package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.view.MeshUtil;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.math.ColorRGBA;

/**
 * generate and store the representation of a chunk of the map. also store
 * vertices that allow for selection of an hex. The colors and texture of the
 * representation can be modified
 * 
 * @author guipatry
 *
 */
public class HexGridChunkFlatSimpleTron extends HexGridChunkFlat25 {

	public HexGridChunkFlatSimpleTron(HexMap map, int xstart, int zstart, int chunkSize, boolean perturbated,
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
		ColorRGBA color = colorExtractor.getColor(cell, map);
		MeshUtility.addColor(ColorRGBA.Black);
		for (@SuppressWarnings("unused")
		Direction direction : Direction.values()) {
			HexCell neighbor = cell.getNeighbor(direction);
			HexCell neighborp = cell.getNeighbor(direction.previous());

			// internal point
			MeshUtility.addColor(ColorRGBA.Black);

			// corner
			if (neighbor == null || neighborp == null) {
				MeshUtility.addColor(color);
			} else if (neighbor.getElevation() != cell.getElevation() || neighborp.getElevation() != cell.getElevation()) {
				MeshUtility.addColor(color);
			} else {
				MeshUtility.addColor(ColorRGBA.Black);
			}

			// bridge
			if (neighbor == null || neighbor.getElevation() != cell.getElevation()) {
				MeshUtility.addColor(color);
				MeshUtility.addColor(color);
			} else {
				MeshUtility.addColor(ColorRGBA.DarkGray);
				MeshUtility.addColor(ColorRGBA.DarkGray);
			}
		}
	}

}
