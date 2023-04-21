import java.util.ArrayList;
import java.util.*;

public class task_three {
    
    //function to compute the minimum Frechet distance of two trajectories
    public static Assignment fd(Trajectory P, Trajectory Q){
        //Create a 2D array for saving the minimum max distance through P(i) Q(j)
        double[][] dynamic = new double[P.points.size()][Q.points.size()];
        //this for loop fils the array
        for(int i = 0; i < P.points.size(); i++){
            for(int j = 0; j < Q.points.size(); j++){
                //if either i or j is 0 we are in an edge case we need to calcualte manually
                if(i == 0 || j == 0){
                    //if we are at (0, 0) we set the max to this distance between P(0) and Q(0)
                    if(i == 0 && j == 0){
                        dynamic[i][j] = P.points.get(0).dist(Q.points.get(0));
                    //if i is 0, we update both arrays based on the value at (i, j-1)
                    } else if(i == 0){
                        dynamic[i][j] = Math.max(P.points.get(i).dist(Q.points.get(j)), dynamic[i][j-1]);
                    //if j is 0, we update both arrays based on the value at (i-1, j)
                    } else{
                        dynamic[i][j] = Math.max(P.points.get(i).dist(Q.points.get(j)), dynamic[i-1][j]);
                    }
                    continue;
                }
                //max distances of the three possible paths coming in
                double above = dynamic[i-1][j];
                double left = dynamic[i][j-1];
                double diag = dynamic[i-1][j-1];
                
                //choose incoming with smallest max
                //make dynamic[i][j] the larger of that max and the distance between P(i) and Q(j)
                double min = Math.min(above, Math.min(left, diag));
                dynamic[i][j] = Math.max(P.points.get(i).dist(Q.points.get(j)), min);
            }
        }

        //create our assignment object
        Assignment ret = new Assignment(Q.id, P.id);
        ret.tracked_stat = "fd";
        //set the stat value to the Frechet distance of the bottom right point in the array
        ret.stat = dynamic[P.points.size() - 1][Q.points.size() - 1];
        int i = P.points.size() - 1;
        int j = Q.points.size() - 1;
        //now we trace our way back through the array finding to create our list of edges
        while(!(i == 0 && j == 0)){
            ret.edges.add(new Edge(P.points.get(i), Q.points.get(j)));
            //edge cases
            if(i == 0){
                j = j - 1;
            } else if(j == 0){
                i = i - 1;
            } else{
                //find the path with smallest max to trace back
                double above = dynamic[i-1][j];
                double left = dynamic[i][j-1];
                double diag = dynamic[i-1][j-1];
                if(above < left && above < diag){
                    i = i - 1;
                } else if(left < above && left < diag){
                    j = j - 1;
                } else{
                    i = i - 1;
                    j = j - 1;
                }
            }         
        }
        //add (0, 0) as a pairing
        ret.edges.add(new Edge(P.points.get(0), Q.points.get(0)));
        //reverse the order of the points since we added them from back to front
        Collections.reverse(ret.edges);
        return ret;
    }

    //function to compute the minimum dynamic time warmping score of two trajectories
    public static Assignment dtw(Trajectory P, Trajectory Q){
        //create two 2D arrays, one for the sums of squared distances in the set of edges, and one the number of edges in the path
        double[][] sums = new double[P.points.size()][Q.points.size()];
        double[][] num_edges = new double[P.points.size()][Q.points.size()]; 
        
        //this for loop fills both arrays with the appropriate values
        for(int i = 0; i < P.points.size(); i++){
            for(int j = 0; j < Q.points.size(); j++){
                //if either i or j is 0 we are in an edge case we need to calcualte manually
                if(i == 0 || j == 0){
                    //if we are at (0, 0) we set the number of edges to one and the sum to the distance squared
                    if(i == 0 && j == 0){
                        sums[i][j] = Math.pow(P.points.get(0).dist(Q.points.get(0)), 2);
                        num_edges[i][j] = 1;
                    //if i is 0, we update both arrays based on the value at (i, j-1)
                    } else if(i == 0){
                        sums[i][j] = sums[i][j-1] + Math.pow(P.points.get(i).dist(Q.points.get(j)), 2);
                        num_edges[i][j] = num_edges[i][j-1] + 1;
                    //if j is 0, we update both arrays based on the value at (i-1, j)
                    } else{
                        sums[i][j] = sums[i-1][j] + Math.pow(P.points.get(i).dist(Q.points.get(j)), 2);
                        num_edges[i][j] = num_edges[i-1][j] + 1;
                    }
                    continue;
                }
                //distance between P(i) and Q(j)
                double curr_dist_squared = Math.pow(P.points.get(i).dist(Q.points.get(j)), 2);
                //value updated using (i-1, j)
                double above = (curr_dist_squared + sums[i-1][j]) / (num_edges[i-1][j] + 1);
                //value updated using (i, j-1)
                double left = (curr_dist_squared + sums[i][j-1]) / (num_edges[i][j-1] + 1);
                //value updated using (i-1, j-1)
                double diag = (curr_dist_squared + sums[i-1][j-1]) / (num_edges[i-1][j-1] + 1);
                //find the minimum
                double min = Math.min(above, Math.min(left, diag));
                
                //check which direction the minimum came from, update num egdes accordingly
                if(min == above) num_edges[i][j] = num_edges[i-1][j] + 1;
                if(min == left) num_edges[i][j] = num_edges[i][j-1] + 1;
                if(min == diag) num_edges[i][j] = num_edges[i-1][j-1] + 1;

                //update sums using new value for num edges
                sums[i][j] = min * num_edges[i][j];
            }
        }

        //now we have a complete table, we need to create our Assignment object to be returned
        Assignment ret = new Assignment(Q.id, P.id);
        ret.tracked_stat = "dtw";
        //stat is the final box in sums divided by the final box in num edges
        int i = P.points.size() - 1;
        int j = Q.points.size() - 1;
        ret.stat = sums[i][j] / num_edges[i][j];
        //now we need to trace our path back through the arrays to create our list of edges
        while(!(i == 0 && j == 0)){
            //add current edge to list
            ret.edges.add(new Edge(P.points.get(i), Q.points.get(j)));
            //edge cases
            if(i == 0){
                j = j - 1;
            } else if(j == 0){
                i = i - 1;
            } else{
                //do same checking process we did before to see which direction we came from
                double curr_dist_squared = Math.pow(P.points.get(i).dist(Q.points.get(j)), 2);
                double above = (curr_dist_squared + sums[i-1][j]) / (num_edges[i-1][j] + 1);
                double left = (curr_dist_squared + sums[i][j-1]) / (num_edges[i][j-1] + 1);
                double diag = (curr_dist_squared + sums[i-1][j-1]) / (num_edges[i-1][j-1] + 1);
                if(above < left && above < diag){
                    i = i - 1;
                } else if(left < above && left < diag){
                    j = j - 1;
                } else{
                    i = i - 1;
                    j = j - 1;
                }
            }         
        }
        //add (0,0) as final edge
        ret.edges.add(new Edge(P.points.get(0), Q.points.get(0)));
        //reverse the order of the egdes since we went from back to front
        Collections.reverse(ret.edges);
        return ret;
    }



