import java.io.IOException;
import java.util.*;
import java.util.Random;
import java.util.HashSet;
import java.util.HashMap;


public class task_five {

    //method for getting random seed
    public static ArrayList<Trajectory> randomSeed(ArrayList<Trajectory> set, int k){
        
        HashSet<Integer> seeds = new HashSet<>();
        Random random = new Random();
        
        //select random numbers in range of possible trajectories until we have k of them
        while(k > 0){
            int randomInt = random.nextInt(set.size());
            if(!seeds.contains(randomInt)){
                seeds.add(randomInt);
                k--;
            }
        }

        ArrayList<Trajectory> ret = new ArrayList<>();
        //add trajectories to return arrayList
        for(int dex : seeds){
            set.get(dex).center_cost = 0;
            ret.add(set.get(dex));
        }
            
        return ret;
    }

    public static Trajectory chooseNextCenter(ArrayList<Trajectory> set, ArrayList<Trajectory> centers){
        double[] distances = new double[set.size()];
        double sumDistances = 0;
        
        //calculate the distance from each trajectory to every center to determine closest center
        for (int i=0; i<set.size(); i++){
            double minDistance = Double.MAX_VALUE;
            for (Trajectory center : centers) {
                double distance = task_three.dtw(set.get(i), center).stat;
                if (distance < minDistance) minDistance = distance;
            }
        
        distances[i] = minDistance;
        sumDistances += distances[i];
        }

        //apply weights to each trajectory, and uses our double rand as our "choice" 
        double rand = Math.random() * sumDistances;
        double cumulativeSum = 0;

        //essentially the distance that puts us "over" is our randomly chosen one, thus farther distance=higher chance of being picked
        for (int i=0; i< set.size(); i++) {
            cumulativeSum += distances[i];
            if (cumulativeSum >= rand) return set.get(i);          
        }
        //shouldnt ever hit
        return null;
    }
    
    public static ArrayList<Trajectory> ourSeed(ArrayList<Trajectory> set, int k){
        ArrayList<Trajectory> centers = new ArrayList<>();
        //iteratively builds our arraylist of centers k times
        for (int i= 0; i<k; i++){
            Trajectory temp = chooseNextCenter(set, centers);
            centers.add(temp);
        }
        return centers;
    }

