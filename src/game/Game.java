package game;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;

/* The class begins below the enum. */

/**
 * This 'enum' just creates a new type of data (like int or char).
 * The type is 'GameState', and the legal values are shown below.
 * We can create variables of this type, and we can store the 
 * values shown below into those variables.
 * 
 * The alternative would have been to use integers to represent states.
 * For example, we could have said 0=setup, 1=update, etc.  I don't like
 * using integers in this way because I would have to remember what
 * they mean.  By using an enum, I can store values that look like what
 * they represent.  SETUP = The game is setting up, etc.
 * 
 * @author Brandon Koch
 */
enum GameState { SETUP, UPDATE, DRAW, WAIT, END }

/**
 * This class represents the playable game.  If you create an
 * object from this class, you have created an instance of the
 * game, complete with JFrame, panel, etc.
 * 
 * The class also has a main method so that the class can
 * be run as an application.
 * 
 * @author pajensen
 */
public class Game implements Runnable
{
    /* Static methods */
    
    /**
     * The entry point for the game.
     * 
     * @param args not used
     */
    public static void main (String[] args)
    {
        // Just create a game object.  The game constructor
        //   will do the rest of the work.
        
        new Game ();
        
        // Main exits, but our other thread of execution
        //   will keep going.  We could do other work here if
        //   needed.
    }
    
    /* Object fields and methods */
    
    private GameState    state;   // The current game state
    private GamePanel    gamePanel;
    private Image        backdrop;
    private Image 		 gameover;
    private PathPoints   gardenPath;
    
    java.util.List<Enemy>  enemies;
    java.util.List<Tower>  towers;
    java.util.List<Effect> effects;
    
    int lives, money;
    protected int frameCounter;
    private long lastTime;
    
    private int generatorStep;
    private double generatorWait;
    
    private boolean placingTower;
    private boolean isGameOver;
    
    // You will declare other variables here.  These variables will last for
    //   the lifetime of the game, so don't store temporary values or loop counters
    //   here.

    /**
     * Constructor:  Builds a thread of execution, then starts it
     * on 'this' object.  This extra thread of execution will be
     * responsible for doing all the work of creating, running,
     * and playing the game.
     * 
     * (Note:  Drawing the screen happens inside of -another-
     * thread of execution controlled by Java.  Fortunately, we
     * don't care, but we are aware that some other threads
     * do exist.)
     */
    public Game ()
    {
        // The game starts in the SETUP state.
        
        state = GameState.SETUP;
        
        // Create a thread of execution and run it.
        
        Thread t = new Thread(this);
        t.start();  // Our run method is now executing!!!
    }
    
    /**
     * The entry point for the second thread of execution.  Our
     * game loop is entirely within this method.
     */
    public void run ()
    {
        // Loop forever, or until the user closes the game window,
        //   whichever comes first.  ;)
        
        while (true)
        {
            // Test our game state, and do the appropriate action.
            
            if (state == GameState.SETUP)
            {
                doSetupStuff();
            }
            
            else if (state == GameState.UPDATE)
            {
                doUpdateTasks();
            }
            
            else if (state == GameState.DRAW)
            {
                // We don't actually force the drawing to happen.
                //   Instead, we 'request' it of the panel.
                
                gamePanel.repaint();  // Uncomment this line when you have your gamePanel variable set up.
                
                // We must wait for the drawing.  It will happen at some time in the near future.
                //   Since we are in an infinite loop, we could just loop until we leave the draw
                //   state.  This would waste battery life on a low power device, so instead
                //   I choose to sleep the current thread for a very short while (so that it
                //   will be briefly inactive).
                
                try { Thread.sleep(5); } catch (Exception e) {}
                
                // Do not advance the state here.  The 'draw' method will advance the state after it draws.
            }
            
            else if (state == GameState.WAIT)
            {
                // Wait 1/10th a second.  This code is not ideal, we'll explore a better way soon.
                
                try { Thread.sleep(20); } catch (Exception e) {}
                
                // Drawing is complete, waiting is complete.  It is time to move
                //   the objects in the game again.  Re-enter the UPDATE state.
                
                state = GameState.UPDATE;
            }
            
            else if (state == GameState.END)
            {
                // Do cleanup if any.  (We don't need to do anything here yet.)
                System.exit(0);
            }
        }
    }
    
