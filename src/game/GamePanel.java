package game;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JPanel;

/**
 * The GamePanel class represents the drawable area on the screen.
 * This class is kept simple, and can be used as-is.
 * 
 * @author pajensen
 */
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener
{
    /* Static variables */
   
    /* This static variable is just to avoid an Eclipse warning.  It serves no other purpose (for us). */
    
    private static final long serialVersionUID = -266426690684141363L;
    
     
    /* Object fields and methods */
   
    private Game enclosingGame;  // A reference back to the Game object that created 'this' object.
    public int mouseX, mouseY;
    public boolean isMousePressed;
    
    
    /**
     * Creates the GamePanel object (which is really just
     * a JPanel object with a little extra functionality.)
     * The GamePanel represents a drawable area on the screen.
     * It has a paint method, and we can listen for events on this
     * object if we want.
     * 
     * @param enclosingGame the Game object that is creating this panel
     */
    public GamePanel (Game enclosingGame)
    {
        // Keep track of the Game object that created this panel.
        //   That way, we can call methods in the game object when needed.
        
        this.enclosingGame = enclosingGame;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    /**
     * Redraws the panel.  The panel does not have access to any of the objects
     * in the game.  (The panel does not have a path object, or a snail object, etc.)
     * This means the panel cannot directly draw them.  Instead, we'll just have
     * our panel call back to the Game object, and let the Game object draw
     * everything.
     * 
     * @param g  the Graphics object that corresponds to the panel
     */
    public void paintComponent (Graphics g)
    {
        enclosingGame.draw (g);
    }
    
    /* Overridden methods that report the correct panel size when needed. */
    
    public Dimension getMinimumSize ()
    {
        return new Dimension(800,600);
    }
    public Dimension getMaximumSize ()
    {
        return new Dimension(800,600);
    }
    public Dimension getPreferredSize ()
    {
        return new Dimension(800,600);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged (MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        isMousePressed = true;
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved (MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        isMousePressed = false;
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked (MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered (MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited (MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed (MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        isMousePressed = true;
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased (MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        isMousePressed = false;
        
    }
}
