package demo;

public class Location {

	private double coordinate_X, coordinate_Y;

	public Location(double coordinate_X, double coordinate_Y) {

		this.coordinate_X = coordinate_X;
		this.coordinate_Y = coordinate_Y;
	}

	public double getCoordinate_X() {
		return coordinate_X;
	}

	public void setCoordinate_X(double coordinate_X) {
		this.coordinate_X = coordinate_X;
	}

	public double getCoordinate_Y() {
		return coordinate_Y;
	}

	public void setCoordinate_Y(double coordinate_Y) {
		this.coordinate_Y = coordinate_Y;
	}
}
