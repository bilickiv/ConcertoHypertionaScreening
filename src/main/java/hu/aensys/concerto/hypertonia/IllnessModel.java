package hu.aensys.concerto.hypertonia;

import java.util.Date;

public class IllnessModel {

	private Date measurementDate;
	private MeasurementModel value;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private String type;
	
	
	public Date getMeasurementDate() {
		return measurementDate;
	}
	public void setMeasurementDate(Date measurementDate) {
		this.measurementDate = measurementDate;
	}
	public MeasurementModel getValue() {
		return value;
	}
	public void setValue(MeasurementModel value) {
		this.value = value;
	}
}
