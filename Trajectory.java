import java.util.ArrayList;

public class Trajectory {
    
    String id;
    ArrayList<Point> points;

    public Trajectory(String id){
        this.id = id;
        points = new ArrayList<>();
    }
}
