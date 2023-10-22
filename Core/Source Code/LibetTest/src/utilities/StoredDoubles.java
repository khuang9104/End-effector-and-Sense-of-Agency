package utilities;

import java.text.DecimalFormat;

public class StoredDoubles {

	double[] doubles = null;
	private DecimalFormat df = new DecimalFormat("#.##");

	public StoredDoubles(int size) {
		doubles = new double[size];
	}

	public void setValue(double d, int index) {
		doubles[index] = d;
	}

	public String toString() {
		String s = "Mean = " + df.format(getMeanValue()) + "\t\t Values = ";
		for (int i = 0; i < doubles.length; i++)
			s += df.format(doubles[i]) + ", ";
		return s;
	}

	private double getMeanValue() {
		double total = 0;
		for (int i = 0; i < doubles.length; i++) {
			total += doubles[i];
		}
		return total / doubles.length;
	}
}
