/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.view.mapColor;

import org.voidsentinel.hexmap.repositories.BaseRepository;

/**
 * a repository for ColorMapper
 * 
 * @author void Sentinel
 *
 */
public class ColorMapperRepository extends BaseRepository<AbstractCellColorExtractor> {

	static public ColorMapperRepository repository = new ColorMapperRepository();

	public AbstractCellColorExtractor getDefaultMapper() {
		for (AbstractCellColorExtractor mapper : this.datas.values()) {
			if (mapper.defaultMapper == true) {
				return mapper;
			}
		}
		return (AbstractCellColorExtractor) this.datas.values().toArray()[0];
	}
}
