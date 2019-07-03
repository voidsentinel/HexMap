/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.view.mapColor;

import org.voidsentinel.hexmap.model.repositories.BaseRepository;

/**
 * @author guipatry
 *
 */
public class colorMapperRepository extends BaseRepository<AbstractCellColorExtractor> {

	static public colorMapperRepository repository = new colorMapperRepository();

	public AbstractCellColorExtractor getDefaultMapper() {
		for (AbstractCellColorExtractor mapper : this.datas.values()) {
			if (mapper.defaultMapper == true) {
				return mapper;
			}
		}
		return (AbstractCellColorExtractor)this.datas.values().toArray()[0];
	}
}