    /**
     * This setup function is called when the game is in the UPDATE state.
     * It just sets up a game, then enters any valid game state.
     */
    private void doSetupStuff ()
    {
        // Do setup tasks
        // Create the JFrame and the JPanel
        
        JFrame f = new JFrame();
        f.setTitle("Peter's Example");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamePanel = new GamePanel(this);
        f.setContentPane(gamePanel);
        f.pack();
        f.setVisible(true);
        
        // Load the background image.
        
        ImageLoader loader = ImageLoader.getLoader();
        backdrop = loader.getImage("path_2.jpg");
        
        // Load the game over image
        gameover = loader.getImage("game_over.png");
        
        // Build the garden path object.
        
        try
        {
            ClassLoader myLoader = this.getClass().getClassLoader();            
            InputStream pointStream = myLoader.getResourceAsStream("resources/path_2.txt");
            Scanner s = new Scanner (pointStream);
            gardenPath = new PathPoints(s);
        }
        catch (Exception e)
        {
            System.out.println ("Could not load: " + e.getMessage());
            System.exit(0);
        }
                
        // Create a list for the snails, towers, and effects
        
        enemies = new LinkedList<Enemy>();
        towers = new LinkedList<Tower>();
        effects = new LinkedList<Effect>();
        
        // test for a single effect
        Coordinate pos = new Coordinate(100, 100);
        
        // Reset the score.
        
        lives = 7;
        money = 200;
        
        // Reset the frame counter and time 
        
        frameCounter = 0;
        lastTime = System.currentTimeMillis();
        
        // Reset the generator
        
        generatorStep = 0;
        generatorWait = 0;
        
        // Set Game Over
        isGameOver = false;
        
        // Reset mouse stuff
        
        placingTower = false;
        
        // Change the game state to start the game.
        
        state = GameState.DRAW;  // You could also enter the 'UPDATE' state.
    }
    
    /**
     * This function is called repeatedly (once per game 'frame').
     * The update function should change the positions of objects in the game.
     * (It could also add new enemies, detect collisions, etc.)  This
     * function is responsible for the 'physics' of the game.
     */
    private void doUpdateTasks()
    {
        // Check for Game Over
    	if (lives < 1)
    	{
    		isGameOver = true;
    		lives = 0;
    	}
    	
    	if(isGameOver)
    	{
    		state = GameState.DRAW;
    		return;
    	}
    	
    	// See how long it was since the last frame.
        
        long currentTime = System.currentTimeMillis();  // Integer number of milliseconds since 1/1/1970.
        double elapsedTime = (currentTime - lastTime) / 1000.0;  // Compute elapsed seconds
        lastTime = currentTime;  // Our current time is the next frame's last time
        
        // Count this frame (I don't use this any more)
        
        frameCounter++;
        
        // Do update tasks
        // Advance the snail along the garden path.
        
        for (Enemy s : enemies)
            s.update(elapsedTime);
        
        // Remove snails that reach the end.
        
        List<Enemy> toBeRemoved = new ArrayList<Enemy>();
        
        for (Enemy s : enemies)
            if (s.getPos().isAtTheEnd())
            {
                toBeRemoved.add(s);
                if (s instanceof Snail)
                    lives--;
                else if (s instanceof SCargo)
                    lives -= 5;  // lives = lives - 5;
            }
        
        enemies.removeAll(toBeRemoved);
        
        // Generate new enemies
        
        generateEnemies(elapsedTime);
        
        // Place towers
               
        placeTowers ();
        
        // Tower interaction
        for (Tower t: towers)
        {
        	t.interact(this);
        }
        
     // Need help here
        // TO DO: Loop through the effects and 'interact' with each one
        
        for (Effect e: new ArrayList<Effect>(effects))
        {
        	e.interact(this, elapsedTime);
        	if(e.isDone())
        	{
        		effects.remove(e);
        	}
        }
        
        // After we have updated the objects in the game, we need to
        //   redraw them.  Enter the 'DRAW' state.
        
       state = GameState.DRAW;
       
       // Careful!  At ANY time after we set this state, the draw method
       //   may execute.  Don't do any further updating.
    }
    
    
    
