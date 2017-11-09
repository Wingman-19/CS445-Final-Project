/*******************************************************************************
* File: Vertex3Float.java 
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
* Date Last Modified: 11/8/2017
* 
* Purpose: Hold the x-, y-, z- coordinates
* 
*******************************************************************************/ 
package pkgfinal.project;

public class Vector3Float
{
    public float x, y, z;
    
    //Constructor: Vector3Float
    //Purpose: Assign the x, y, z values to the x-, y-, z- coordinates
    public Vector3Float(int x, int y, int z)
    {
        this.x = x; 
        this.y = y;
        this.z = z;
    }
}
