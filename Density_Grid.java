import java.util.ArrayList;

//this is a class for creating a desnity grid object
public class Density_Grid {
    
    //the minimum x and y values of all the points
    double xmin;
    double ymin;
    //the dimensions of the boxes in our grid
    double box_width;
    //the actual grid.. each box is an ArrayList of points that go in that box
    ArrayList<Point> regions[][];

    public Density_Grid(int num_boxes, double xmin, double ymin, double box_width){
        this.xmin = xmin;
        this.ymin = ymin;
        this.box_width = box_width;
        regions = new ArrayList[num_boxes][num_boxes];
        for(int i = 0; i < num_boxes; i++){
            for(int j = 0; j < num_boxes; j++){
                this.regions[i][j] = new ArrayList<>();
            }
        }
    }

}
