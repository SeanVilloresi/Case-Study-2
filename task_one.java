import java.util.ArrayList;


public class task_one{

    //method to check all the points in one box of the grid, and also mark points within the threshold as visited
    public static int check_box(Density_Grid grid, Point p, double radius, double threshold, int a, int b){
        //variable for counting the points in the box within radius of p
        int ret = 0;
        //for loop for visiting all the points in the grid box
        //first check sees if point is within the radius for density of p
        //second check sees if point is within the threshold of p so that we can mark the point as visited, so we will not calculate its density
        for(int i = 0; i < grid.regions[a][b].size(); i++){
            double dist = p.dist(grid.regions[a][b].get(i));
            if(dist < radius) ret++;
            if(dist < threshold) grid.regions[a][b].get(i).visited = true;
        }
        //return points in the box within radius of p
        return ret;
    }
    
    //function for computing the density at point p a grid of Points, a density radius,
    //and a threshold within which we will mark points so that we do not call density again
    public static int density(Density_Grid grid, Point p, double radius, double threshold){
        //compute the box p is in
        int[] box = compute_box(p, grid.xmin, grid.ymin, grid.box_width);
        int density = 0;
        //check the nine boxes surrounding p using calls to check_box()
        if(box[0] - 1 >= 0 && box[1] - 1 >= 0) density += check_box(grid, p, radius, threshold, box[0]-1, box[1]-1);
        if(box[1] - 1 >= 0) density += check_box(grid, p, radius, threshold, box[0], box[1]-1);
        if(box[0] + 1 < grid.regions.length && box[1] - 1 >= 0) density += check_box(grid, p, radius, threshold, box[0]+1, box[1]-1);
        if(box[0] - 1 >= 0) density += check_box(grid, p, radius, threshold, box[0]-1, box[1]);
        density += check_box(grid, p, radius, threshold, box[0], box[1]);
        if(box[0] + 1 < grid.regions.length) density += check_box(grid, p, radius, threshold, box[0]+1, box[1]);
        if(box[0] - 1 >= 0 && box[1] + 1 < grid.regions.length) density += check_box(grid, p, radius, threshold, box[0]-1, box[1]+1);
        if(box[1] + 1 < grid.regions.length) density += check_box(grid, p, radius, threshold, box[0], box[1]+1);
        if(box[0] + 1 < grid.regions.length && box[1] + 1 < grid.regions.length) density += check_box(grid, p, radius, threshold, box[0]+1, box[1]+1);
        return density;
    }

    //function that computes which box a point p goes in given some information about the grid
    public static int[] compute_box(Point p, double xmin, double ymin, double width){
        //calculate how many box widths from xmin and ymin our points is in order to compute the box coordinates
        int ret[] = new int[2];
        ret[0] = (int) ((p.x - xmin)/width);
        ret[1] = (int) ((p.y - ymin)/width);
        return ret;
    }
    
    //function for computing k hubs of high density that are all seperated by at least the radius parameter
    public static ArrayList<Point> hubs(ArrayList<Point> points, Density_Grid grid, int k, double radius){
        //array of hubs to return
        ArrayList<Point> hubs = new ArrayList<>();
        //array to copt the points into to seperate the ones we compute density for and the ones we do not
        ArrayList<Point> copy = new ArrayList<>();
        //visit all the points, but only calculate density if they have not been visited
        //meaning through other calls to density we may have marked point p as being very close to a point
        //we have already called density for
        for(Point p : points){
            if(!p.visited){
                //we use the box width as our density radius so we know all points within radius of p
                //will be in reached by our 9 surrounding box checks in density function
                //we use the box width over 30 because two circles of the same radius who are 1/30 
                //of that radius apart will share almost 98% of the same area, and therefore have almost identical densities
                p.density = density(grid, p, grid.box_width, grid.box_width / 20);
                copy.add(p);
            }
        }
        //add the max point to hubs that is not within radius of the points already in hubs
        //repeat k times
        while(hubs.size() < k){
            int max_d = 0;
            int max_dex = 0;
            for(int j = 0; j < copy.size(); j++){
                Point p = copy.get(j);
                boolean add = true;
                for(int i = 0; i < hubs.size(); i++){
                    if(p.dist(hubs.get(i)) < radius) add = false;
                }
                if(add && p.density > max_d){
                    max_d = p.density;
                    max_dex = j;
                }
            }
            hubs.add(copy.remove(max_dex));
        }
        
        //return k hugh density hubs
        return hubs;
    }

