import java.util.*;

public class task_five {

    public static ArrayList<Trajectory> randomSeed(ArrayList<Trajectory> set){

        return null;
    }

    public static ArrayList<Trajectory> ourSeed(ArrayList<Trajectory> set){

        return null;
    }
    public static ArrayList<Trajectory> lloyds(String method, ArrayList<Trajectory> set){
        ArrayList<Trajectory> initialCenters;

        if (method.equals("Random")) initialCenters = randomSeed(set);
        else if (method.equals("OurSeed")) initialCenters = ourSeed(set);
        else throw new IllegalArgumentException("Invalid String Passed");
        
       


        return null;
    }





    public static void main (String[] args){

    }
}
