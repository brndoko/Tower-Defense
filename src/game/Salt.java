/**
 * 
 */
package game;

import java.awt.*;

/**
 * @author pajensen
 *
 */
public class Salt implements Tower
{
    protected Coordinate pos;
    private Image picture;
    private int anchorX, anchorY;  // A delta to the tower center.
    private boolean fired;
    private int timeSinceLastFired;
    
    public Salt (Coordinate pos)
    {
        this.pos = pos;
        
        ImageLoader loader = ImageLoader.getLoader();
        this.picture = loader.getImage("salt.png");
        
        this.anchorX = 25;
        this.anchorY = 30;
        
        fired = false;
        timeSinceLastFired = 100;
    }
    
    public void draw (Graphics g)
    {
        // Draw the shape for this enemy.
        
        g.drawImage(picture, pos.x - anchorX, pos.y - anchorY, null);
        
        // Debugging:  Draw a dot at the snail's location.
        
        g.setColor(Color.GREEN);
        g.fillOval(pos.x - 2, pos.y - 2, 5, 5);
    }

    /* (non-Javadoc)
     * @see game_checkpoint_3_start.Tower#interact(game_checkpoint_3_start.Game)
     */
    @Override
    public void interact (Game g)
    {
        // If the snail is within a certain distance of the tower
    	// the tower creates a new salt crystal effect and places
    	// it in the list
    	
    	timeSinceLastFired = timeSinceLastFired + 1;
    	
    	for (Enemy s : g.enemies)
    	{
	    	// Calculate the distance between the tower and the snail object
	    	double enemyX = s.getPos().getCoordinate().x;
	    	double enemyY = s.getPos().getCoordinate().y;
	    	double distance = Math.sqrt(Math.pow((enemyX - pos.x),2) + Math.pow((enemyY - pos.y), 2));
	    	
	  
		    	if(distance <= 100 && timeSinceLastFired > 100) // Tower is within a certain distance of snail object
		    	{	
		    		Coordinate crystalCoordinate = new Coordinate(s.getPos().getCoordinate().x - 100, s.getPos().getCoordinate().y - 100);
		    		
		    		// Create a new effect and add it to the list
		    		Effect crystal = new SaltCrystal (pos, crystalCoordinate);
		    		g.effects.add(crystal);
		    		timeSinceLastFired = 0;
		    		return;
		    	}
	    	}
	    	
    	}
    	

   
    public void setPosition (Coordinate c)
    {
        pos = c;
    }

}
