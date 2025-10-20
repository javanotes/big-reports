package org.bigcompany.reports.file;

/**
 * A basic metadata for field types. Can be enhanced with advanced controls for governance and compliance, for example.
 */
public class FieldTyp {

	public boolean isEnableTypeCheck() {
		return enableTypeCheck;
	}

	public void setEnableTypeCheck(boolean enableTypeCheck) {
		this.enableTypeCheck = enableTypeCheck;
	}

	public enum DataTyp{
		Text,Num
	}
	
	public final DataTyp type;
	public final String key;
	// other data control features
	private boolean enableTypeCheck;
	public FieldTyp(String key, DataTyp type) {
		this.type = type;
		this.key = key;
	}

}
