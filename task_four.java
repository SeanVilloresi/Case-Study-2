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
            simplified_set.remove(p);
            //get total distance to all other trajectories
            for (Trajectory q : simplified_set){
                current += task_three.dtw(p, q).stat;
            }
            //if less then min so far, update min and currentCenter(which is what will be returned)
            if (current<minimumTotalDistance){
                minimumTotalDistance = current;
                currentCenter = p;
            }
            simplified_set.add(p);

        }


        return currentCenter;

    }


}
