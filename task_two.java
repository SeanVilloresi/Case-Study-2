import java.util.ArrayList;



public class task_two {
    
    //compute the distance between a point q and an edge e
    public static double d(Point q, Edge e){
        //create a new Edge between q and the starting point of e
        Edge v = new Edge(e.start, q);
        //compute the orthogonal projection coefficient of v onto e
        double projection_coef = e.dot_product(v) / e.dot_product(e);
        //if the projections is less than 0 or greater than 1, the closest point is the start or end of the edge
        if(projection_coef < 0) return q.dist(e.start);
        if(projection_coef > 1) return q.dist(e.end);
        //otherwise, the closest point is found my moving from the start point along the egde
        //until reaching the ratio of the projection coefficient
        Point closest_point = new Point(e.start.x + (projection_coef * e.x_change), e.start.y + (projection_coef * e.y_change));
        //return the distance between q and its closest point on the egde
        return q.dist(closest_point);
    }

    public static Trajectory TS_greedy(Trajectory t, double epsilon){
        int num_points = t.points.size();
        //base case, since we need the starting and ending nodes
        if(num_points < 3) return t;
        //create an edge from the start point to the end point of the trajectory
        Point start = t.points.get(0);
        Point end = t.points.get(num_points - 1);
        Edge e = new Edge(start, end);
        //find which point in the trajectory is the maximum distance from the start-end edge
        double max_error = 0;
        int max_error_index = -1;
        for(int i = 1; i < num_points - 1; i++){
            double error = d(t.points.get(i), e);
            if(error > max_error){
                max_error = error;
                max_error_index = i;
            }
        }
        //create return Trajectory
        Trajectory ret = new Trajectory(t.id);
        //if max is less than epsilon, we can simplify to the trajectory to just the start and end points
        if(max_error <= epsilon){
            ret.points.add(start);
            ret.points.add(end);
            return ret;
        }
        //if not, create left and right trajectories for recursion
        Trajectory left = new Trajectory(t.id);
        Trajectory right = new Trajectory(t.id);

        //split into left and right halves based on the index of the maximum index
        for(int i = 0; i < num_points; i++){
            if(i < max_error_index) left.points.add(t.points.get(i));
            if(i == max_error_index){
                left.points.add(t.points.get(i));
                right.points.add(t.points.get(i));
            } 
            if(i > max_error_index) right.points.add(t.points.get(i));
        }
        //add points from left recursive call to the return trajectory
        for(Point p : TS_greedy(left, epsilon).points){
            ret.points.add(p);
        }
        //remove the last point, which is the point of max error, since it is in both sets and will be added twice
        ret.points.remove(ret.points.size() - 1);
        //add points from right recursive call to the return trajectory
        for(Point p : TS_greedy(right, epsilon).points){
            ret.points.add(p);
        }
        return ret;
    }
    
    
    public static void main(String[] args) throws Exception {
        
        ArrayList<Point> points = File_methods.read_points_from_csv("Data//geolife-cars.csv");
       

        //create trajectories
        Trajectory T128_20080503104400 = new Trajectory("128-20080503104400");
        Trajectory T010_20081016113953 = new Trajectory("010-20081016113953");
        Trajectory T115_20080520225850 = new Trajectory("115-20080520225850");
        Trajectory T115_20080615225707 = new Trajectory("115-20080615225707");
        for(int i = 0; i < points.size(); i++){
            if(points.get(i).id.equals(T128_20080503104400.id)) T128_20080503104400.points.add(points.get(i));
            if(points.get(i).id.equals(T010_20081016113953.id)) T010_20081016113953.points.add(points.get(i));
            if(points.get(i).id.equals(T115_20080520225850.id)) T115_20080520225850.points.add(points.get(i));
            if(points.get(i).id.equals(T115_20080615225707.id)) T115_20080615225707.points.add(points.get(i));
        }
        //create simplifications for test cases
        Trajectory T128_20080503104400_simp_3 = TS_greedy(T128_20080503104400, 0.3);
        Trajectory T128_20080503104400_simp_1 = TS_greedy(T128_20080503104400, 0.1);
        Trajectory T128_20080503104400_simp_03 = TS_greedy(T128_20080503104400, 0.03);
        Trajectory T010_20081016113953_simp_03 = TS_greedy(T010_20081016113953, 0.03);
        Trajectory T115_20080520225850_simp_03 = TS_greedy(T115_20080520225850, 0.03);
        Trajectory T115_20080615225707_simp_03 = TS_greedy(T115_20080615225707, 0.03);

        //calculate ratios
        double ratio_T128_20080503104400 = (double) T128_20080503104400_simp_03.points.size() / (double) T128_20080503104400.points.size();
        double ratio_T010_20081016113953 = (double) T010_20081016113953_simp_03.points.size() / (double) T010_20081016113953.points.size();
        double ratio_T115_20080520225850 = (double) T115_20080520225850_simp_03.points.size() / (double) T115_20080520225850.points.size();
        double ratio_T115_20080615225707 = (double) T115_20080615225707_simp_03.points.size() / (double) T115_20080615225707.points.size();

        System.out.println("Compression ratio for 128-20080503104400: " + ratio_T128_20080503104400);
        System.out.println("Compression ratio for 010-20081016113953: " + ratio_T010_20081016113953);
        System.out.println("Compression ratio for 115-20080520225850: " + ratio_T115_20080520225850);
        System.out.println("Compression ratio for 115_20080615225707: " + ratio_T115_20080615225707);
        
        
        //for visualization only
        // File_methods.createPointsFile("t2_initial_128", T128_20080503104400.points);
        // File_methods.createPointsFile("t2_128simp_.03", T128_20080503104400_simp_03.points);
        // File_methods.createPointsFile("t2_128simp_.1", T128_20080503104400_simp_1.points);
        // File_methods.createPointsFile("t2_128simp_.3", T128_20080503104400_simp_3.points);




    }
}



