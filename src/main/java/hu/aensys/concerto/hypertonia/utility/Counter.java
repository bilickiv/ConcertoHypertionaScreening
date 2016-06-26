package hu.aensys.concerto.hypertonia.utility;

public class Counter {
	private String name;
	private double value;
	public Counter(String id, double value) {
		super();
		this.name = id;
		this.value = value;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "Counter [id=" + name + ", value=" + value + "]";
	}
	
}
