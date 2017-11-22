/*******************************************************************************
* File: Chunk.java 
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
* Date Last Modified: 11/20/2017
* 
* Purpose: Allows for fewer render calls while creating more blocks when called 
* 
*******************************************************************************/ 
package pkgfinal.project;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk {
    
    //Constant to determine the size of the chunks
    static final int CHUNK_SIZE = 30;
    //Constant for the size of the cube
    static final int CUBE_LENGTH = 2;
    private Block[][][] Blocks; //3D space for our blocks of our chunk
    private int VBOVertexHandle;    //Holds the vertex information
    private int VBOColorHandle;     //Holds the color information
    private int StartX, StartY, StartZ; //Start coords of the chunk
    private Random r;   //RNG
    private int VBOTextureHandle;   //Holds the texture information
    private Texture texture;    //Holds the textures
    
    //Constructor: Chunck
    //Purpose: Creates a chunk at the given coordinates
    public Chunk(int startX, int startY, int startZ) {
        //Gets the texture image
        try {
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png"));
            
        } catch (Exception e) {
            System.out.print("ER-ROAR!");
            System.out.println("Working Directory = " +
            System.getProperty("user.dir"));
            e.printStackTrace();
        }
        r = new Random();   //Sets up the RNG
        //Creates a new 3D block array
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        //Itterates through each block and creates it with a random texture
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    //Creates a Grass block
                    if (r.nextFloat() > 0.7f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    //Creates a Dirt block
                    } else if (r.nextFloat() > 0.7f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    //Creates a Water block
                    } else if (r.nextFloat() > 0.7f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    //Creates a Sand block
                    } else if (r.nextFloat() > 0.7f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                    //Creates a Bedrock block
                    } else if (r.nextFloat() > 0.7f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
                    //Creates a Stone block
                    } else {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    }
                }
            }
        }
        VBOTextureHandle = glGenBuffers();
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ);    //Draws the chunk
    }
    
    //Method: render()
    //Prupose: This method creates the scene with the appropriat block information
    //         for the chunk
    public void render() {
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2, GL_FLOAT, 0, 0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }
    
    //Method: rebuildMesh
    //Purpose: This method creates the hills and valleys of the chunk
    public void rebuildMesh(float startX, float startY, float startZ) {
        
        Random rand = new Random();
        //Values between 0.05 and 0.08 seemed to give the best looking results
        //Values greater than 0.15 crashed the program
        double persistence = .05;
        int seed = r.nextInt();
        //Create the simplexNoise to be able to get various heights
        SimplexNoise simplexNoise = new SimplexNoise(CHUNK_SIZE,persistence,seed);
        
        float lowestHeight = CHUNK_SIZE;
        //Used to make all blocks on a level the same texture
        Block.BlockType[] terrainLevels = getTerrainLevels();
        float[][] heights = new float[CHUNK_SIZE][CHUNK_SIZE];
        
        VBOTextureHandle = glGenBuffers();  //Create the texture handle
        VBOColorHandle = glGenBuffers();    //Create the color handle
        VBOVertexHandle = glGenBuffers();   //Create the vertex handle
        //Create the texture data
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        //Create the Position data
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        //Create the color data
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        //Iterate through each x,z coordinate position setting the heights
        for (int x = 0; x < CHUNK_SIZE; x+= 1) {
            for (int z = 0; z < CHUNK_SIZE; z += 1) {
                
                int i = (int)(startX + x * ((175 - startX) / 640));
                int j = (int)(startZ + z * ((175 - startZ) / 480));
                //Generate a height at the current position
                //Take the absolut value of the simplexNoise value so that there are no negative heights
                float height = (startY + (int)(100 * Math.abs(simplexNoise.getNoise(i,j))) * CUBE_LENGTH);
                if(height < 30)
                    height++;
                if(height < lowestHeight)
                    lowestHeight = height;
                heights[x][z] = height;
            }
        }
        
        for(int x = 0; x < CHUNK_SIZE; x++)
        {
            for(int z = 0; z < CHUNK_SIZE; z++)
            {
                //Update the data of each block at each position up to the height
                for (float y = 0; y <= heights[x][z]; y += 1) {
                    VertexPositionData.put(createCube((float)(startX + x * CUBE_LENGTH), (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)), (float)(startZ + z * CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int)x][(int)y][(int)z])));
                    if(y == 0)
                        VertexTextureData.put(createTexCube(0.0f, 0.0f, Block.BlockType.BlockType_Bedrock));
                    else if(y == heights[x][z])
                    {
                        if(heights[x][z] == lowestHeight)
                            VertexTextureData.put(createTexCube(0.0f, 0.0f, Block.BlockType.BlockType_Water));
                        else
                        {
                            if(rand.nextFloat() > 0.5f)
                                VertexTextureData.put(createTexCube(0.0f, 0.0f, Block.BlockType.BlockType_Sand)); 
                            else
                                VertexTextureData.put(createTexCube(0.0f, 0.0f, Block.BlockType.BlockType_Grass));
                        }
                    }
                    //Otherwise set the texture to the texture of the top level
                    else
                    {
                        if(rand.nextFloat() > 0.5f)
                            VertexTextureData.put(createTexCube(0.0f, 0.0f, Block.BlockType.BlockType_Dirt));
                        else
                            VertexTextureData.put(createTexCube(0.0f, 0.0f, Block.BlockType.BlockType_Stone));
                    }
                }
            }
        }
        VertexTextureData.flip();   //Flip the data so it is in the correct order
        VertexColorData.flip();     //Flip the data so it is in the correct order
        VertexPositionData.flip();  //Flip the data so it is in the correct order
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    //Method: getTerrainLevels
    //Purpose: This method gives each level of the chunk a specific texture
    //         The lowest level is always bedrock
    private Block.BlockType[] getTerrainLevels()
    {
        //The array of the textures at each level
        Block.BlockType[] terrainLevels = new Block.BlockType[CHUNK_SIZE];
        Random rand = new Random(); //Used to get a random texture on the top
        //The lowest level is always bedrock texture
        terrainLevels[0] = Block.BlockType.BlockType_Bedrock;
        //Move through the array giving each level a type except for the last level
        for(int i = 1; i < terrainLevels.length - 1; i++)
        {
            if(rand.nextFloat() > 0.5f)
                terrainLevels[i] = Block.BlockType.BlockType_Dirt;
            else
                terrainLevels[i] = Block.BlockType.BlockType_Stone;
        }
        //Give the last level a type
        if(rand.nextFloat() > 0.7f)
            terrainLevels[terrainLevels.length - 1] = Block.BlockType.BlockType_Sand;
        else if(rand.nextFloat() > 0.7f)
            terrainLevels[terrainLevels.length - 1] = Block.BlockType.BlockType_Water;
        else
            terrainLevels[terrainLevels.length - 1] = Block.BlockType.BlockType_Grass;
        return terrainLevels;
    }
    
    //Method: createCubeVertexCol
    //Purpose:
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }
    
    //Static Method: createCube
    //Purpse: This method creates a cube at the given location
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
            return new float[] {
                // TOP QUAD
                x + offset, y + offset, z,
                x - offset, y + offset, z,
                x - offset, y + offset, z - CUBE_LENGTH,
                x + offset, y + offset, z - CUBE_LENGTH,
                // BOTTOM QUAD
                x + offset, y - offset, z - CUBE_LENGTH, 
                x - offset, y - offset, z - CUBE_LENGTH,
                x - offset, y - offset, z,
                x + offset, y - offset, z,
                // FRONT QUAD
                x + offset, y + offset, z - CUBE_LENGTH, 
                x - offset, y + offset, z - CUBE_LENGTH, 
                x - offset, y - offset, z - CUBE_LENGTH,
                x + offset, y - offset, z - CUBE_LENGTH,
                // BACK QUAD
                x + offset, y - offset, z, 
                x - offset, y - offset, z,
                x - offset, y + offset, z,
                x + offset, y + offset, z,
                // LEFT QUAD
                x - offset, y + offset, z - CUBE_LENGTH, 
                x - offset, y + offset, z, 
                x - offset, y - offset, z, 
                x - offset, y - offset, z - CUBE_LENGTH,
                // RIGHT QUAD
                x + offset, y + offset, z, 
                x + offset, y + offset, z - CUBE_LENGTH, 
                x + offset, y - offset, z - CUBE_LENGTH,
                x + offset, y - offset, z 
            };
    }
    
    //Method: getCubeColor
    //Purpose: This method returns the color white
    private float[] getCubeColor(Block block) {
        /*switch(block.getID()) {
            case 1:
                return new float[] {0, 1, 0};
            case 2:
                return new float[] {1, 0.5f, 0};
            case 3:
                return new float[] {0, 0f, 1f};
                
        }*/
        return new float[] {1, 1, 1};
    }
    
    //Method createTextCube
    //Purpose: This method gives the given block its texture
    public static float[] createTexCube(float x, float y, Block.BlockType type) {
        float offset = (1024f / 16) / 1024f;
        switch(type) {
            //Grass Texture
            case BlockType_Grass:
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*3, y + offset*10, 
                    x + offset*2, y + offset*10, 
                    x + offset*2, y + offset*9,
                    x + offset*3, y + offset*9,
                    // TOP!
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*3, y + offset*0, 
                    x + offset*4, y + offset*0, 
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*4, y + offset*1, 
                    x + offset*3, y + offset*1, 
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*0, 
                    x + offset*4, y + offset*0, 
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0, 
                    x + offset*4, y + offset*0, 
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1};
            //Sand Texture
            case BlockType_Sand:
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*1, y + offset*12, 
                    x + offset*0, y + offset*12, 
                    x + offset*0, y + offset*11,
                    x + offset*1, y + offset*11,
                    // TOP!
                    x + offset*1, y + offset*12, 
                    x + offset*0, y + offset*12, 
                    x + offset*0, y + offset*11,
                    x + offset*1, y + offset*11,
                    // FRONT QUAD
                    x + offset*1, y + offset*12, 
                    x + offset*0, y + offset*12, 
                    x + offset*0, y + offset*11,
                    x + offset*1, y + offset*11,
                    // BACK QUAD
                    x + offset*1, y + offset*12, 
                    x + offset*0, y + offset*12, 
                    x + offset*0, y + offset*11,
                    x + offset*1, y + offset*11,
                    // LEFT QUAD
                    x + offset*1, y + offset*12, 
                    x + offset*0, y + offset*12, 
                    x + offset*0, y + offset*11,
                    x + offset*1, y + offset*11,
                    // RIGHT QUAD
                    x + offset*1, y + offset*12, 
                    x + offset*0, y + offset*12, 
                    x + offset*0, y + offset*11,
                    x + offset*1, y + offset*11};
            //Water Texture
            case BlockType_Water:
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*2, y + offset*12, 
                    x + offset*1, y + offset*12, 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    // TOP!
                    x + offset*2, y + offset*12, 
                    x + offset*1, y + offset*12, 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    // FRONT QUAD
                    x + offset*2, y + offset*12, 
                    x + offset*1, y + offset*12, 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    // BACK QUAD
                    x + offset*2, y + offset*12, 
                    x + offset*1, y + offset*12, 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    // LEFT QUAD
                    x + offset*2, y + offset*12, 
                    x + offset*1, y + offset*12, 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    // RIGHT QUAD
                    x + offset*2, y + offset*12, 
                    x + offset*1, y + offset*12, 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11};
            //Dirt Texture
            case BlockType_Dirt:
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // TOP!
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // BACK QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // RIGHT QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0};
            //Stone Texture
            case BlockType_Stone:
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*0, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*2,
                    x + offset*0, y + offset*2,
                    // TOP!
                    x + offset*0, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*2,
                    x + offset*0, y + offset*2,
                    // FRONT QUAD
                    x + offset*0, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*2,
                    x + offset*0, y + offset*2,
                    // BACK QUAD
                    x + offset*0, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*2,
                    x + offset*0, y + offset*2,
                    // LEFT QUAD
                    x + offset*0, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*2,
                    x + offset*0, y + offset*2,
                    // RIGHT QUAD
                    x + offset*0, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*2,
                    x + offset*0, y + offset*2};
            //Bedrock Texture
            case BlockType_Bedrock:
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    // TOP!
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    // FRONT QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    // BACK QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    // LEFT QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    // RIGHT QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1};
        }
        return null;
    }
}