/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgfinal.project;

/**
 *
 * @author Luke Walsh
 */
public class Block {
    private boolean isActive;
    private BlockType Type;
    private float x,y,z;
    
    public enum BlockType {
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
    
    public Block(BlockType type) {
        Type = type;
    }
    
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
