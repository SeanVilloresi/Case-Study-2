import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class task_four {
    
    //Approach 1: Brute Force Approach
    public static Trajectory center1(ArrayList<Trajectory> set) throws IOException{

        double minimumTotalDistance = Integer.MAX_VALUE;
        Trajectory currentCenter = null;

        //Iterate over all trajectories
        //compute what the cost would be if we used this trajectory as the center
        //update minimumTotalDistace and currentCenter accordingly
        for (Trajectory p : set){
            double current = 0;
            
            //get total distance to all other trajectories
            for (Trajectory q : set){
                if (!(q.equals(p))){
                current += task_three.dtw(p, q).stat;
                }
            }
            //if less than min so far, update min and currentCenter(which is what will be returned)
            if (current<minimumTotalDistance){
                minimumTotalDistance = current;
                currentCenter = p;
            }
        }
        //set center cost value of the Trajectory object
        currentCenter.center_cost = minimumTotalDistance;
        currentCenter.id="center";
        return currentCenter;
    }

    
     
    

    public static Trajectory center2(ArrayList<Trajectory> set){
        
        //find the maximum length trajecotry and get its mid-point
        int max_traj_size = 0;
        int max_traj_dex = 0;
        for(int i = 0; i < set.size(); i++){
            max_traj_size = Math.max(max_traj_size, set.get(i).points.size());
            if(set.get(i).points.size() == max_traj_size) max_traj_dex = i; 
        }
        int mid_dex = set.get(max_traj_dex).points.size() / 2;

        //this array will store the point in each trajectory that we are pairing together
        int[] paired_point_dexes = new int[set.size()];

        //iterate over all trajectories and get the closest point to the mid-point of the longest trajectory
        for(int i = 0; i < set.size(); i++){
            ArrayList<Point> p = set.get(i).points;
            double min_dist = Double.MAX_VALUE;
            int dex = 0;
            for(int j = 0; j < p.size(); j++){
                double dist = set.get(max_traj_dex).points.get(mid_dex).dist(p.get(j));
                if(dist < min_dist){
                    min_dist = dist;
                    dex = j;
                }
            }
            //assign this point of minimum distance to the list of paired points
            paired_point_dexes[i] = dex;
        }

        //arrays we will need for storing data for each bin
        double[] x_sum = new double[max_traj_size*2];
        double[] y_sum = new double[max_traj_size*2];
        int[] num_points = new int[max_traj_size*2];
        Arrays.fill(x_sum, 0);
        Arrays.fill(y_sum, 0);
        Arrays.fill(num_points, 0);

        //iterate over each trajectory and add its points sequentially to the proper bucket
        for(int i = 0; i < set.size(); i++){
            ArrayList<Point> p = set.get(i).points;
            //compute what bin we need to start adding the points from this trajectory
            //make sure the point in "paired_point_dexes" ends up in the right buckets
            int start_bin = max_traj_size - paired_point_dexes[i];
            for(int j = 0; j < p.size(); j++){
                int bin = start_bin + j;
                x_sum[bin] = x_sum[bin] + p.get(j).x;
                y_sum[bin] = y_sum[bin] + p.get(j).y;
                num_points[bin] = num_points[bin] + 1;
            }     
        }
        //create the center trajectory we will return
        Trajectory ret = new Trajectory("center");

        //iterate over all the bins... if there are points in the bin, compute their mean x and y
        //add point to the return trajectory
        for(int i = 0; i < max_traj_size*2; i++){
            if(num_points[i] == 0) continue;
            ret.points.add(new Point(x_sum[i] / num_points[i], y_sum[i] / num_points[i]));
        }
        
        return ret;
    }



    public static void main(String[] args) throws Exception {

        //Create set and put all the points for all the trajectories into an arraylist
        ArrayList<Trajectory> set = File_methods.readTrajectoriesFromFile("trajectory-ids.txt");
        ArrayList<Point> points = File_methods.read_points_from_csv("geolife-cars-upd8.csv");
        
        //Add points to all the trajectories
        for (Trajectory p : set){
            for(int i = 0; i < points.size(); i++){
                if(points.get(i).id.equals(p.id)) p.points.add(points.get(i));  
            }
        }

        //Simplify all Trajectories currently in our set with specified epsilon
        double epsilon = 0.3;
        ArrayList<Trajectory> simplified_set=new ArrayList<>();
        for (Trajectory p : set){
             simplified_set.add(task_two.TS_greedy(p, epsilon));
        }

       //Test Cases
        Trajectory first_method = center1(set);
        Trajectory second_method =center2(set);
        Trajectory first_method_simp = center1(simplified_set);
        Trajectory second_method_simp =center2(simplified_set);


        //compute cost for second approach since it is not automatically given to us
        double sum = 0;
        double sum_simp = 0;
        for (Trajectory p : set){
            sum += task_three.dtw(p, second_method).stat;
        }
        second_method.center_cost = sum;
        for (Trajectory p : simplified_set){
            sum_simp += task_three.dtw(p, second_method_simp).stat;
        }
        second_method_simp.center_cost = sum_simp;

        System.out.println("Approach I Center Cost: " + first_method.center_cost);
        System.out.println("Approach II Center Cost: " + second_method.center_cost);
        System.out.println("Approach I Center Cost(Epsilon = " + epsilon + "): " + first_method_simp.center_cost);
        System.out.println("Approach II Center Cost(Epsilon = " + epsilon + "): " + second_method_simp.center_cost);
    

        
        // for (int i=0; i<simplified_set.size(); i++){
        //         File_methods.createPointsFile("simplifiedtrajectory"+ i, simplified_set.get(i).points);
        // }

       

        //Just used to create point files for all trajectories in trajectory-ids
        // for (int i=0; i<set.size(); i++){
        //     File_methods.createPointsFile("trajectory"+ i, set.get(i).points);
        // }

        //File_methods.createPointsFile("center1", first_method.points);
        //File_methods.createPointsFile("center2", second_method.points);
        
        

    }


}
