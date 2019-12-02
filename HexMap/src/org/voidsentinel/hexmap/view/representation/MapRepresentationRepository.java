/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.repositories.BaseRepository;

/**
 * @author guipatry
 *
 */
public class MapRepresentationRepository extends BaseRepository<MapRepresentation> {

	static public MapRepresentationRepository repository = new MapRepresentationRepository();

	public MapRepresentation getDefault() {
		for (MapRepresentation mapper : this.datas.values()) {
			if (mapper.defaultMapper == true) {
				return mapper;
			}
		}
		return (MapRepresentation)this.datas.values().toArray()[0];
	}
}