    //function for doing our pre-proccessing and creating our grip
    public static Density_Grid pre_proccess(ArrayList<Point> points){
        int n = points.size();
        //we compute the number of boxes based on n so that our average points per box is O(1)
        int num_boxes = ((int) Math.sqrt(n)) + 1;
        //compute min and max values for x and y
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;
        for(Point p: points){
            if(p.x < xmin) xmin = p.x;
            if(p.y < ymin) ymin = p.y; 
            if(p.x > xmax) xmax = p.x + 0.001;
            if(p.y > ymax) ymax = p.y + 0.001; 
        }
        //calculate our box width based on the range of x and y and our number of boxes
        double box_width = Math.max(xmax - xmin, ymax - ymin)/num_boxes;
        //create grid object
        Density_Grid grid = new Density_Grid(num_boxes, xmin, ymin, box_width);
        //compute box for each point and add it to the correct box
        for(Point p: points){
            int[] box = compute_box(p, xmin, ymin, box_width);
            grid.regions[box[0]][box[1]].add(p);
        }
        //return the grid object 
        return grid;
    }

    public static void main(String[] args) throws Exception {
        
        ArrayList<Point> points = File_methods.read_points_from_csv("Data//geolife-cars.csv");
        ArrayList<Point> points60 = File_methods.read_points_from_csv("Data//geolife-cars-sixty-percent.csv");
        ArrayList<Point> points30 = File_methods.read_points_from_csv("Data//geolife-cars-thirty-percent.csv");
        ArrayList<Point> points10 = File_methods.read_points_from_csv("Data//geolife-cars-ten-percent.csv");

        //set k and radius for test case calls
        //change these values to run different test cases
        int k = 10;
        int radius = 8;
        
        //test case on full data set
        long start = System.currentTimeMillis();
        Density_Grid grid = pre_proccess(points);
        long pre_full = System.currentTimeMillis() - start;
        long mid = System.currentTimeMillis();
        ArrayList<Point> full = hubs(points, grid, k, radius);
        long hubs_full = System.currentTimeMillis() - mid;
        long total_full = pre_full + hubs_full;
        System.out.println("Running time for full data set with k = " + k + " and radius = " + radius + ":");
        System.out.println("Preprocessing runtime: " + pre_full);
        System.out.println("Hubs runtime: " + hubs_full);
        System.out.println("Total runtime: " + total_full);

        //test case on 60%
        long start60 = System.currentTimeMillis();
        Density_Grid grid60 = pre_proccess(points60);
        long pre_60 = System.currentTimeMillis() - start60;
        long mid60 = System.currentTimeMillis();
        ArrayList<Point> sixty = hubs(points60, grid60, k, radius);
        long hubs_60 = System.currentTimeMillis() - mid60;
        long total_60 = pre_60 + hubs_60;
        System.out.println("Running time for 60% data set with k = " + k + " and radius = " + radius + ":");
        System.out.println("Preprocessing runtime: " + pre_60);
        System.out.println("Hubs runtime: " + hubs_60);
        System.out.println("Total runtime: " + total_60);

        //test case on 30%
        long start30 = System.currentTimeMillis();
        Density_Grid grid30 = pre_proccess(points30);
        long pre_30 = System.currentTimeMillis() - start30;
        long mid30 = System.currentTimeMillis();
        ArrayList<Point> thirty = hubs(points30, grid30, k, radius);
        long hubs_30 = System.currentTimeMillis() - mid30;
        long total_30 = pre_30 + hubs_30;
        System.out.println("Running time for 30% data set with k = " + k + " and radius = " + radius + ":");
        System.out.println("Preprocessing runtime: " + pre_30);
        System.out.println("Hubs runtime: " + hubs_30);
        System.out.println("Total runtime: " + total_30);

        //test case on 10%
        long start10 = System.currentTimeMillis();
        Density_Grid grid10 = pre_proccess(points10);
        long pre_10 = System.currentTimeMillis() - start10;
        long mid10 = System.currentTimeMillis();
        ArrayList<Point> ten = hubs(points10, grid10, k, radius);
        long hubs_10 = System.currentTimeMillis() - mid10;
        long total_10 = pre_10 + hubs_10;
        System.out.println("Running time for 10% data set with k = " + k + " and radius = " + radius + ":");
        System.out.println("Preprocessing runtime: " + pre_10);
        System.out.println("Hubs runtime: " + hubs_10);
        System.out.println("Total runtime: " + total_10);

        
        //Visualization
        File_methods.createPointsFile("t1_hubs", full);
        File_methods.createPointsFile("t1_points", points);

    }
}