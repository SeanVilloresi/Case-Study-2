import java.util.ArrayList;

public class Trajectory {
    
    String id;
    ArrayList<Point> points;
    double center_cost;

    public Trajectory(String id){
        this.id = id;
        points = new ArrayList<>();
        center_cost = -1;
    }
}
