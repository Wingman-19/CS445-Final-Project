/*******************************************************************************
* File: FPCameraController.java 
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
* Date Last Modified: 11/21/2017 
* 
* Purpose: This class will be used to control the camera in a first-person view.
*          It has methods to move the player around the screen, as well as
*          control the direction the player is looking. It has the gameLoop
*          method that is responsible for getting the user actions (what buttons
*          are pressed) and the render method that draws the cube on the screen.
* 
* Possible actions:
*       * The w,a,s,d keys and the arrow keys control the direction the player
*         moves around the screen
*       * The space bar moves the user up and the left shift key moves the player
*         down
*       * By moving the mouse around the screen, the player can look around
*       * The escape key closes the window and exits the program
*       * The e key toggles explore mode
*           - Explore mode allows the user to move freely around the map and 
*             does not account for the edges or changing the height of the camera
*           - When Explore mode is off, the camera stays on the map and raises
*             and lowers with respect to the height of the current location
* 
*******************************************************************************/ 
package pkgfinal.project;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

public class FPCameraController
{
    //The camera's position
    private Vector3f position = null;
    private Vector3f lPosition = null;
    
    private float yaw = 0.0f;   //Rotation around the Y-axis
    private float pitch = 0.0f; //Rotation around the X-axis
    private Vector3Float me;
    private Chunk chunk;
    private boolean explore; //Control explore mode
    
    //Constructor: FPCameraController
    //Purpose: This constructor sets up the position of the camera at the x-, y-,
    //         z-, coordinates
    public FPCameraController(float x, float y, float z)
    {
        position = new Vector3f(x, y, z);   //Sets position to (x, y, z)
        lPosition = new Vector3f(x, y, z);  //Sets lPosition to (x, y, z)
        lPosition.x = 0f;   //Change x of lPosition to 0
        lPosition.y = 15f;  //Change y of lPosition to 15
        lPosition.z = 0f;   //Change z of lPosition to 0
        explore = true; //Start with explore mode on
    }
    
    //Method: yaw
    //Purpose: This mehod adds the amount to the current yaw
    public void yaw(float amount) {yaw += amount;}
    
    //Method:  pitch
    //Purpose: This method adds the amount to the current pitch
    public void pitch(float amount) {pitch -= amount;}
    
    //Method: moveForward
    //Purpose: This method moves the user in the direction they are looking
    //         This direction is basesd on the yaw and affects the x- and the 
    //         z- directions
    public void moveForward(float distance)
    {
        float xOffset = distance * (float)(Math.sin(Math.toRadians(yaw)));
        float zOffset = distance * (float)(Math.cos(Math.toRadians(yaw)));
        position.x -= xOffset;  //Updates the x position with the new distance
        position.z += zOffset;  //Updates the z positoin with the new distance
        //If Explore mode is off adjust the height
        if(!explore)
        {
            //Try to update the y-position with the new x- and z- positions
            try
            {
                updateYPos();
            }catch(ArrayIndexOutOfBoundsException e1)    //Doesn't work
            {
                position.x += xOffset;  //Reset x
                //Try again with new z- and old x- position
                try
                {
                    updateYPos();
                }catch(ArrayIndexOutOfBoundsException e2)    //Doesn't work again
                {
                    position.x -= xOffset;  //Get new x- position
                    position.z -= zOffset;  //Reset z
                    //Try again with new x- and old z- position
                    try
                    {
                        updateYPos();
                    }catch(ArrayIndexOutOfBoundsException e3) //This doesn't work either
                    {
                        position.x += xOffset;  //Reset x
                    }
                }
            }
        }
    }
    
    //Method: moveBackward
    //Purpose: This method moves the user in the opposite direction they are looking
    //         This direction is basesd on the yaw and affects the x- and the 
    //         z- directions
    public void moveBackwards(float distance)
    {
        float xOffset = distance * (float)(Math.sin(Math.toRadians(yaw)));
        float zOffset = distance * (float)(Math.cos(Math.toRadians(yaw)));
        position.x += xOffset;  //Updates the x position with the new distance
        position.z -= zOffset;  //Updates the z position with the new distance
        //If Explore mode is off adjust the height
        if(!explore)
        {
            //Try to update the y-position with the new x- and z- positions
            try
            {
                updateYPos();
            }catch(ArrayIndexOutOfBoundsException e1)    //Doesn't work
            {
                position.x -= xOffset;  //Reset x
                //Try again with new z- and old x- position
                try
                {
                    updateYPos();
                }catch(ArrayIndexOutOfBoundsException e2)    //Doesn't work again
                {
                    position.x += xOffset;  //Get new x- position
                    position.z += zOffset;  //Reset z
                    //Try again with new x- and old z- position
                    try
                    {
                        updateYPos();
                    }catch(ArrayIndexOutOfBoundsException e3) //This doesn't work either
                    {
                        position.x -= xOffset;  //Reset x
                    }
                }
            }
        }
    }
    
