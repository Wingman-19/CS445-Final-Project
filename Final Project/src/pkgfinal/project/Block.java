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
* Date Last Modified: 11/12/2017
* 
* Purpose: Used to specify one of six types of materials and create a block of it
* 
*******************************************************************************/ 
package pkgfinal.project;

public class Block {
    private boolean isActive;
    private BlockType Type;
    private float x,y,z;
    
    public enum BlockType {
        //the six different types of blocks we can create
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5);
        
        private int BlockID;
        
        //Creates a BlockType with a given ID
        BlockType(int i) {
            BlockID = i;
        }
        
        //Returns the ID of the BlockType
        public int GetID() {
            return BlockID;
        }
        
        //Sets the ID of the BlockType
        public void setID(int i) {
            BlockID = i;
        }
    }
    
    //Constructor: Block
    //Purpose: Creates a block and sets the type to the given type
    public Block(BlockType type) {
        Type = type;
    }
    
    //Method: setCoords
    //Purpose: This method sets the coordinates of the block
    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    //Method: isActive
    //Purpose: This method returns if the block is active
    public boolean isActive() {
        return isActive;
    }
    
    //Method: setActive
    //Purpose: This method sets the block to active or not active
    public void setActive(boolean active) {
        isActive = active;
    }
    
    //Method: getID
    //Purpose: This method returns the ID of the block
    public int getID() {
        return Type.GetID();
    }
}
