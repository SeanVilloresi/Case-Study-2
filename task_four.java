import java.util.ArrayList;

public class task_four {
    //Implement two approaches
    //Essentially, come up with a trajectory Tc that is a center trajectory between two others
    //Minimizes distance between itself and both trajectories respecivelly

    public static Trajectory center1(ArrayList<Trajectory> set){

        ArrayList<Trajectory> simplified_set=new ArrayList<>();
        //Simplify all Trajectories currently in our set
        for (Trajectory p : set){
            simplified_set.add(task_two.TS_greedy(p, 0.1));
        }
        
        double minimumTotalDistance = 99999999;
        Trajectory currentCenter = null;

        //This may be too slow but im not sure
        for (Trajectory p : simplified_set){
            double current = 0;
            
            //get total distance to all other trajectories
            for (Trajectory q : simplified_set){
                if (!(q.equals(p))){
                current += task_three.dtw(p, q).stat;
                }
            }
            //if less then min so far, update min and currentCenter(which is what will be returned)
            if (current<minimumTotalDistance){
                minimumTotalDistance = current;
                currentCenter = p;
            }
            

        }


        return currentCenter;

    }



    public static void main(String[] args) throws Exception {
        //Set is right
        ArrayList<Trajectory> set = File_methods.readTrajectoriesFromFile("trajectory-ids.txt");
        ArrayList<Point> points = File_methods.read_points_from_csv("geolife-cars-upd8.csv");

        for (Trajectory p : set){
            for(int i = 0; i < points.size(); i++){
                if(points.get(i).id.equals(p.id)) p.points.add(points.get(i));  

            }
        }


        //System.out.print(set.get(2).points.size());
        Trajectory firstmethod = center1(set);

        
        File_methods.createPointsFile("center1", firstmethod.points);
        for (int i=0; i<set.size(); i++){
            File_methods.createPointsFile("trajectory"+ i, set.get(i).points);
        }
        

    }


}