    //Method: strafeLeft
    //Purpose: This method makes the camera move to the left
    //         This is based on yaw and affects the x- and the z- directions
    public void strafeLeft(float distance)
    {
        float xOffset = distance * (float)(Math.sin(Math.toRadians(yaw - 90)));
        float zOffset = distance * (float)(Math.cos(Math.toRadians(yaw - 90)));
        position.x -= xOffset;  //Updates the x position with the new distance
        position.z += zOffset;  //Updates the z position with the new distance
        //If Explore mode is off adjust the height
        if(!explore)
        {
            //Try to update the y-position with the new x- and z- positions
            try
            {
                updateYPos();
            }catch(ArrayIndexOutOfBoundsException e1)    //Doesn't work
            {
                position.x += xOffset;  //Reset x
                //Try again with new z- and old x- position
                try
                {
                    updateYPos();
                }catch(ArrayIndexOutOfBoundsException e2)    //Doesn't work again
                {
                    position.x -= xOffset;  //Get new x- position
                    position.z -= zOffset;  //Reset z
                    //Try again with new x- and old z- position
                    try
                    {
                        updateYPos();
                    }catch(ArrayIndexOutOfBoundsException e3) //This doesn't work either
                    {
                        position.x += xOffset;  //Reset x
                    }
                }
            }
        }
    }
    
    //Method: strafeRight
    //Purpose: This method makes the camera move to the right
    //         This is based on yaw and affects the x- and the z- directions
    public void strafeRight(float distance)
    {
        float xOffset = distance * (float)(Math.sin(Math.toRadians(yaw + 90)));
        float zOffset = distance * (float)(Math.cos(Math.toRadians(yaw + 90)));
        position.x -= xOffset;  //Updates the x position with the new distance
        position.z += zOffset;  //Updates the z position with the new distance
        //If Explore mode is off adjust the height
        if(!explore)
        {
            //Try to update the y-position with the new x- and z- positions
            try
            {
                updateYPos();
            }catch(ArrayIndexOutOfBoundsException e1)    //Doesn't work
            {
                position.x += xOffset;  //Reset x
                //Try again with new z- and old x- position
                try
                {
                    updateYPos();
                }catch(ArrayIndexOutOfBoundsException e2)    //Doesn't work again
                {
                    position.x -= xOffset;  //Get new x- position
                    position.z -= zOffset;  //Reset z
                    //Try again with new x- and old z- position
                    try
                    {
                        updateYPos();
                    }catch(ArrayIndexOutOfBoundsException e3) //This doesn't work either
                    {
                        position.x += xOffset;  //Reset x
                    }
                }
            }
        }
    }
    
    //Method: mvoeUp
    //Purpose: This method makes the camera move up
    //         This just subtracts the distance to the current y position
    public void moveUp(float distance)
    {
        position.y -= distance;
    }
    
    //Method: mvoeDown
    //Purpose: This method makes the camera move up
    //         This just adds the distance to the current y position
    public void moveDown(float distance)
    {
        position.y += distance;
    }
    
    //Method: lookThrough
    //Purpose: This method performs the transformations so the user is looking 
    //         in the direction the camera is
    public void lookThrough()
    {
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        glTranslatef(position.x, position.y, position.z);
    }
    