    /**
     * Draws all the game objects, then enters the wait state.
     * 
     * @param g a valid graphics object.
     */
    public void draw(Graphics g)
    {
        // If we're not in the DRAW state, do not draw!
        
        if (state != GameState.DRAW)
            return;
        
        // Draw the backdrop image and the garden path line.
        
        g.drawImage(backdrop,  0, 0, null);        
        gardenPath.drawPath(g);
        
        // Draw the snail.  (We should be able to use the draw
        // method in the Snail class.  Note:  We should not move the
        // snail.  The objects in the game should only be moved in the
        // update method.)       
        
        for (Enemy s : enemies)
            s.draw(g);
       
        // Draw the towers
        
        for (Tower t : towers)
            t.draw(g);
        
        
        
        // If the user is placing a tower, create one temporarily to
        //   draw under the cursor.
                
        if (placingTower)
        {
            Coordinate c = new Coordinate (gamePanel.mouseX, gamePanel.mouseY);
            Tower temp = new Salt (c); 
            temp.draw(g);
        }
       
       // Draw the effects
        
       for(Effect e: effects)
    	   e.draw(g);
        
    // If Game Over
       if(isGameOver)
       {
       	g.drawImage(gameover, 0, 0, null);
       }
       // Clear the menu area on the right.
              
       g.setColor(Color.WHITE);
       g.fillRect(600, 0, 200, 600);
        
       // Draw a salt shaker in the menu area.
       
       g.setColor(new Color(1f, 1f, 0.8f));
       g.fillRect(660, 260, 80, 80);
       
       Salt salt = new Salt(new Coordinate(700, 300));
       salt.draw(g);
       
       // Draw the score.
       
       g.setColor (Color.BLACK);
       g.setFont(new Font("Lucidia Sans", Font.BOLD, 24));
       g.drawString("Lives: " + lives, 620, 100);
       g.drawString("Money: " + money, 620, 130);
        
       // If Game Over
       if(isGameOver)
       {
       	g.drawImage(gameover, 0, 0, null);
       }
       
        // Drawing is now complete.  Enter the WAIT state to create a small
        //   delay between frames.
        
        state = GameState.WAIT;
    }
    
    /**
     * This is just a helper function for generating enemies each frame.
     * I have updated it to use 'deltaTime' so that the enemy generation
     * rate is not tied to the frame update rate.  (It is tied to 'seconds'
     * instead.)
     * 
     * @param deltaTime
     */
    private void generateEnemies (double deltaTime)
    {
        // This string controls how enemies are created.  
        //   's' means to create a snail.
        //   'v' means to create a van.
        //   1-9 means delay this many tenths of a second.
        
        String enemyList = "s9v9s99s99s1s1s1s7" + 
                           "s8s7ssss9s9s9s9s6s7s8";  // This should be much, much longer (perhaps 300 seconds worth of enemies).
        
        // Wait if we need to.  (If the generator count is positive, we skip
        //   generation.)
        
        generatorWait -= (deltaTime * 10);  // Reduce the time we're supposed to wait.
        
        if (generatorWait > 0)  // If positive, bail.
            return;
        
        // Get a new start position
        
        PathPosition p = gardenPath.getStart();
        
        // get the next character from the enemy list
        
        char ch = enemyList.charAt(generatorStep);
        generatorStep++;
        
        // If we get to the end of the list, go back to the start.
        
        if (generatorStep >= enemyList.length())
            generatorStep = 0;
        
        // Generate the enemy (or set up to delay)
        
        if (ch == 's')
            enemies.add(new Snail(p));
        else if (ch == 'v')
            enemies.add(new SCargo(p));
        else
            generatorWait = ch - '0';  // Subtract ascii 0 from ascii #        
        
        // Done.
    }
    
    /**
     * This helper function responds to user mouse actions.  It
     * checks for the user clicking on menu buttons or the screen,
     * and reacts accordingly.
     */
    private void placeTowers ()
    {
        // I've cleaned this up a bit since lecture.  I removed the 'newTower'
        //   variable, it was not not needed and was confusing.
        
        // Is the mouse pressed on the menu button, and does the user have enough
        //   money for a tower?  If so, set a flag that indicates we should build a tower.
         
        if (gamePanel.isMousePressed && 
            gamePanel.mouseX > 660 && gamePanel.mouseX < 740 &&
            gamePanel.mouseY > 260 && gamePanel.mouseY < 340 &&
            money > 120)
        {
            placingTower = true;
        }
        
        // Otherwise, is the user currently placing a tower, and is the
        //   user clicking on the map sufficiently far away from the path?
        
        else if (placingTower && gamePanel.isMousePressed &&
            gamePanel.mouseX > 0 && gamePanel.mouseX < 600 &&
            gamePanel.mouseY > 0 && gamePanel.mouseY < 600 &&            
            gardenPath.distanceToPath(gamePanel.mouseX, gamePanel.mouseY) > 40)
        {
            Coordinate c = new Coordinate (gamePanel.mouseX, gamePanel.mouseY);
            Tower t = new Salt (c); 
            towers.add(t);
            
            money = money - 120;
            placingTower = false;
        }
    }
}

