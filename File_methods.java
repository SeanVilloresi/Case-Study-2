import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

//this is a class for our methods we use to read CSVs and create text files
public class File_methods {
    //read a textfile into a trajectory arraylist when points are present as well
    public static ArrayList<Trajectory> readTrajectoriesFromcsv(String filename) throws IOException {
        ArrayList<Trajectory> trajectories = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String id = line.trim(); 
            int index = id.indexOf(',', 0);
            id = id.substring(0, index);
            Trajectory trajectory = new Trajectory(id); 
            trajectories.add(trajectory);
        }
        reader.close();
        return trajectories;
    }

    //read a textfile into a Trajectory ArrayList
    public static ArrayList<Trajectory> readTrajectoriesFromFile(String filename) throws IOException {
        ArrayList<Trajectory> trajectories = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String id = line.trim(); 
            Trajectory trajectory = new Trajectory(id); 
            trajectories.add(trajectory);
        }
        reader.close();
        return trajectories;
    }
    
    //read a CSV into a points array
    public static ArrayList<Point> read_points_from_csv(String file_path) throws FileNotFoundException{
        Scanner s = new Scanner(new File(file_path));
        s.useDelimiter(",|\\n");
        ArrayList<Point> points = new ArrayList<>();
         for(int i = 0; i < 3; i++){
             String waste = s.next();
         }
        while (s.hasNext()) {
            //String date = s.next();
            String id = s.next();
            String x_var = s.next();
            String y_var = s.next();
            points.add(new Point("N/A", id, Double.parseDouble(x_var), Double.parseDouble(y_var)));
        }

        s.close();
        return points;
    }

    public static void computeAveragesFile(String file, String method,  ArrayList<Trajectory> set) throws IOException{
        FileWriter writer = new FileWriter(file + ".txt");

        for (int i=4;i<=12;i+=2){
            int sum=0;
            System.out.print(i);
            for (int j=0; j<3;j++){
                System.out.print("run" + j);
                sum+=task_five.lloyds(method, set, i).cost;
            }
            writer.write("For k = "+ i + ", average cost =" + sum/3 + "\n");
        }
        writer.close();

    }
    //write a Cluster Object into a file that has all the points that make up its center
    public static void createClusterCenterFile(String file, Clustering cluster) throws IOException{
        FileWriter writer = new FileWriter(file + ".txt");
        int size =0;
        for (int i=0; i<cluster.centers.size(); i++){
            for (Point q : cluster.centers.get(i).points){
                writer.write(cluster.centers.get(i).id + i + "," + q.x + "," + q.y + "\n");

            }
        }

        writer.close();
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
