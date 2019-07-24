package org.voidsentinel.hexmap.model.repositories;

import java.util.HashMap;
import java.util.Map;

public class BaseRepository<T extends RepositoryData> {

	public Map<String, T> datas = new HashMap<String, T>();

	/**
	 * get the data data for the given id;
	 * 
	 * @param id
	 * @return
	 */
	public T getData(String id) {
		return datas.get(id.toLowerCase());
	}

	/**
	 * add a terrainData to the repository
	 * 
	 * @param data  the data to add
	 * @param clear if true, any data with the same Id will be removed before adding
	 *              the data. if false, parameters of the new terraindata will be
	 *              added to the parameters of th eold.
	 * @exception IllegalArgumentException if data is null
	 */
	public void addData(T data) throws IllegalArgumentException {
		if (data == null) {
			throw new IllegalArgumentException();
		}
		T source = datas.get(data.id.toLowerCase());
		if (source == null) {
			source = data;
			datas.put(data.id.toLowerCase(), data);
		} else {
			// source is a ref to a data already in HashMap,
			// so no need to add it again : just modify the datas
			source.addData(data);
		}
	}

}