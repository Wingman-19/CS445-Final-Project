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
        //thee six different types of blocks we can create
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5);
        
        private int BlockID;
        
        BlockType(int i) {
            BlockID = i;
        }
        
        public int GetID() {
            return BlockID;
        }
        
        public void setID(int i) {
            BlockID = i;
        }
    }
    
    //constructor for the block that takes in what type of block it is
    public Block(BlockType type) {
        Type = type;
    }
    
    //sets the location of the block
    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void SetActive(boolean active) {
        isActive = active;
    }
    
    public int GetID() {
        return Type.GetID();
    }
}