    public static Clustering lloyds(String method, ArrayList<Trajectory> set, int k) throws IOException{
        
        ArrayList<Trajectory> centers;
        //select seeds based on string paramter
        if (method.equals("Random")) centers = randomSeed(set, k);
        else if (method.equals("OurSeed")) centers = ourSeed(set, k);
        else throw new IllegalArgumentException("Invalid String Passed");

        //set number of iterations
        int T_MAX = 10; 
        
        //hash-map stores what center each traj is assigned to... 
        //tells us if we need to repeat Lloyds of if we have converged
        HashMap<Integer, Integer> center_assignments = new HashMap<>();
        //list of trajectories assigned to each cluster
        ArrayList<Trajectory>[] clusters = new ArrayList[k];

        for(int i = 0; i < k; i++){
            clusters[i] = new ArrayList<>();
        }

        for(int i = 0; i < set.size(); i++){
            center_assignments.put(i, -1); 
        }
        //this boolean tells us if we need to repeat iterations or if we have converged
        boolean repeat = true;

        //iterate until TMAX or we converge
        while(repeat && T_MAX > 0){

            repeat = false;
            //clear previous clusters
            for(ArrayList<Trajectory> cluster : clusters){
                cluster.clear();
            }

            //add each trajectory to the cluster with the nearest center
            for(int i = 0; i < set.size(); i++){
                double min_score = Double.MAX_VALUE;
                int min_dex = -1;
                for(int j = 0; j < k; j++){
                    double cur_score = task_three.dtw(set.get(i), centers.get(j)).stat;
                    if(cur_score < min_score){
                        min_score = cur_score;
                        min_dex = j;
                    }
                }
                //if a trajectory changes assignment, make sure we will iterate again
                if(center_assignments.get(i) != min_dex){
                    repeat = true;
                }

                //add trajectory to appropriate cluster
                clusters[min_dex].add(set.get(i));
                center_assignments.put(i, min_dex);
            }
            
            //make sure no clusters are empty, if so, we will find a new center for it
            for(int i = 0; i < k; i++){
                
                if(clusters[i].size() < 1){
                    
                    //find trajectory with the worst distance to its center
                    double localmax = 0;
                    Trajectory newcenter = new Trajectory("center");
                    for (int j=0;j<k;j++){
                        for (Trajectory p:clusters[j]){
                            double temp3 = task_three.dtw(centers.get(j), p).stat;
                            if (temp3> localmax){    
                                localmax = temp3;
                                newcenter = p;
                            }
                        }
                    }
                    
                    //Assign the empty center to the trajectory determined above
                    centers.set(i, newcenter);
                    centers.get(i).id="center";
                 
                }
                //get new centers for all clusters that were not reassigned through the process above (for being empty)
                if (clusters[i].size()>0){
                    //get new center
                    centers.set(i, task_four.center2(clusters[i]));
                    //calculate cost of clustering
                    double sum = 0;
                    for (Trajectory p : clusters[i]){
                        sum += task_three.dtw(p, centers.get(i)).stat;
                    }
                    centers.get(i).center_cost = sum;
                }
        }
            T_MAX--;
            //USE THIS FOR CHECKING COST OVER EACH ITERATION
            // int temp=0;
            // for (Trajectory p : centers) temp += p.center_cost;
            // System.out.println(temp);
           
            
        }

       //create return Clustering and calculate its cost
        int temp = 0;
        Clustering ret = new Clustering(centers, clusters);
        for (Trajectory p : ret.centers) temp += p.center_cost;
        ret.cost=temp;

        return ret;
        
    }


    public static void main (String[] args) throws Exception{
        
        //process for creating arrayList of trajectories
        ArrayList<Trajectory> set = File_methods.readTrajectoriesFromcsv("geolife-cars-upd8.csv");
        ArrayList<Point> points = File_methods.read_points_from_csv("geolife-cars-upd8.csv");
        
        
        HashMap<String, ArrayList<Point>> everything = new HashMap<>();

        for(Trajectory t: set){
            everything.putIfAbsent(t.id, new ArrayList<>());
        }

        for(Point p : points){
            everything.get(p.id).add(p);
        }

        set.clear();

        for (Map.Entry<String, ArrayList<Point>> entry : everything.entrySet()) {
            Trajectory t = new Trajectory(entry.getKey());
            for(Point p : entry.getValue()){
                t.points.add(p);
            }
            //change epsilon here if you wish to change epsilon for simplification
            t = task_two.TS_greedy(t, 0.01);
            set.add(t);
        }

        //Code for getting arrays to put in python visualization 
        //for average iterations over multiple runs  
        //MAKE SURE LINES 168-170 UNCOMMENTED TO PRINT COST AFTER EACH ITERATION
        // for (int i=0;i<5;i++){
        //     System.out.print("ourseedrun" + i + " = [" );
        //     lloyds("OurSeed", set, 10);
        //     System.out.println("]");
        // }

        // for (int i=0;i<5;i++){
        //     System.out.print("random" + i + " = [" );
        //     lloyds("Random", set, 10);
        //     System.out.println("]");
        // }
        
        //HERE IS A TEST CASE 
        //CHANGE K AND SEEDING METHOD AS DESIRED
        //TO SEE ITERATIVE OUTPUT, COMMENT OIN PRINT STATEMENT IN LLOYDS
        Clustering k = lloyds("Random", set, 12);
        
        //CREATE FILES FOR VISUALIZATIONS
        //File_methods.createClusterCenterFile("k10centers", k);
        
        //Compute averages for both random and our seed (3 runs)
        //File_methods.computeAveragesFile("randomAverages", "Random", set);
        //File_methods.computeAveragesFile("ourseedAverages", "OurSeed", set);
    
        
    }
}
