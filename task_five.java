import java.io.IOException;
import java.util.*;
import java.util.Random;
import java.util.HashSet;
import java.util.HashMap;


public class task_five {

    public static ArrayList<Trajectory> randomSeed(ArrayList<Trajectory> set, int k){
        
        HashSet<Integer> seeds = new HashSet<>();
        Random random = new Random();
        
        while(k > 0){
            int randomInt = random.nextInt(set.size());
            if(!seeds.contains(randomInt)){
                seeds.add(randomInt);
                k--;
            }
        }

        ArrayList<Trajectory> ret = new ArrayList<>();

        for(int dex : seeds){
            set.get(dex).center_cost = 0;
            ret.add(set.get(dex));
        }
            
        return ret;
    }

    public static Trajectory chooseNextCenter(ArrayList<Trajectory> set, ArrayList<Trajectory> centers){
        double[] distances = new double[set.size()];
        double sumDistances = 0;

        for (int i=0; i<set.size(); i++){
            double minDistance = Double.MAX_VALUE;
            for (Trajectory center : centers) {
                double distance = task_three.dtw(set.get(i), center).stat;
                if (distance < minDistance) minDistance = distance;
            }
        
        distances[i] = minDistance;
        sumDistances += distances[i];
        }
        double rand = Math.random() * sumDistances;
        double cumulativeSum = 0;
        for (int i=0; i< set.size(); i++) {
            cumulativeSum += distances[i];
            if (cumulativeSum >= rand) return set.get(i);          
        }
        //shouldnt ever hit
        return null;
    }
    public static ArrayList<Trajectory> ourSeed(ArrayList<Trajectory> set, int k){
        ArrayList<Trajectory> centers = new ArrayList<>();
        for (int i= 0; i<k; i++){
            Trajectory temp = chooseNextCenter(set, centers);
            centers.add(temp);
        }
        return centers;
    }

    public static Clustering lloyds(String method, ArrayList<Trajectory> set, int k) throws IOException{
        
        ArrayList<Trajectory> centers;

        if (method.equals("Random")) centers = randomSeed(set, k);
        else if (method.equals("OurSeed")) centers = ourSeed(set, k);
        else throw new IllegalArgumentException("Invalid String Passed");

        int T_MAX = 30; 
        int yoinkSize = set.size() / (k*2);

        HashMap<Integer, Integer> center_assignments = new HashMap<>();
        ArrayList<Trajectory>[] clusters = new ArrayList[k];

        for(int i = 0; i < k; i++){
            clusters[i] = new ArrayList<>();
        }

        for(int i = 0; i < set.size(); i++){
            center_assignments.put(i, -1); 
        }
            
        boolean repeat = true;
        int currentBest = 99999999;
        Clustering currentBestC = null;
        while(repeat && T_MAX > 0){

            repeat = false;
            for(ArrayList<Trajectory> cluster : clusters){
                cluster.clear();
            }

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
                if(center_assignments.get(i) != min_dex){
                    repeat = true;
                }

                clusters[min_dex].add(set.get(i));
                center_assignments.put(i, min_dex);
            }
            
            for(int i = 0; i < k; i++){
                
                if(clusters[i].size() <= 10){

                    double max_cost = 0;
                    int max_cost_dex = -1;

                    for(int j = 0; j < k; j++){
                        
                        //checks to see if clusters meet our conditions to have trajectories yoinked
                        if(centers.get(j).center_cost >= max_cost && clusters[j].size() >= yoinkSize){
                            max_cost = centers.get(j).center_cost;
                            max_cost_dex = j;
                        }
                        
                    }
                    //essentially if no clusters meet our "ideal" criteria just take some from biggest cluster
                    if (max_cost_dex == -1){
                        int maxsize=0;
                        for (int j = 0; j < k; j++){
                            if (clusters[j].size()>maxsize) {maxsize = clusters[j].size(); max_cost_dex = j;}
                        }
                    }
                    //Essentially takes 10 trajectories from our "worst" cluster cost wise at random to give to a cluster thats too small
                    Random random = new Random();
                    for(int j = 0; j < 10; j++){
                        int randomInt = random.nextInt(clusters[max_cost_dex].size());
                        clusters[i].add(clusters[max_cost_dex].get(randomInt));
                        clusters[max_cost_dex].remove(randomInt);
                    }
                }
                centers.set(i, task_four.center2(clusters[i]));
                //System.out.println(clusters[i].size());
            }

            T_MAX--;
            //Checking to see if our current iteration is our new overall best iteration
            int temp=0;
            for (Trajectory p : centers) temp += p.center_cost;
            if (temp<currentBest){
                currentBest = temp;
                currentBestC = new Clustering(centers,clusters);
                currentBestC.cost=currentBest;
            }
            //Debug statements
            // System.out.println("Sum:" + temp);
            // System.out.println("NEXT ITER");
        }

        //Below we set up our final clustering and compare it to the "best" one through all our trials, and then determine which to use based on cost.
        int temp = 0;
        Clustering ret = new Clustering(centers, clusters);
        for (Trajectory p : ret.centers) temp += p.center_cost;
        ret.cost=temp;


        if (ret.cost>currentBestC.cost) return currentBestC;


        return ret;
    }


    public static void main (String[] args) throws Exception{
        
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
            t = task_two.TS_greedy(t, 0.05);
            set.add(t);
        }

            
        //lloyds("Random", set, 10);
        
        //Clustering k10 = lloyds("OurSeed", set, 10);
        
        //File_methods.createClusterCenterFile("k10centers", k10);
        //for (int i=0; i<10;i++){
            File_methods.computeAveragesFile("ourseedAverages", "OurSeed", set);
        //}
        
    }
}
