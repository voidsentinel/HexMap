/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.repositories.RepositoryData;

import com.jme3.math.ColorRGBA;

/**
 * @author Xerces
 *
 */
public class TerrainBorderedColorExtractor extends AbstractCellColorExtractor {
	final static float HEIGHTCHANGECOEFF = 0.8f;


	public TerrainBorderedColorExtractor(String id) {
		super(id);
	}

	/**
	 * 
	 * @param cell
	 * @return
	 */
	public ColorRGBA getColor(HexCell cell, HexMap map) {
		return cell.getTerrain().getBaseColor(cell.random);
	}

	public  void addDataParameters (RepositoryData data) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.voidsentinel.hexagons.view.AbstractCellColorExtractor#getColors(org.
	 * voidsentinel.hexagons.model.BoardMap, org.voidsentinel.hexagons.model.Cell)
	 */
	@Override
	public DirectionColorPair[] getTopColors(HexMap map, HexCell cell) {
		DirectionColorPair[] colorArray = new DirectionColorPair[6];
		int index = 0;
		if (cell == null) {
			// UV = 0/0
			for (int i = 0; i < 6; i++) {
				colorArray[index++] = new DirectionColorPair(ColorRGBA.Gray.clone(), ColorRGBA.Gray.clone());
			}
		} else {
			TerrainData terrain = cell.getTerrain();
			ColorRGBA color = terrain.getBaseColor(cell.random);
			for (Direction dir : Direction.values()) {
				HexCell voisin2 = cell.getNeighbor(dir);
				if (voisin2 != null && voisin2.getElevation() != cell.getElevation()) {
					colorArray[index++] = new DirectionColorPair(color.clone(), color.mult(HEIGHTCHANGECOEFF));
				} else {
					colorArray[index++] = new DirectionColorPair(color.clone(), color.clone());
				}
			}
		}
		return colorArray;
	}

	public DirectionColorPair[] getSideColors(HexMap map, HexCell cell) {
		DirectionColorPair[] colorArray = new DirectionColorPair[6];
		int index = 0;
		if (cell == null) {
			for (int i = 0; i < 6; i++) {
				colorArray[index++] = new DirectionColorPair(ColorRGBA.Gray.clone(), ColorRGBA.Gray.clone());
			}
		} else {
//			TerrainData terrain = cell.getTerrain();
//			ColorRGBA color = terrain.getBaseColor(cell.getRandom());
//			for (Direction dir : Direction.values()) {
//				HexCell voisin2 = map.getCellInDirection(cell.getPosition(), dir);
//				if (voisin2 != null && voisin2.getHeight() != cell.getHeight()) {
//					colorArray[index++] = new DirectionColorPair(color.mult(HEIGHTCHANGECOEFF),
//					      color.mult(HEIGHTCHANGECOEFF * HEIGHTCHANGECOEFF));
//				} else {
//					colorArray[index++] = new DirectionColorPair(color.clone(), color.clone());
//				}
//			}
		}
		return colorArray;
	}

	@Override
	public void addDataParameters(String name, String value, String additional) {
		// TODO Auto-generated method stub
		
	}

}
