import java.util.ArrayList;

import static java.lang.Math.sqrt;

/**
 * City class that represents a city by a point and a name
 */
public class City {
	private String name;
    private ArrayList<Double> coordinates;

	public City(String name, ArrayList<Double> coordinatesTmp ) {
		coordinates = coordinatesTmp;
		this.name = name;
	}

	/**
	 * Gets the city's name
	 *
	 * @return The city's name
	 */
	public String getName() {
		return name;
	}

    public double getCoordinates(int i){
        return coordinates.get(i);
    }
    public double distance(City city) {
        int dist = 0;
        for(int i = 0; i < coordinates.size(); ++i){
            dist += (city.getCoordinates(i) - getCoordinates(i))*(city.getCoordinates(i) - getCoordinates(i)) ;
        }
        return sqrt(dist);
    }
}
