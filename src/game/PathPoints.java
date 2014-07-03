/**
 * 
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 * @author pajensen
 *
 */
public class PathPoints
{
    List<Coordinate> path;
    
    /** This constructor does the following:
     *     - It creates a new ArrayList to hold the path, and stores it in the path variable 
     *     - It reads a number of coordinates, n, from the scanner
     *     - It loops n times, each time scanning a coordinate pair, building a
     *       coordinate object, and adding the coordinate object to the path list
     * 
     * @param s  a Scanner set up by the caller to scan through a list of coordinates
     */
     public PathPoints (Scanner s)
     {
         path = new ArrayList<Coordinate>();
         
         int count = s.nextInt();
         for (int i = 0; i < count; i++)
         {
             Coordinate c = new Coordinate(s.nextInt(), s.nextInt());
             path.add(c);
         }
     }

     /** Returns a new path position object that represents
      * the first position along this path.
      * 
      * @return the first position along this path
      */
     public PathPosition getStart ()
     {
         return new PathPosition(new ArrayList<Coordinate>(path));
     }    
     
     /**
      * Returns the distance (in pixels) from the
      * specified point to the nearest path point.
      */
     public double distanceToPath (double px, double py)
     {
         double minDistance = Double.MAX_VALUE;
         
         for (int i = 0; i < path.size()-1; i ++)
         {
             // Get start and end points for a segment.
             
             double sx = path.get(i).x;
             double sy = path.get(i).y;
             double ex = path.get(i+1).x;
             double ey = path.get(i+1).y;
             
             // Computed segment vector from start to end, and its length, then normalize it.
             
             double vx = ex - sx;
             double vy = ey - sy;
             double vl = Math.sqrt(vx * vx + vy * vy);
             vx /= vl;
             vy /= vl;
             
             // Compute vector from start to point
             
             double dx = px - sx;
             double dy = py - sy;
             double dl = Math.sqrt(dx * dx + dy * dy);
             
             // Compute the distance along the segment to the nearest point (dot product)
             
             double segDist = dx * vx + dy * vy;
             
             // If the nearest point is off one end of the segment or the other, 
             //   just compute the distance to that endpoint, otherwise use Pythagorean theorem.
             
             double distance;
             if (segDist < 0)
                 distance = Math.sqrt((vx-sx)*(vx-sx) + (vy-sy)*(vy-sy));
             else if (segDist > vl)
                 distance = Math.sqrt((vx-ex)*(vx-ex) + (vy-ey)*(vy-ey));
             else
                 distance = Math.sqrt(dl*dl - segDist*segDist);
             
             // If the distance is smaller than existing distance, keep it.
             
             if (distance < minDistance)
                 minDistance = distance;
         }
         
         return minDistance;
     }
     
     /**
      * Draws the current path to the screen (to whereever the graphics object points)
      * using a highly-visible color.
      * 
      * @param g   a graphics object
      */
     public void drawPath (Graphics g)
     {
         g.setColor(new Color(1f, 0f, 1f));
         for (int i = 0; i < path.size()-1; i ++)
         {
             g.drawLine(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
             g.drawLine(path.get(i).x, path.get(i).y+1, path.get(i+1).x, path.get(i+1).y+1);
         }    
     }
}
