package org.bigcompany.reports.file;

/**
 * A basic metadata for field types. Can be enhanced with advanced controls
 */
public class FieldTyp {
	
	public enum DataTyp{
		Text,Num
	}
	
	public final DataTyp type;
	public final String key;
	
	public FieldTyp(String key, DataTyp type) {
		this.type = type;
		this.key = key;
	}

}
