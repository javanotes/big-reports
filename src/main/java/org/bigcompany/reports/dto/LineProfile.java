package org.bigcompany.reports.dto;

public class LineProfile {
	private String id;
	private String name;
	private int line;
	public LineProfile(String id, String name, int line) {
		this.id = id;
		this.name = name;
		this.line = line;
	}
	public String getEmployeeId() {
		return id;
	}
	public void setEmployeeId(String employeeId) {
		this.id = employeeId;
	}
	public String getEmployeeName() {
		return name;
	}
	public void setEmployeeName(String employeeName) {
		this.name = employeeName;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return String.format(
			"{\n  \"id\": \"%s\",\n  \"name\": \"%s\",\n  \"line\": %d\n}",
			id == null ? "" : id,
			name == null ? "" : name,
			line
		);
	}
}