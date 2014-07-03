/**
 * 
 */
package game;

import java.awt.Graphics;

/**
 * @author pajensen
 *
 */
public interface Enemy
{
    public void update (double deltaTime);
    public PathPosition getPos ();
    public void draw (Graphics g);
}
