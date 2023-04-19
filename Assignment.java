import java.util.ArrayList;

//this class is from creating an assignment object of two trajectories
public class Assignment {
    
    //ids of two trajectories
    String id1;
    String id2;
    //list of edges in the assignemnt
    ArrayList<Edge> edges;
    //which stat we are tracking (fd or dtw) and what its value is
    String tracked_stat;
    double stat;

    public Assignment(String id1, String id2){
        this.id1 = id1;
        this.id2 = id2;
        edges = new ArrayList<>();
        this.tracked_stat = "";
        this.stat = Double.MAX_VALUE;
    }
}