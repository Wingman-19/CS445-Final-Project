/*******************************************************************************
* File: FinalProject.java 
* Group: The RenderMen
* Members:
*       * Marc Deaso
*       * Omar Rodriguez
*       * Nick Spencer
*       * Luke Walsh
*       * Alex Winger
* Class: CS 445: – Computer Graphics 
* 
* Assignment: Final Project 
* Date Last Modified: 11/12/2017 
* 
* Purpose: Creates the display and calls the camera's gameLoop method
* 
*******************************************************************************/ 
package pkgfinal.project;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class FinalProject
{
    //Camera object used to call the gameLoop method
    private FPCameraController fp;
    private DisplayMode displayMode;    //The display mode for the game
    
    //Method: main
    //Purpose: This is the main method of the program. It creates a new instance
    //         of the FinalProject class and starts it
    public static void main(String[] args)
    {
        FinalProject finalProject = new FinalProject();
        finalProject.start();
    }
    
    //Method: start
    //Purpose: This method creates the window, initializes it and calls the 
    //         gameLoop method
    private void start()
    {
        try
        {
            fp = new FPCameraController(0.0f, 0.0f, 0.0f);
            createWindow(); //Create the window
            initGL();   //Initialize the window
            fp.gameLoop();  //Call the gameLoop method
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    //Method: createWindow
    //Purpose: This method creates the window with the appropriate display mode
    private void createWindow() throws Exception
    {
        Display.setFullscreen(false);   //The window isn't full screen
        //Get all available display modes
        DisplayMode d[] = Display.getAvailableDisplayModes();
        //Move through the array until we find a display mode that works
        for(int i = 0; i < d.length; i++)
        {
            if(d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32)
            {
                displayMode = d[i];
                break;  //Break out of the loop when we find the display mode
            }
        }
        Display.setDisplayMode(displayMode);    //Set the display mode
        Display.setTitle("Final Project");  //Set the title
        Display.create();   //Create the display
    }
    
    //Method: initGL
    //Puprose: This method initializes the window with the background and the
    //         origin of the coordinate system
    private void initGL()
    {
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.6f, 0.8f, 1.0f, 0.0f);   //Sky colored background
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)displayMode.getHeight(), 0.1f, 300.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnable(GL_DEPTH_TEST);    //Hide the hidden faces of objects
    }
}
