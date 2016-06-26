package hu.aensys.concerto.hypertonia;

import java.util.Date;

public class MeasurementModel implements Comparable<MeasurementModel>{

	private Date measurementDate;
	private Integer value;
	private String type;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public Date getMeasurementDate() {
		return measurementDate;
	}
	public void setMeasurementDate(Date measurementDate) {
		this.measurementDate = measurementDate;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public int compareTo(MeasurementModel other) {
        return measurementDate.compareTo(other.measurementDate);
    }
}
