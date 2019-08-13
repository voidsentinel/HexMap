package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.view.MeshUtil;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer.Type;

/**
 * generate and store the representation of a chunk of the map. also store
 * vertices that allow for selection of an hex. The colors and texture of the
 * representation can be modified
 * 
 * @author guipatry
 *
 */
public class HexGridChunkFlat25Lighted extends HexGridChunkFlat25 {

	public HexGridChunkFlat25Lighted(HexMap map, int xstart, int zstart, int chunkSize,
			AbstractCellColorExtractor colorExtractor) {
		super(map, xstart, zstart, chunkSize, colorExtractor);
	}

	/**
	 * Should be called only if representation is non empty. Will extract the colors
	 * for each cell with the new extractor, and fill the color buffer of the mesh
	 * with the new values
	 * 
	 * @param colorExtractor
	 *           the new colorExtractor to use.
	 */
	public void regenerateColor(AbstractCellColorExtractor colorExtractor) {
		this.colorExtractor = colorExtractor;
		MeshUtil meshUtility = new MeshUtil();
		HexCell hexCell = null;
		for (int z = zStart; z <= zEnd; z++) {
			for (int x = xStart; x <= xEnd; x++) {
				hexCell = map.getCell(x, z);
				colorizeCellCenter(hexCell, meshUtility);
				colorizeCellSide(hexCell, meshUtility);
			}
		}
		((Geometry) (representation.getChild("ground"))).getMesh().setBuffer(Type.Color, 4, meshUtility.getColorArray());
	}


	/**
	 * generate the internal hexagon
	 * 
	 * @param cell
	 * @param MeshUtility
	 */
	protected void colorizeCellCenter(HexCell cell, MeshUtil MeshUtility) {
		ColorRGBA color = colorExtractor.getColor(cell, map);
		ColorRGBA color2 = color.mult(0.8f);
		MeshUtility.addColor(color);
		for (@SuppressWarnings("unused")
		Direction direction : Direction.values()) {
			HexCell neighbor = cell.getNeighbor(direction);
			HexCell neighborp = cell.getNeighbor(direction.previous());

			// internal point
			MeshUtility.addColor(color);

			// corner
			if (neighbor == null || neighborp == null) {
				MeshUtility.addColor(color2);
			} else if (neighbor.getElevation() != cell.getElevation() || neighborp.getElevation() != cell.getElevation()) {
				MeshUtility.addColor(color2);
			} else {
				MeshUtility.addColor(color);
			}

			// bridge
			if (neighbor == null || neighbor.getElevation() != cell.getElevation()) {
				MeshUtility.addColor(color2);
				MeshUtility.addColor(color2);
			} else {
				MeshUtility.addColor(color);
				MeshUtility.addColor(color);
			}
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