    //Method: gameLoop
    //Purpose: This method allows the game to be played. It creates our camera
    //         controller and gets the user actions. It is resposible for calling
    //         the render method and showing the display to the user
    public void gameLoop()
    {
        //Create a chunk
        Chunk chunk = new Chunk(0, 0, 0);
        int[] pos = chunk.getHighPos(); //Get the coordinates of the highest block in our chunk
        //Our camera starting just above the highest position on our map
        FPCameraController camera = new FPCameraController(-pos[0] * Chunk.CUBE_LENGTH,
                                                           -pos[1] * Chunk.CUBE_LENGTH - (Chunk.CHUNK_SIZE - 1),
                                                           -pos[2] * Chunk.CUBE_LENGTH);
        camera.chunk = chunk;
        float dx = 0.0f;    //Change in the x direction
        float dy = 0.0f;    //Change in the y direction
        float dt = 0.0f;    //Change in the time
        float lastTime = 0.0f;  //The last time that was received
        long time = 0;  //The current time
        float mouseSensitivity = 0.09f; //How quickly the user looks around
        float movementSpeed = 0.35f;    //How fast the user moves around
        Mouse.setGrabbed(true); //Hides the mouse so it is in the window
        
        //Continues to show the display until the window is closed or the user 
        //clicks the escape key
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            time = Sys.getTime();   //Gets the current time
            lastTime = time;    //Sets this to the last time to compare later
            
            dx = Mouse.getDX(); //Gets the change in the x position of the mouse
            dy = Mouse.getDY(); //Gets the change in the y position of the mouse
            camera.yaw(dx * mouseSensitivity);  //Updates the yaw with the new position of the mouse
            camera.pitch(dy * mouseSensitivity);    //Updates the pitch with the new postion of the mouse
            
            //Calls to move forward when the w key or up key are pressed
            if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))
                camera.moveForward(movementSpeed);
            //Calls to move backwards when the s key or the down key are pressed
            if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
                camera.moveBackwards(movementSpeed);
            //Calls to strafe left when the a key or the left key are pressed
            if(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT))
                camera.strafeLeft(movementSpeed);
            //Calls to strafe right when the d key or the right key are pressed
            if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
                camera.strafeRight(movementSpeed);
            //Calls to move up when the space bar is pressed
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                camera.moveUp(movementSpeed);
            //Calls to move down when the left shift key is pressed
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                camera.moveDown(movementSpeed);
            //Toggle explore mode on/off
            if(Keyboard.isKeyDown(Keyboard.KEY_E))
            {
                //If we are in explore mode move back to the starting position
                //This prevents the camera from freezing when off the map
                if(camera.explore)
                {
                    camera.explore = false; //Turn off explore mode
                    //Set the postion
                    camera.position.x = -pos[0] * Chunk.CUBE_LENGTH;
                    camera.position.y = -pos[1] * Chunk.CUBE_LENGTH - (Chunk.CHUNK_SIZE - 1);
                    camera.position.z = -pos[2] * Chunk.CUBE_LENGTH;
                }
                else
                    camera.explore = true;  //Turn on explore mode
            }
            glLoadIdentity();
            camera.lookThrough();   //Performs the transformations
            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            chunk.render();   //Draws the scene
            Display.update();
            Display.sync(60);
        }
        Display.destroy();  //Closes the display
    }
    
    //Method: updateYPos
    //Purpose: This method sets the height at the new position to be just
    //         above the highest block in the stack. Throws an error if the
    //         new position is going to be off the map
    private void updateYPos()
    {
        //Only change the y position if the current position is on the map
        if((int)(Math.abs(position.x / Chunk.CUBE_LENGTH)) < Chunk.CHUNK_SIZE - 1 && position.x < 1 &&
           (int)(Math.abs(position.z / Chunk.CUBE_LENGTH)) < Chunk.CHUNK_SIZE - 1 && position.z < 1)
        {
            position.y = -chunk.getHeights()[(int)(Math.abs(position.x / Chunk.CUBE_LENGTH))][(int)(Math.abs(position.z / Chunk.CUBE_LENGTH))];
            position.y *= Chunk.CUBE_LENGTH;
            position.y -= Chunk.CHUNK_SIZE;
        }
        //Otherwise throw an error
        else
            throw new ArrayIndexOutOfBoundsException();
    }
    
    //Method: render
    //Purpose: This method is meant to draw the scene which happens to be a 2x2x2
    //         cude with different color sides right now
    /*private void render()
    {
        try
        {
            //Draw the 6 sides with different colors about the origin
            glBegin(GL_QUADS);
                //Top
                glColor3f(1.0f, 0.0f, 0.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                //Bottom
                glColor3f(0.5f, 0.5f, 0.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
                //Front
                glColor3f(0.0f, 1.0f, 0.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                //Back
                glColor3f(0.0f, 0.5f, 0.5f);
                glVertex3f(1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
                //Left
                glColor3f(0.0f, 0.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                //Right
                glColor3f(0.5f, 0.0f, 0.5f);
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();
            //Draw the outline to each edge of the cube in black
            glColor3f(0.0f, 0.0f, 0.0f);
            //Top
            glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
            glEnd();
            //Bottom
            glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();
            //Front
            glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
            glEnd();
            //Back
            glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
            glEnd();
            //Left
            glBegin(GL_LINE_LOOP);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
            glEnd();
            //Right
            glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();
        }catch(Exception e)
        {}
    }*/
}
