import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

//this is a class for our methods we use to read CSVs and create text files
public class File_methods {
    
    //read a CSV into a points array
    public static ArrayList<Point> read_points_from_csv(String file_path) throws FileNotFoundException{
        Scanner s = new Scanner(new File(file_path));
        s.useDelimiter(",|\\n");
        ArrayList<Point> points = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            String waste = s.next();
        }
        while (s.hasNext()) {
            String date = s.next();
            String id = s.next();
            String x_var = s.next();
            String y_var = s.next();
            points.add(new Point(date, id, Double.parseDouble(x_var), Double.parseDouble(y_var)));
        }

        s.close();
        return points;
    }
    //write out to a file given an array of points
    public static void createPointsFile(String file, ArrayList<Point> arrData)
            throws IOException {
        FileWriter writer = new FileWriter(file + ".txt");
        int size = arrData.size();
        for (int i=0;i<size;i++) {
            String str = arrData.get(i).x + ", " + arrData.get(i).y;
            writer.write(str);
            if(i < size-1) //This prevent creating a blank like at the end of the file**
                writer.write("\n");
        }
        writer.close();
    }
    //write out to a file given an array of edges
    public static void createEdgeFile(String file, ArrayList<Edge> arrData) throws IOException {
        FileWriter writer = new FileWriter(file + ".txt");
        int size = arrData.size();
        for (int i=0;i<size;i++) {
            String str = String.valueOf(arrData.get(i).length());
            writer.write(str);
            if(i < size-1) //This prevent creating a blank like at the end of the file**
                writer.write("\n");
        }
        writer.close();
    }

}