    //main method where we will run our test cases
    public static void main(String[] args) throws Exception {
        
        //read in the data
        ArrayList<Point> points = File_methods.read_points_from_csv("Data//geolife-cars.csv");
        //create trajectories
        Trajectory T128_20080503104400 = new Trajectory("128-20080503104400");
        Trajectory T128_20080509135846 = new Trajectory("128-20080509135846");
        Trajectory T010_20081016113953 = new Trajectory("010-20081016113953");
        Trajectory T010_20080923124453 = new Trajectory("010-20080923124453");
        Trajectory T115_20080520225850 = new Trajectory("115-20080520225850");
        Trajectory T115_20080615225707 = new Trajectory("115-20080615225707");

        for(int i = 0; i < points.size(); i++){
            if(points.get(i).id.equals(T128_20080503104400.id)) T128_20080503104400.points.add(points.get(i));
            if(points.get(i).id.equals(T128_20080509135846.id)) T128_20080509135846.points.add(points.get(i));
            if(points.get(i).id.equals(T010_20081016113953.id)) T010_20081016113953.points.add(points.get(i));
            if(points.get(i).id.equals(T010_20080923124453.id)) T010_20080923124453.points.add(points.get(i));
            if(points.get(i).id.equals(T115_20080520225850.id)) T115_20080520225850.points.add(points.get(i));
            if(points.get(i).id.equals(T115_20080615225707.id)) T115_20080615225707.points.add(points.get(i));
        }

        //compute assignments
        Assignment one_fd = fd(T128_20080503104400, T128_20080509135846);
        Assignment one_dtw = dtw(T128_20080503104400, T128_20080509135846);
        Assignment two_fd = fd(T010_20081016113953, T010_20080923124453);
        Assignment two_dtw = dtw(T010_20081016113953, T010_20080923124453);
        Assignment three_fd = fd(T115_20080520225850, T115_20080615225707);
        Assignment three_dtw = dtw(T115_20080520225850, T115_20080615225707);

        //print results
        System.out.println("Frechet Distance of trajectories 128-20080503104400 and 128-20080509135846: " + one_fd.stat);
        System.out.println("DTW of trajectories 128-20080503104400 and 128-20080509135846: " + one_dtw.stat);
        System.out.println("Frechet Distance of trajectories 010-20081016113953 and 010-20080923124453: " + two_fd.stat);
        System.out.println("DTW of trajectories 010-20081016113953 and 010-20080923124453: " + two_dtw.stat);
        System.out.println("Frechet Distance of trajectories 115-20080520225850 and 115-20080615225707: " + three_fd.stat);
        System.out.println("DTW of trajectories 115-20080520225850 and 115-20080615225707: " + three_dtw.stat);

        

        
        //for visualization only
        // ArrayList<Edge> fdlengths = three_fd.edges;
        // ArrayList<Edge> dtwlengths = three_dtw.edges;
        // File_methods.createEdgeFile("t3_fd_115notsimp", fdlengths);
        // File_methods.createEdgeFile("t3_dtw_115notsimp", dtwlengths);

        //simplified 115 comparison
        
        // Trajectory simp1 = task_two.TS_greedy(T115_20080520225850, .03);
        // Trajectory simp2 = task_two.TS_greedy(T115_20080615225707, .03);
        // Assignment temp1fd = fd(simp1, simp2);
        // Assignment temp2dtw = dtw(simp1, simp2);
        // ArrayList<Edge> fdlengthssimp = temp1fd.edges;   
        // ArrayList<Edge> dtwlengthssimp = temp2dtw.edges;
        //File_methods.createEdgeFile("t3_fd_.03_115", fdlengthssimp);
        //File_methods.createEdgeFile("t3_dtw_.03_115", dtwlengthssimp);
    }
}
