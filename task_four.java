import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class task_four {
    //Implement two approaches
    //Essentially, come up with a trajectory Tc that is a center trajectory between two others
    //Minimizes distance between itself and both trajectories respecivelly

    public static Trajectory center1(ArrayList<Trajectory> set) throws IOException{

        double minimumTotalDistance = Integer.MAX_VALUE;
        Trajectory currentCenter = null;

        //This may be too slow but im not sure
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
        System.out.println(minimumTotalDistance);
        return currentCenter;
    }

    




    public static Trajectory center2(ArrayList<Trajectory> set){
        // OK WHAT WE GON DO IS TREAT THE X COORDINATE LIKE A TIME AND TAKE THE AVERAGE OF ALL THE Y VALUES AT THAT POINT
        // USE A THRESHOLD TO ACCOUNT FOR THE X COORDS NOT EXACTLY LINING UP (aka all x's 1-1.1 will be considered at "the same time")
        
        int max_traj_size = 0;

        for(Trajectory t : set){
            max_traj_size = Math.max(max_traj_size, t.points.size());   
        }


        double[] x_sum = new double[max_traj_size];
        double[] y_sum = new double[max_traj_size];
        int[] num_points = new int[max_traj_size];
        Arrays.fill(x_sum, 0);
        Arrays.fill(y_sum, 0);
        Arrays.fill(num_points, 0);

        for(int i = 0; i < max_traj_size; i++){
            for(Trajectory t : set){
                if(t.points.size() - i - 1 < 0) continue;
                x_sum[i] = x_sum[i] + t.points.get(i).x;
                y_sum[i] = y_sum[i] + t.points.get(i).y;
                num_points[i] = num_points[i] + 1;
            }
                
        }

        Trajectory ret = new Trajectory("center");

        for(int i = 0; i < max_traj_size; i++){
            if(num_points[i] == 0) continue;
            ret.points.add(new Point(x_sum[i] / num_points[i], y_sum[i] / num_points[i]));
        }

        double minimumTotalDistance = 0;
        for (Trajectory p : set){
            minimumTotalDistance += task_three.dtw(ret, p).stat;
        }
        System.out.println(minimumTotalDistance);
        return ret;
    }

    public static Trajectory center3(ArrayList<Trajectory> set){
        // OK WHAT WE GON DO IS TREAT THE X COORDINATE LIKE A TIME AND TAKE THE AVERAGE OF ALL THE Y VALUES AT THAT POINT
        // USE A THRESHOLD TO ACCOUNT FOR THE X COORDS NOT EXACTLY LINING UP (aka all x's 1-1.1 will be considered at "the same time")
        
        int max_traj_size = 0;
        int max_traj_dex = 0;

        for(int i = 0; i < set.size(); i++){
            max_traj_size = Math.max(max_traj_size, set.get(i).points.size());
            if(set.get(i).points.size() == max_traj_size) max_traj_dex = i; 
        }

        int mid_dex = set.get(max_traj_dex).points.size() / 2;

        int[] paired_point_dexes = new int[set.size()];

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
            paired_point_dexes[i] = dex;
        }


        double[] x_sum = new double[max_traj_size*2];
        double[] y_sum = new double[max_traj_size*2];
        int[] num_points = new int[max_traj_size*2];
        Arrays.fill(x_sum, 0);
        Arrays.fill(y_sum, 0);
        Arrays.fill(num_points, 0);

        for(int i = 0; i < set.size(); i++){
            ArrayList<Point> p = set.get(i).points;
            int start_bin = max_traj_size - paired_point_dexes[i];
            for(int j = 0; j < p.size(); j++){
                int bin = start_bin + j;
                x_sum[bin] = x_sum[bin] + p.get(j).x;
                y_sum[bin] = y_sum[bin] + p.get(j).y;
                num_points[bin] = num_points[bin] + 1;
            }
                
        }

        Trajectory ret = new Trajectory("center");

        for(int i = 0; i < max_traj_size*2; i++){
            if(num_points[i] == 0) continue;
            ret.points.add(new Point(x_sum[i] / num_points[i], y_sum[i] / num_points[i]));
        }

        double minimumTotalDistance = 0;
        for (Trajectory p : set){
            minimumTotalDistance += task_three.dtw(ret, p).stat;
        }
        System.out.println(minimumTotalDistance);
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

        //System.out.print(set.get(2).points.size());
        Trajectory first_method = center1(set);

        //Not created yet lol
        Trajectory second_method =center2(set);

        Trajectory third_method =center3(set);

        ArrayList<Trajectory> simplified_set=new ArrayList<>();
        //Simplify all Trajectories currently in our set
        for (Trajectory p : set){
             simplified_set.add(task_two.TS_greedy(p, 0.1));
        }

        center1(simplified_set);
        center2(simplified_set);
        center3(simplified_set);
        
        // for (int i=0; i<simplified_set.size(); i++){
        //         File_methods.createPointsFile("simplifiedtrajectory"+ i, simplified_set.get(i).points);
        //      }

       

        //Just used to create point files for all trajectories in trajectory-ids
        // for (int i=0; i<set.size(); i++){
        //     File_methods.createPointsFile("trajectory"+ i, set.get(i).points);
        // }

        //File_methods.createPointsFile("center1", first_method.points);
        //File_methods.createPointsFile("center2", third_method.points);
        
        

    }


}
