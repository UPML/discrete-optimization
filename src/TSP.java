import java.io.*;
import java.util.ArrayList;

public class TSP {
    public static void main(String[] args) throws IOException {
        File file = new File("26out30.txt");
        for (Integer i = 2600; i < 2610; i+= 10) {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getName(), true)));
            out.println("i = " + i.toString());
            if(i == 1210){
                i = 1210;
            }
            tsp("/home/kagudkov/create_test/" + i.toString() + ".txt", out);
            out.close();
            System.out.println(i);
        }

    }

    private static void tsp(String args, PrintWriter out) {
        File file = new File(args);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            alert("Error loading file " + e);
            System.exit(1);
        }


        int numberOfVertices = 0;
        try {
            String line;
            while (!(line = reader.readLine()).equals("NODE_COORD_SECTION")) {
                String[] entry = line.split(":");
                String s = entry[0].trim();
                if (s.equals("DIMENSION")) {
                    numberOfVertices = Integer.parseInt(entry[1]);

                }
            }
        } catch (Exception e) {
            alert("Error parsing header " + e);
            System.exit(1);
        }
        ArrayList<City> cities = new ArrayList<>(numberOfVertices);

        try {
            String line;
            while (true) {
                line = reader.readLine();
                if (line.length() == 0) {
                    break;
                }
                String[] entry = line.split(" ");
                if (entry[0].equals("EOF")) {
                    break;
                }
                ArrayList<Double> coordinates = new ArrayList<>();
                for (int i = 1; i < entry.length; ++i) {
                    coordinates.add(Double.parseDouble(entry[i]));
                }
                cities.add(new City(entry[0], coordinates));
            }

            reader.close();
        } catch (Exception e) {
            if (cities.size() != numberOfVertices) {
                alert("Error parsing data " + e);
                System.exit(1);
            }
        }

        Timer timer = new Timer();
        Solver solver = new Solver(cities);
        timer.start();
        int[] path = solver.calculate();
        timer.stop();

        String message = cities.get(path[0]).getName();
        for (int i = 1; i < path.length; i++) {
            message += " to " + cities.get(path[i]).getName();
        }
        message += " to " + cities.get(path[0]).getName();
        message += "\nCost: " + solver.getCost();
        message += "\nTime: " + timer.getTime();

        out.println(message);
    }

    private static void alert(String message) {
        System.out.println(message);
    }

}
