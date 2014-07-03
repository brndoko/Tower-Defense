package game;

import java.awt.Graphics;

/**
 * Interface for all animation effects of tower and enemy objects within the game
 * 
 * @author Brandon Koch
 *
 */
public interface Effect {
	
	/**
	 * Controls the effect motion, interact with other game objects
	 * Keep track of the effect duration
	 * @param graphics g
	 * @param double deltaTime
	 */
	void interact (Game g, double deltaTime);
	
	/**
	 * Returns true when the effect should be removed
	 * @return boolean 
	 */
	boolean isDone ();
	
	/**
	 * Draws the effect
	 * @param graphics g
	 */
	void draw (Graphics g);
}
