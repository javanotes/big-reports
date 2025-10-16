package org.bigcompany.reports.file;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A data wrapper over a delimiter separated file data, held in memory. This implementation mandatorily
 * expects a header row. The in memory data structure used will be List of Maps - where each record is represented as a key value entity. The headers being
 * the key in the entries. Note, the structural integrity of the file with respect to the headers has to be maintained.
 * The data can be refreshed. Not thread safe!
 */
final class CsvTable implements ITable {
	
	private static Logger log = Logger.getLogger("CsvLoader");

	private int header = 0;
	private String delimiter = ",";
	private int columns=0;
	private String[] headers;
	private List<Map<String, String>> loadedData;
	private final String filePath;
	
	public CsvTable(int columns, String filePath) {
		this.columns = columns;
		this.filePath = filePath;
	}
	public CsvTable(String filePath) {
		this.filePath = filePath;
	}
	
	// in an advanced version we can have field type metadata
	
	@Override
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int getHeader() {
		return header;
	}

	public void setHeader(int header) {
		this.header = header;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public int getColumns() {
		return columns;
	}

	public String[] getHeaders() {
		return Arrays.copyOf(headers, headers.length) ;
	}

	public String getFilePath() {
		return filePath;
	}
	private Path file;
	private List<String> lines;
	/**
	 * Validate and load structure
	 * @param fieldTyps
	 * @return
	 */
	@Override
	public boolean load(FieldTyp ...fieldTyps) {
		try {
			file = Path.of(Thread.currentThread().getContextClassLoader().getResource(filePath).toURI());
		} catch (URISyntaxException e) {
			throw new DataException("Cannot load file - ".concat(filePath), e);
		}
		// skip blank lines
		// assuming there is no line gap between header and data lines		
		try {
			lines = Files.readAllLines(file).stream().filter(s -> s != null && s.trim().length() > 0).collect(Collectors.toList());
		} catch (IOException e) {
			throw new DataException("Cannot read file - ".concat(filePath), e);
		}
		Map<String, String> row;
		for (int i = 0; i < lines.size(); i++) {
			if(i == header) {
				// some basic metadata validation
				String [] splits = lines.get(i).split(delimiter);
				validate(splits, fieldTyps);
				continue;
			}
			
			String [] splits = lines.get(i).split(delimiter, columns);
			if(splits.length != columns) {
				log.warning(String.format("Unexpected fields not matching header count at line %d. expecting %d, found %d. Skip ..", i, columns, splits.length));
				continue;
			}
			row = new HashMap<>();
			for (int j = 0; j < splits.length; j++) {
				// do not add blank values
				if(splits[j].length() > 0) {
					row.put(headers[j], splits[j]);	
				}
							
			}
			getLoadedData().add(row);
		}
		return true;
	}
	private void validate(String [] splits, FieldTyp ...fieldTyps) {
		// header count check
		if(splits.length == 0 || splits.length != columns) {
			throw new DataException("Metadata mismatch! Unexpected header count - " + (splits.length));
		}
		headers = new String[columns];
		for (int j = 0; j < splits.length; j++) {
			headers[j] = splits[j];
		}
		
		// mandatory header check
		if(fieldTyps.length > 0) {
			var set = Set.of(headers);
			for (int j = 0; j < fieldTyps.length; j++) {
				if(!set.contains(fieldTyps[j].key)) {
					throw new DataException("Metadata mismatch! Mandatory header missing - " + fieldTyps[j].key);
				}
			}
		}
		loadedData = new ArrayList<>();
	}
	
	@Override
	public void clear() {
		if(getLoadedData() != null) {
			getLoadedData().clear();
			loadedData = null;
		}
	}
	public static void main(String[] args) throws IOException, URISyntaxException {
		ITable csv = new CsvTable(5, "input.txt");
		csv.load();
	}

	@Override
	public List<Map<String, String>> getLoadedData() {
		return loadedData;
	}
}
