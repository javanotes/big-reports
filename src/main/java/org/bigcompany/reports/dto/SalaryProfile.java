package org.bigcompany.reports.dto;

public class SalaryProfile {

	private String id;
	private String name;
	private double salary;
	private double deviation;
	private String range = null;
	public String getManagerId() {
		return id;
	}
	public void setManagerId(String managerId) {
		this.id = managerId;
	}
	public String getManagerName() {
		return name;
	}
	public void setManagerName(String managerName) {
		this.name = managerName;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public double getDeviation() {
		return deviation;
	}
	public void setDeviation(double deviation) {
		this.deviation = deviation;
	}
	@Override
	public String toString() {
		return String.format(
			"{\n  \"id\": \"%s\",\n  \"name\": \"%s\",\n  \"salary\": %.2f,\n  \"deviation\": %.2f,\n  \"range\": \"%s\"\n}",
			id == null ? "" : id,
			name == null ? "" : name,
			salary,
			deviation,
			range == null ? "" : range
		);
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}

	

}