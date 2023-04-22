import java.util.*;
import java.util.Random;
import java.util.HashSet;


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
    public static ArrayList<Trajectory> lloyds(String method, ArrayList<Trajectory> set, int k){
        ArrayList<Trajectory> initialCenters;

        if (method.equals("Random")) initialCenters = randomSeed(set, k);
        else if (method.equals("OurSeed")) initialCenters = ourSeed(set, k);
        else throw new IllegalArgumentException("Invalid String Passed");
        
       


        return null;
    }


    public static void main (String[] args){

    }
}
