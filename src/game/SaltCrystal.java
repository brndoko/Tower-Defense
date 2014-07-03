package game;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
/**
 * SaltCrystal effect that is utilized by towers to destroy enemy objects
 * @author Brandon Koch
 *
 */
public class SaltCrystal implements Effect
{
	private Coordinate position, target;
	private int velocityX, velocityY;
	private Image picture;
	private double ageInSeconds;
	
	/**
	 * Constructor for SaltCrystal effect
	 * @param position
	 * @param target
	 */
	SaltCrystal (Coordinate position, Coordinate target)
	{
		velocityX = target.x - position.x;
		velocityY = target.y - position.y;
		
		ImageLoader loader = ImageLoader.getLoader();
        this.picture = loader.getImage("salt_crystals.png");
        
        this.position = position;
        this.target = target;
        
        ageInSeconds = 0;
	}
	
	
	// The interact method in each effect will control the effect motion, 
	// interact with other game objects, and keep track of the effect duration
	@Override
	public void interact(Game g, double deltaTime) {
		
		// Move the effect toward the target, while adding the time to its age
		
		ageInSeconds = ageInSeconds + deltaTime;
		
		int x = (int) (position.x + velocityX * deltaTime);
		int y = (int) (position.y + velocityY * deltaTime);
		position = new Coordinate(x, y);
		
		// Create a copy of the enemies list
		List<Enemy> toBeRemoved = new ArrayList<Enemy>(g.enemies);
		
		// Loop through the enemies list
		for(Enemy e: toBeRemoved)
		{
			// Calculate the distance between the effect and the snail object
	    	double distance = Math.sqrt(Math.pow((e.getPos().getCoordinate().x - position.x),2) + Math.pow((e.getPos().getCoordinate().y - position.y), 2));
	    	
	    	if(distance <= 40)
	    	{	g.enemies.remove(e);
	    		g.money = g.money + 10;
	    	}
		}
		
	
	}
	
	/**
	 * If the age of the effect is greater than or equal to 1.5 seconds, the effect is done
	 */
	public boolean isDone() {
		return (ageInSeconds >= 1.5);
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(picture, position.x, position.y, null);
	}
	
}
