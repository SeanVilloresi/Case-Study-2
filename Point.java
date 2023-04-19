import java.lang.Math;

//this is a class for creating a Point object
public class Point {
    
    //x and y coordinate, as well as id and time information
    double x;
    double y;
    String id;
    String date;
    //these variables are only relevent to task one, we arbitrarily assign density -1 and visited false
    int density;
    boolean visited;

    public Point(String date, String id, double x, double y){
        this.date = date;
        this.id = id;
        this.x = x;
        this.y = y; 
        density = -1;
        visited = false;
    }
    //second constructor if we want to use a point that has no id info
    public Point(double x, double y){
        this.date = "";
        this.id = "";
        this.x = x;
        this.y = y; 
    }

    //distance between two points
    public double dist(Point other){
        return Math.sqrt(((this.x - other.x) * (this.x - other.x)) + ((this.y - other.y) * (this.y - other.y)));
    }

}
