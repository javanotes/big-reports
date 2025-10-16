package org.bigcompany.reports.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract generic data access pattern over a {@code ITable}. This is a foundational
 * data read operation layer, to be used for building dimensional data structures on
 * the file data.
 */
public abstract class TableDao {

	protected final ITable table;
	
	protected TableDao(ITable rows) {
		this.table = rows;
	}
	protected abstract void open();
	
	@SuppressWarnings("unchecked")
	public <T> Optional<T> get(Map<String, String> row, FieldTyp field) {
		if(row != null && row.containsKey(field.key)) {
			var v = row.get(field.key);
			if(v != null) {
				switch (field.type) {
				case Num : return (Optional<T>) Optional.of(Double.valueOf(v));
				default:
					return (Optional<T>) Optional.of(v);
				}
			}
		}
		return Optional.empty();
	}
	public Map<String, List<Map<String, String>>> getGroupedData(String key, String _default) {
		Map<String, List<Map<String, String>>> grouped = new HashMap<>();
		table.getLoadedData().stream().forEach(_map -> {
			grouped.computeIfAbsent(_map.getOrDefault(key, _default), r -> new ArrayList<>()).add(_map);
		});
		
		return grouped;
	}
	public Map<String, Map<String, String>> getKeyedData(String key) {
		Map<String, Map<String, String>> keyed = new HashMap<>();
		table.getLoadedData().stream().forEach(_map -> {
			keyed.putIfAbsent(_map.get(key), _map);
		});
		
		return keyed;
	}
	
	
}
