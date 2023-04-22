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

        int T_MAX = 20; 

        HashMap<Integer, Integer> center_assignments = new HashMap<>();
        ArrayList<Trajectory>[] clusters = new ArrayList[k];

        for(ArrayList<Trajectory> list : clusters){
            list = new ArrayList<>();
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
                centers.add(i, task_four.center1(clusters[i]));
            }

            T_MAX--;

        }

        Clustering ret = new Clustering(centers, clusters);


        return ret;
    }


    public static void main (String[] args){

    }
}
