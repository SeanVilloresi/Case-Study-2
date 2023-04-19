
//this file is for creatin edge objects
public class Edge {

        //instance variables are simply the starting and finishing points of the edge and the delta x and y of the edge
        Point start;
        Point end;
        double x_change;
        double y_change;

        public Edge(Point s, Point e){
            this.start = s;
            this.end = e;
            x_change = e.x - s.x;
            y_change = e.y - s.y;
        }

        //computes the dot product of two edges
        public double dot_product(Edge other){
            return ((this.x_change * other.x_change) + (this.y_change * other.y_change));
        }
        
        //computes the length of an edge
        public double length(){
            return Math.sqrt(this.x_change*this.x_change + this.y_change*this.y_change);
        }
}
