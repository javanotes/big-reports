package org.bigcompany.reports.file;

import java.util.List;
import java.util.Map;
/**
 * Basic operations for an in memory tabular data, loaded from a file
 */
public interface ITable {

	/**
	 * Number of columns
	 * @param columns
	 */
	void setColumns(int columns);

	/**
	 * Validate and load structure
	 * @param fieldTyps
	 * @return
	 */
	boolean load(FieldTyp... fieldTyps);
	/**
	 * Get the complete loaded data
	 * @return
	 */
	List<Map<String, String>> getLoadedData();
	/**
	 * 
	 */
	void clear();
	
	/**
	 * default factory method to get implementation classes. This can be the strategy class
	 */
	static ITable ofDelimSeparated(String filePath) {
		return new CsvTable(filePath);
	}

}