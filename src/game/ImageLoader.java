/**
 * 
 */
package game;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author pajensen
 *
 */
public class ImageLoader
{
    /* Static fields and methods */
    
    static private ImageLoader instance;  // A reference to our singleton object
    
    static private String pathBase = "resources/";
    
    /**
     * Returns a reference to an ImageLoader singleton.  This
     * method allows applications to all share a single ImageLoader
     * object.  (Only one ImageLoader object will ever be created,
     * and this method returns a reference to it.)
     * 
     * @return the ImageLoader object
     */
    static public ImageLoader getLoader()
    {
        // If an ImageLoader object has not been built, build one.
        
        if (instance == null)
          instance = new ImageLoader();

        // Return the reference to the ImageLoader object.
        
        return instance;
    }

    /* Object fields and methods */
    
    private Map<String,Image> imageMap;  // Maps filenames to image objects
    
    /**
     * Not callable outside of this class.
     */
    private ImageLoader ()
    {
        imageMap = new TreeMap<String, Image>();
    }
    
    /**
     * Returns the image associated with a filename.  If the image
     * cannot be found, the method displays an error message and
     * exits the application.
     * 
     * Filenames are all relative to the "resources" directory.  In
     * other words, to load "resources/path1.jpg", just request "path1.jpg".
     * 
     * @param filename the filename of an image to load
     * @return the image stored in that file
     */
    public Image getImage (String filename)
    {
        Image loadedImage;
        
        // See if the image is in the map.  If so, return it.
        
        loadedImage = imageMap.get(filename);
        if (loadedImage != null)
            return loadedImage;
        
        // Load the image.
        
        try
        {
            ClassLoader myLoader = this.getClass().getClassLoader();
            InputStream imageStream = myLoader.getResourceAsStream(pathBase + filename);
            loadedImage = javax.imageio.ImageIO.read(imageStream);            
        }
        catch (Exception e)
        {
            System.out.println ("Could not load " + pathBase + filename + ": " + e.getMessage());
            System.exit(0);
        }
        
        // Put the image in the map and return it.
        
        imageMap.put(filename,  loadedImage);
        return loadedImage;
    }
}
