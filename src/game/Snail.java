/**
 * 
 */
package game;

import java.awt.*;

/**
 * @author pajensen
 *
 */
public class Snail implements Enemy
{
    private PathPosition pos;
    private double velocity;  // pixels per second
    private Image picture;
    private int anchorX, anchorY;  // A delta to the snail center.
    
    public Snail (PathPosition pos)
    {
        this.pos = pos;
        this.velocity = 72;
        
        ImageLoader loader = ImageLoader.getLoader();
        this.picture = loader.getImage("snail.png");
        
        this.anchorX = 22;
        this.anchorY = 19;
    }
    
    public void update (double deltaTime)
    {
        pos.advance(velocity * deltaTime);
    }
    
    public PathPosition getPos ()
    {
        return pos;
    }
    
    public void draw (Graphics g)
    {
        // Draw the shape for this enemy.
        
        Coordinate c = pos.getCoordinate();
        g.drawImage(picture, c.x - anchorX, c.y - anchorY, null);
        
        // Debugging:  Draw a dot at the snail's location.
        
        g.setColor(Color.CYAN);
        g.fillOval(c.x - 2, c.y - 2, 5, 5);
    }

}
