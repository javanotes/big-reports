package org.bigcompany.reports.file;

import java.util.List;
import java.util.Map;
/**
 * Basic operations for an in memory tabular data, loaded from a file.
 * Each row will be represented as a key value entity. It is important to note that since we are
 * modelling our data on key values, the sequence of the physical data (as in a delimiter separated file)
 * becomes inconsequential.
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
	 * default factory method to get implementation classes.
	 * The discovery strategy can be enhanced in a configuration driven manner.
	 */
	static ITable newInstance(String filePath) {
		return new CsvTable(filePath);
	}

}