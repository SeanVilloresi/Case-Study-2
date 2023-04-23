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

    public static ArrayList<Trajectory> ourSeed(ArrayList<Trajectory> set, int k){

        return null;
    }

    public static Clustering lloyds(String method, ArrayList<Trajectory> set, int k) throws IOException{
        
        ArrayList<Trajectory> centers;

        if (method.equals("Random")) centers = randomSeed(set, k);
        else if (method.equals("OurSeed")) centers = ourSeed(set, k);
        else throw new IllegalArgumentException("Invalid String Passed");

        int T_MAX = 100; 

        HashMap<Integer, Integer> center_assignments = new HashMap<>();
        ArrayList<Trajectory>[] clusters = new ArrayList[k];

        for(int i = 0; i < k; i++){
            clusters[i] = new ArrayList<>();
        }

        for(int i = 0; i < set.size(); i++){
            center_assignments.put(i, -1); 
        }
            
        boolean repeat = true;

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
                
                if(clusters[i].size() <= 20){

                    double max_cost = 0;
                    int max_cost_dex = -1;

                    for(int j = 0; j < k; j++){
                        
                        
                        if(centers.get(j).center_cost >= max_cost && clusters[j].size() >= 40){
                            max_cost = centers.get(j).center_cost;
                            max_cost_dex = j;
                        }
                        
                    }

                    Random random = new Random();
                    for(int j = 0; j < 20; j++){
                        int randomInt = random.nextInt(clusters[max_cost_dex].size());
                        clusters[i].add(clusters[max_cost_dex].get(randomInt));
                        clusters[max_cost_dex].remove(randomInt);
                    }
                }
                centers.set(i, task_four.center3(clusters[i]));
                //System.out.println(clusters[i].size());
            }

            T_MAX--;
            int temp=0;
            for (Trajectory p : centers) temp += p.center_cost;
            System.out.println("Sum:" + temp);
            System.out.println("NEXT ITER");
        }

        Clustering ret = new Clustering(centers, clusters);


        return ret;
    }


    public static void main (String[] args) throws Exception{
        ArrayList<Trajectory> set = File_methods.readTrajectoriesFromcsv("geolife-cars-upd8.csv");
        ArrayList<Point> points = File_methods.read_points_from_csv("geolife-cars-upd8.csv");
        //System.out.println(set.get(3).id);
        
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
            t = task_two.TS_greedy(t, 0.03);
            set.add(t);
        }

            
        lloyds("Random", set, 12);
        
        
    }
}
