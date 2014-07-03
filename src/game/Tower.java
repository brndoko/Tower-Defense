/**
 * 
 */
package game;

import java.awt.Graphics;

/**
 * @author pajensen
 *
 */
public interface Tower
{
    public void draw (Graphics g);
    public void interact (Game g);
    public void setPosition (Coordinate c);
}
