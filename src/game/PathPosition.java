/**
 * 
 */
package game;


import java.util.List;

/**
 * @author pajensen
 *
 */
public class PathPosition
{
    private int segment;
    private double percentage;
    private List<Coordinate> points;
    
    PathPosition (List<Coordinate> points)
    {
        this.segment = 0;
        this.percentage = 0;
        this.points = points;           
    }
    
    PathPosition (PathPosition other)
    {
        this.segment = other.segment;
        this.percentage = other.percentage;
        this.points = other.points;
    }
    
    /**
     * Returns true if this path position is at
     * the end of the path.
     * 
     * @return true if this position cannot be advanced any further
     */
    public boolean isAtTheEnd ()
    {
        return segment == points.size()-1;
    }
    
    /**
     * Returns a coordinate object containing the (x, y) location
     * of the current path position.  The path position is
     * converted to its (x, y) representation, a coordinate object
     * is built containing the (x, y) location, and the coordinate
     * is returned.
     * 
     * @return A Coordinate object containing the (x, y) location of this position
     */
    public Coordinate getCoordinate ()
    {
        if (isAtTheEnd ())
            return points.get(points.size()-1);
        
        Coordinate start = points.get(segment);
        Coordinate end = points.get(segment+1);
        
        double dx = end.x - start.x;
        double dy = end.y - start.y;

        Coordinate spot = new Coordinate((int)(start.x + dx * percentage), (int)(start.y + dy * percentage));
        
        return spot;        
    }
    
    /**
     * This method advances this path position by the specified number
     * of Cartesian units.  (Pythagorean's theorem is used internally
     * help with the computation.)
     *  
     * @param distance  the distance to travel along the path
     */
    public void advance (double distance)
    {
        if (isAtTheEnd()) 
            return;
        
        Coordinate start = points.get(segment);        
        Coordinate end = points.get(segment+1);
        
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        
        double totalDist = Math.sqrt(dx * dx + dy * dy);
        
        double midx = start.x + dx * percentage;
        double midy = start.y + dy * percentage;
        
        double rx = end.x - midx;
        double ry = end.y - midy;
        
        double remainingDist = Math.sqrt(rx * rx + ry * ry);
        
        if (remainingDist < distance)
        {
            segment++;
            percentage = 0;
            advance(distance-remainingDist);
        }
        else 
            percentage = (totalDist - remainingDist + distance) / totalDist;
    }    
}
