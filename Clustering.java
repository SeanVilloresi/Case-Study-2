import java.util.ArrayList;

public class Clustering {
    
    
    ArrayList<Trajectory> centers;
    ArrayList<Trajectory>[] clusters;
    int num_clusters;
    double cost;

    public Clustering(ArrayList<Trajectory> centers, ArrayList<Trajectory>[] clusters){
        this.centers = centers;
        this.clusters = clusters;
        num_clusters = centers.size();
        cost = -1;
    }

    public double compute_cost(){
        this.cost = 0;
        for(int i = 0; i < centers.size(); i++){
            for(int j = 0; j < centers.size(); j++){
                cost = cost + task_three.dtw(centers.get(i), clusters[i].get(j)).stat;
            }   
        }
        return cost;
    }

}
